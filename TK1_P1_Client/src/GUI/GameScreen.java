package GUI;


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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.IGameClientImpl;
import impl.Player;

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
private JPanel playing;
private JButton ok;
private Player clientplayer;
private Map<String, Player> players;
private IGameClientImpl IGame;

private int gamenr;

private boolean[][] example; 

	public GameScreen(IGameClientImpl IGame){
		this.IGame = IGame;
		clientplayer = IGame.getPlayer();
		players = new HashMap<String, Player>(IGame.getPlayers());
		example = IGame.getBoard();
		
		gamenr = 0;
		frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(clientplayer.getName() + " hunt the fly!");
        frame.setSize(LARGE+200,HIGH+100);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        
        title = new JPanel(new BorderLayout());
        titlelbl= new JLabel("Game "+ gamenr);
        title.add(titlelbl);
        title.setSize(LARGE+200, 100);
        user= new JLabel("Your score : "+clientplayer.getScore());
        title.add(user, BorderLayout.EAST);
        
        
        table = paintTable(example);
        table.setVisible(true);
        
        playing = new JPanel();
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
							IGame.huntFly();
						}
					});
					button.setSize(LARGE/columns, HIGH/rows);
				}

				panel.add(button);
			}
		}
		return panel;
	}
	
	public void wingame(){
		gamenr++;
		 titlelbl.setText("Game "+ gamenr);
		 user.setText("Your score : "+clientplayer.getScore());
		 fillScores();
		System.out.println("You won");
		frame.repaint();
	}
	
	public void lostgame(String name){
		gamenr++;
		 titlelbl.setText("Game "+ gamenr);
		 user.setText("Your score : "+clientplayer.getScore());
		 fillScores();
		System.out.println("You lost ! \n" + name + "won the game");
		frame.repaint();
	}
	
	private void fillScores(){
		scores.removeAll();
		JLabel intro = new JLabel("People playing : ");
		intro.setAlignmentX(Component.RIGHT_ALIGNMENT);
		intro.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        scores.add(intro);
        Iterator<Entry<String, Player>> itr = players.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<String, Player> pairs = (Map.Entry<String, Player>)itr.next();
			JLabel playerlbl = new JLabel(pairs.getValue().getName() + " : "+ pairs.getValue().getScore());
			playerlbl.setAlignmentX(Component.LEFT_ALIGNMENT);
			playerlbl.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			scores.add(playerlbl);
		}
		
		frame.repaint();
	}
	
	public void repaint() {
		clientplayer = IGame.getPlayer();
		players = new HashMap<String, Player>(IGame.getPlayers());
		example = IGame.getBoard();
		
		title = new JPanel(new BorderLayout());
        titlelbl= new JLabel("Game "+ gamenr);
        title.add(titlelbl);
        title.setSize(LARGE+200, 100);
        user= new JLabel("Your score : "+clientplayer.getScore());
        title.add(user, BorderLayout.EAST);
        
        
        table = paintTable(example);
        table.setVisible(true);
        
        playing = new JPanel();
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
        frame.repaint();
	}
}
