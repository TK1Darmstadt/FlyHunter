package GUI;

import impl.Player;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginScreen {

	private JFrame frame;
	private JPanel panel;
	private JTextField username;
	private JLabel userNamelbl;
	private JButton ok;
	private Player client;
		
	public LoginScreen(){
		frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Welcome to Fly Hunter");
        frame.setSize(480,240);
        frame.setLocationRelativeTo(null);
        
        //frame.setLayout(new BorderLayout());
        panel= new JPanel();
        panel.setLayout(new FlowLayout());
        JPanel panel2 = new JPanel(new BorderLayout());
        JLabel hint= new JLabel("Please, type your username");
        ok= new JButton("OK");
        ok.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				String name= username.getText();
				client= new Player(name);
				new GameScreen(client);
				frame.setVisible(false);
			}
        	
        });
        
        userNamelbl= new JLabel("Username : ");
        username= new JTextField(20);
        panel.add(userNamelbl);
        panel.add(username);
        panel2.add(hint, BorderLayout.NORTH);
        panel2.add(panel, BorderLayout.CENTER);
        panel2.add(ok, BorderLayout.SOUTH);
        frame.add(panel2);
        frame.pack();
        frame.setVisible(true);
	}
	
	public Player getClient(){
		return client;
	}
	
}
