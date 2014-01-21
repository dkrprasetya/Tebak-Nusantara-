package com.deadlinestudio.tebaknusantara.services;

import com.deadlinestudio.tebaknusantara.util.Player;

public class ScoreboardManager {
	private Player[][] scoreboard;
	
	public ScoreboardManager(Player[][] scoreboard)
	{
		this.scoreboard = scoreboard;
	}
	
	public Player[][] getScoreboard()
	{
		return scoreboard;
	}
	
	public void setScoreboard(Player[][] scoreboard)
	{
		this.scoreboard = scoreboard;
	}
	
	public boolean isNewHighscore(Player p, int game_id)
	{
		int score = p.getScore();
		
		for (int i = 0; i < 7; ++i)
		{
			if (score > scoreboard[i][game_id].getScore())
			{
				return true;
			}
		}
		return false;
	}
	
	public void insertToHighscore(Player p, int game_id)
	{
		for (int i = 0; i < 7; ++i)
		{
			if (p.getScore() > scoreboard[i][game_id].getScore())
			{
				for (int j = 6; j >= i+1; --j)
					scoreboard[j][game_id] = scoreboard[j-1][game_id];
				
				scoreboard[i][game_id] = p;
				
				break;
			}
			
		}
	}
}
