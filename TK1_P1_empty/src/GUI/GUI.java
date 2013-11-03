package GUI;

/**
 * Graphical User Interface
 * @author Jose San Juan, Maxime Peyrard, Christian Greciano Raiskila
 *
 */
public class GUI {
	private LoginScreen loginscreen;
	
	public GUI(){
	loginscreen= new LoginScreen();
	}
	
	public static void main(String[] args){
		new GUI();
	}
}
