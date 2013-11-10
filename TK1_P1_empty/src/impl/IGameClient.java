package impl;
/**
 * Game Client interface
 * @author Maxime Peyrard, Jose San Juan, Christian Greciano
 *
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IGameClient extends Remote{
	void recieveFlyHunted(String playerName, int newPoints) throws RemoteException;
	void recieveFlyPosition(int x, int y, Map<String, Integer> players_info, int gamenb) throws RemoteException;
}
