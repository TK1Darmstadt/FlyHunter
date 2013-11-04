package main;

public class Player {
	private int score;
	private String name;
	private short ID;
	
	public Player(String name){
		this.name=name;
		score=0;
		ID =-1;
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
	public void setId(short ID) {
		this.ID = ID;
	}
	public short getId(){
		return ID;
	}
	
	public void incrementScore(){
		score++;
	}
}
