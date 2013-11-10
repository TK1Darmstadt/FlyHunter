package impl;
/**
 * Game Server interface
 * @author Maxime Peyrard, Jose San Juan, Christian Greciano
 *
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameServer extends Remote{
	short login (String playerName, IGameClient IGame) throws RemoteException;
	void logout(String playerName) throws RemoteException;
	void huntFly(String playerName, long time) throws RemoteException;

}
