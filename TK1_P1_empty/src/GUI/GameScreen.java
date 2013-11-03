package GUI;

import impl.Player;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GameScreen {
private static int LARGE=640;
private static final int HIGH= 480;

private int rows;
private int columns;

private JFrame frame;
private JPanel table;
private JPanel title;
private JPanel scores;
private JLabel user;
private JLabel titlelbl;
private JButton ok;
private Player clientplayer;
private List<Player> players;

private int gamenr;

private boolean[][] example = { {false, false, false, false, false, false, false, false}, {false, false, false, false, false, false, false, false},{false, false, false, false, false, false, false, false},{false, false, false, false, true, false, false, false},{false, false, false, false, false, false, false, false}};
	
	public GameScreen(Player client){
		
		prepareexample();
		this.clientplayer = client;
		players.add(clientplayer);
		gamenr = 0;
		frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(client.getName() + " hunt the fly!");
        frame.setSize(LARGE+200,HIGH+100);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        
        title = new JPanel(new BorderLayout());
        titlelbl= new JLabel("Game "+ gamenr);
        title.add(titlelbl);
        title.setSize(LARGE+200, 100);
        user= new JLabel("Your score : "+client.getScore());
        title.add(user, BorderLayout.EAST);
        
        
        table = paintTable(example);
        table.setVisible(true);
        
        JPanel playing = new JPanel();
        scores= new JPanel();
        scores.setLayout(new BoxLayout(scores, BoxLayout.Y_AXIS));
        playing.setMinimumSize(new Dimension(200, HIGH));
        scores.setAlignmentY(Component.CENTER_ALIGNMENT);
        playing.setAlignmentY(Component.CENTER_ALIGNMENT);
        fillScores();
        playing.setLayout(new BorderLayout());
        playing.add(scores, BorderLayout.SOUTH);
        
        frame.add(title, BorderLayout.NORTH);
        frame.add(table, BorderLayout.CENTER);
        frame.add(playing, BorderLayout.EAST);
        //frame.pack();
        frame.setVisible(true);
	}
	
	private JPanel paintTable(boolean[][] table){
		rows= table.length;
		columns= table[0].length;
		JPanel panel = new JPanel(new GridLayout(rows, columns));
        panel.setSize(LARGE, HIGH);
		for(int i=0; i< rows; i++){
			for (int j=0; j<columns; j++){
				JButton button = new JButton();
				button.setVisible(true);
				if (table[i][j]){
					try {
					    Image img = ImageIO.read(getClass().getResource("resources/fly.jpg"));
					    img.getScaledInstance(LARGE/columns, HIGH/rows, 0);
					    button.setIcon(new ImageIcon(img));
					  } catch (IOException ex) {
					  }
					button.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							wingame();
						}
					});
					button.setSize(LARGE/columns, HIGH/rows);
				}

				panel.add(button);
			}
		}
		return panel;
	}
	
	private void wingame(){
		gamenr++;
		clientplayer.incrementScore();
		 titlelbl.setText("Game "+ gamenr);
		 user.setText("Your score : "+clientplayer.getScore());
		 fillScores();
		System.out.println("You won");
		frame.repaint();
	}
	
	
	private void fillScores(){
		scores.removeAll();
		JLabel intro = new JLabel("People playing : ");
		intro.setAlignmentX(Component.RIGHT_ALIGNMENT);
		intro.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        scores.add(intro);
		for(Player player: players){
			JLabel playerlbl = new JLabel(player.getName() + " : "+ player.getScore());
			playerlbl.setAlignmentX(Component.LEFT_ALIGNMENT);
			playerlbl.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			scores.add(playerlbl);
		}
		
		frame.repaint();
	}
	
	private void prepareexample(){
		players= new ArrayList<Player>();
		Player p1= new Player("Maxime");
		Player p2= new Player("Christian");
		Player p3= new Player ("Jose");
		players.add(p1);
		players.add(p2);
		players.add(p3);
	}
	
	

}
