package GUI;

import main.*;
/**
 * Graphical User Interface
 * @author Jose San Juan, Maxime Peyrard, Christian Greciano Raiskila
 *
 */
public class GUI {
	private LoginScreen loginscreen;
	
	public GUI(IGameClientImpl IGame){
	loginscreen= new LoginScreen(IGame);
	}

}
