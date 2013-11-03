package impl;

public class Player {
	private int score;
	private String name;
	
	public Player(String name){
		this.name=name;
		score=0;
	}
	
	public String getName(){
		return name;
	}
	public int getScore(){
		return score;
	}
	public void setName(String name){
		this.name=name;
	}
	
	public void incrementScore(){
		score++;
	}
}
