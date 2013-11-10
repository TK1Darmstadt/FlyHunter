package server;
/**
 * Game Server implementation
 * @author Maxime Peyrard, Jose San Juan, Christian Greciano
 *
 */
import impl.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Timer;

public class IGameServerImpl extends UnicastRemoteObject  implements IGameServer{
	private static final long serialVersionUID = 2674880711467464646L;
	private Map<String, Player> players;
	private short nb_game;
	private Map<String, Long> timers;
	private short next_ID;
	private Timer timer;
	private final int timeout = 5000;
	private Map<String, IGameClient> remote_players;
	private boolean game_started;
	
	protected IGameServerImpl() throws RemoteException {
		super();
		nb_game = 0;
		next_ID = 1;
		players = new HashMap<String, Player>();
		remote_players = new HashMap<String, IGameClient>();
		timers = new HashMap<String, Long>();
		game_started = false;
		
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				timer.restart();
				timer.stop();
				System.out.println("TIMEOUT");
				String winner = new String ("");
				long time_win = Long.MAX_VALUE;
				Iterator<Entry<String,Long>> itr = timers.entrySet().iterator();
				while (itr.hasNext()) {
					Map.Entry<String, Long> pairs = (Map.Entry<String, Long>)itr.next();
					if (pairs.getValue() < time_win) {
						time_win = pairs.getValue();
						winner = new String(pairs.getKey());
					}
				}
				
				Iterator<Entry<String, Player>> itr2 = players.entrySet().iterator();
				while (itr2.hasNext()) {
					Map.Entry<String, Player> pairs = (Map.Entry<String, Player>)itr2.next();
					if (!timers.containsKey(pairs.getKey())) {
						pairs.getValue().incrementTimeout();
						if ( players.get(pairs.getKey()).isDead() ) {
							try {
								logout(pairs.getKey());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				timers.clear();
				players.get(winner).incrementScore();
				broadcastWinner (winner);
			}
		 };
		timer = new Timer(timeout, taskPerformer);
	}
	
	@Override
	public short login(String playerName, IGameClient IGame) throws RemoteException {
		if (players.containsKey(playerName)) {
			return -1;
		}
		System.out.println(playerName + "Login successful");
		players.put(playerName, new Player(playerName));
		players.get(playerName).setId(next_ID);
		remote_players.put(playerName, IGame);
		
		if (players.size() == remote_players.size())
			System.out.println("List of players now : \n" + players.toString());
		
		if (players.size() >= 1 && !game_started) {
			startGame();
			System.out.println("New game Started");
			game_started = true;
		}
		short tmp = next_ID;
		next_ID++;
		return tmp;
	}

	@Override
	public void logout(String playerName) throws RemoteException {
		if (players.containsKey(playerName)) {
			players.remove(playerName);
			remote_players.remove(playerName);
		}
	}

	@Override
	public void huntFly(String playerName, long time) throws RemoteException {
		if (timers.isEmpty()) {
			 timer.start();
			 System.out.println("First hunt, time is running out");
		}
		System.out.println("Someone hunt : " + playerName+ "  " + time);
		System.out.println(timers.toString());
		timers.put(playerName, time);
		System.out.println("now : ");
		System.out.println(timers.toString());
		System.out.println(timers.size() + "   and " + players.size());
		
		
		if (timers.size() == players.size()) {
			timer.restart();
			timer.stop();
			String winner = new String("");
			long time_win = Long.MAX_VALUE;
			Iterator<Entry<String,Long>> itr = timers.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, Long> pairs = (Map.Entry<String, Long>)itr.next();
				if (pairs.getValue() < time_win) {
					time_win = pairs.getValue();
					winner = new String(pairs.getKey());
				}
			}
			timers.clear();
			players.get(winner).incrementScore();
			broadcastWinner (winner);
		}
	}
	
	public void broadcastWinner (String playerName) {
		System.out.println("Winner broadcast : "+ playerName + " score "+ players.get(playerName).getScore());
		Iterator<Entry<String, IGameClient>> itr = remote_players.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, IGameClient> pairs = (Map.Entry<String, IGameClient>)itr.next();
			try {
				System.out.println("Send to " + pairs.getKey());
				pairs.getValue().recieveFlyHunted(playerName, players.get(playerName).getScore());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("An other game will be started");
		startGame();
	}
	
	public Map<String, Integer> createPlayersInfos() {
		Map<String, Integer> tmp = new HashMap<String, Integer>();
		Iterator<Entry<String,Player>> itr = players.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, Player> pairs = (Map.Entry<String, Player>)itr.next();
			tmp.put(pairs.getKey(), pairs.getValue().getScore());
		}
		return tmp;
	}
	
	public void startGame() {
		nb_game++;
		int random_x = (int)(Math.random() * (Constant.size-1)) + 1;
		int random_y = (int)(Math.random() * (Constant.size-1)) + 1;
		Map<String, Integer> players_info = createPlayersInfos();
		
		Iterator<Entry<String, IGameClient>> itr = remote_players.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, IGameClient> pairs = (Map.Entry<String, IGameClient>)itr.next();
			try {
				pairs.getValue().recieveFlyPosition(random_x, random_y, players_info, nb_game);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
