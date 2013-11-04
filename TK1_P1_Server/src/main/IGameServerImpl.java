package main;

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
	private final int timeout = 1500;
	private Map<String, IGameClient> remote_players;
	
	protected IGameServerImpl() throws RemoteException {
		super();
		nb_game = 0;
		next_ID = 1;
		players = new HashMap<String, Player>();
		remote_players = new HashMap<String, IGameClient>();
		timers = new HashMap<String, Long>();
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				timer.stop();
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
		
		if (players.size() >= 1) 
			startGame();
		next_ID++;
		return next_ID--;
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
		timers.put(playerName, time);
		if (timers.isEmpty()) {
			 timer.start();
		}
		else if (timers.size() == players.size()) {
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
		Iterator<Entry<String, IGameClient>> itr = remote_players.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, IGameClient> pairs = (Map.Entry<String, IGameClient>)itr.next();
			try {
				pairs.getValue().recieveFlyHunted(playerName, players.get(playerName).getScore());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		startGame();
	}
	
	public void startGame() {
		nb_game++;
		int random_x = (int)(Math.random() * (Constant.size-1)) + 1;
		int random_y = (int)(Math.random() * (Constant.size-1)) + 1;
		Iterator<Entry<String, IGameClient>> itr = remote_players.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, IGameClient> pairs = (Map.Entry<String, IGameClient>)itr.next();
			try {
				pairs.getValue().recieveFlyPosition(random_x, random_y);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
