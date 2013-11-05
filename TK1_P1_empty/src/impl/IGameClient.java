package impl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IGameClient extends Remote{
	void recieveFlyHunted(String playerName, int newPoints) throws RemoteException;
	void recieveFlyPosition(int x, int y, Map<String, Integer> players_info) throws RemoteException;
}
