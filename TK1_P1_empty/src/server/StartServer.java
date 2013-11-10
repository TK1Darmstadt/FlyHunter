package server;
/**
 * Main server class, to start rmi communication
 * @author Maxime Peyrard, Jose San Juan, Christian Greciano
 *
 */
import impl.Constant;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class StartServer {
	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(Constant.RMI_Port);
			
			//if (System.getSecurityManager() == null) 
				//System.setSecurityManager(new RMISecurityManager());
			
			IGameServerImpl IGame = new IGameServerImpl();
			
			String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + ":" + Constant.RMI_Port + "/" + Constant.RMI_Server_ID;
			Naming.rebind(url, IGame);
			
			System.out.println("Server started");
			
		} catch (Exception e) {
			System.out.println("Exception catched");
			e.printStackTrace();
		}
	}
}