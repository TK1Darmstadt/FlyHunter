package main;

import impl.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import GUI.GameScreen;
import GUI.LoginScreen;

public class IGameClientImpl extends UnicastRemoteObject implements IGameClient {
	private static final long serialVersionUID = 443738388782351913L;
	private Player player;
	private long timer;
	private Coordinate flyPos;
	private boolean [][] board;
	private Map<String, Player> players;
	private GameScreen gameScreen;
	
	protected IGameClientImpl() throws RemoteException {
		super();
		player = new Player("");
		board = new boolean [8][8];
		for(int i=0; i<board.length; i++) {
	        for(int j=0; j<board[i].length; j++) {
	                board[i][j] = false;
	        }
		}
		
		flyPos = new Coordinate(0,0);
		board[0][0] = true;
		
		players= new HashMap<String, Player>();
	}
	
	public Map<String, Player> getPlayers() {
		return players;
	}
	
	public boolean[][] getBoard(){
		return board;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void login (String name) {
		System.out.println("Entré login");
		player.setName(name);
		try {
			Remote r = Naming.lookup("rmi://127.0.0.1:"+ Constant.RMI_Port+ "/" + Constant.RMI_Server_ID);
			if (r instanceof IGameServer) {
				short ID = ((IGameServer) r).login(name, this);
				if (ID < 1) {
					System.out.println("Pseudo already in use");
				}
				player.setId(ID);
			}
			
			players.put(player.getName(), player);
			System.out.println("Retour RPC");
			System.out.println(players.toString());
			
			if (gameScreen != null)
				gameScreen.repaint();
			else
				gameScreen = new GameScreen(this);
			
		}
		catch (MalformedURLException e) {
			 e.printStackTrace(); 
		} catch (RemoteException e) {	 
			 e.printStackTrace();
		} catch (NotBoundException e) {	 
			 e.printStackTrace();
		}
	}
	
	public void huntFly() {
		long time = System.currentTimeMillis() - timer;
		try {
			Remote r = Naming.lookup("rmi://127.0.0.1:"+ Constant.RMI_Port+ "/" + Constant.RMI_Server_ID);
			if (r instanceof IGameServer)
				((IGameServer) r).huntFly(player.getName(), time);	
		}
		catch (MalformedURLException e) {
			 e.printStackTrace(); 
		} catch (RemoteException e) {	 
			 e.printStackTrace();
		} catch (NotBoundException e) {	 
			 e.printStackTrace();
		}
	}
	
	
	public void logout() {
		try {
			Remote r = Naming.lookup("rmi://127.0.0.1:"+ Constant.RMI_Port+ "/" + Constant.RMI_Server_ID);
			if (r instanceof IGameServer)
				((IGameServer) r).logout(player.getName());	
			new LoginScreen(this);
		}
		catch (MalformedURLException e) {
			 e.printStackTrace(); 
		} catch (RemoteException e) {	 
			 e.printStackTrace();
		} catch (NotBoundException e) {	 
			 e.printStackTrace();
		}
	}
	
	
	@Override
	public void recieveFlyHunted(String playerName, int newPoints) throws RemoteException {
		System.out.println("ENtre recieve hunt ");
		System.out.println("score : "+players.get(playerName).getScore()+1);
		players.get(playerName).incrementScore();
		if (playerName.equals(player.getName())) {
			gameScreen.wingame();
		}
		else {
			gameScreen.lostgame(playerName);
		}
	}
	
	public void createPlayersList(Map<String, Integer> players_info) {
		this.players = new HashMap<String, Player>();
		players.put(player.getName(), player);
		Iterator<Entry<String,Integer>> itr = players_info.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String,Integer> pairs = (Map.Entry<String,Integer>)itr.next();
			if (!pairs.getKey().equals(player.getName())) {
				Player tmp = new Player (pairs.getKey(), pairs.getValue(), (short)-1, (short)0);
				this.players.put(pairs.getKey(), tmp);
			}
		}
	}

	@Override
	public void recieveFlyPosition(int x, int y, Map<String, Integer> players_info) throws RemoteException {
		System.out.println("receive pos ");
		System.out.println("prev : " + flyPos.getX() + " : " + flyPos.getY());
		System.out.println("now : " + x + " : "+ y);
		createPlayersList(players_info);
		System.out.println("Players on this game : \n" + this.players.toString());
		flyPos.setX(x);
		flyPos.setY(y);
		for(int i=0; i<board.length; i++) {
	        for(int j=0; j<board[i].length; j++) {
	                board[i][j] = false;
	        }
		}
		board[x][y] = true;
		timer = System.currentTimeMillis();
		gameScreen = new GameScreen(this);
	}
}
