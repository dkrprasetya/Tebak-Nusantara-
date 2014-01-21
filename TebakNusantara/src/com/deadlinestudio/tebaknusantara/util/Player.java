package com.deadlinestudio.tebaknusantara.util;

public class Player {

	private String name;
	private int score;
	
	public Player(int score)
	{
		name = "-";
		this.score = score;  
	}
	
	public Player(String name, int score)
	{
		this.name = name;
		this.score = score;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getScore()
	{
		return score;
	}
}
