package impl;

public class Player {
	private int score;
	private String name;
	private short ID;
	private short nb_timeouts;
	
	public Player(String name){
		this.name=name;
		nb_timeouts = 0;
		score=0;
		ID =-1;
	}
	
	public Player(String name, int score, short ID, short nb_timeouts) {
		this.name = name;
		this.nb_timeouts = nb_timeouts;
		this.score = score;
		this.ID = ID;
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
	
	public void incrementTimeout() {
		nb_timeouts++;
	}
	
	public boolean isDead () {
		if (nb_timeouts > 3) 
			return true;
		return false;
	}
	
	public String toString() {
		return name + " : " + score + ", " + nb_timeouts + ", " + ID + "\n";
	}
}