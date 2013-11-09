package client;
/**
 * Client
 * @author Maxime Peyrard, Jose San Juan, Christian Greciano
 *
 */
import GUI.*;

public class main {
	public static void main(String[] args) {
		try {
			final IGameClientImpl IGame = new IGameClientImpl();
			new GUI(IGame);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
