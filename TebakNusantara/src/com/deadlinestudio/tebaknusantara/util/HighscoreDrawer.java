package com.deadlinestudio.tebaknusantara.util;

import java.io.ObjectInputStream.GetField;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.screens.MenuScreen;

public class HighscoreDrawer extends Actor {

	private final float WIDTH;
	private final float HEIGHT;
	private final float RATIO;
	
	private Player[][] scoreboard;
	private MenuScreen scr;
	private BitmapFont mediumFont;
	private BitmapFont largeFont;
	
	TextBounds textbound;
	
	public HighscoreDrawer(Player[][] scoreboard, MenuScreen scr)
	{
		this.scoreboard = scoreboard;
		this.scr = scr;
		
		WIDTH = TebakNusantara.SCREEN_WIDTH;
		HEIGHT = TebakNusantara.SCREEN_HEIGHT;
		RATIO = TebakNusantara.RATIO;
		
		largeFont = scr.getFont(40);
		mediumFont = scr.getFont(32);
		
		largeFont.setColor(new Color(99f/255f, 50f/255f, 19f/255f, 1f));
		mediumFont.setColor(new Color(99f/255f, 50f/255f, 19f/255f, 1f));
	}
	@Override
	public void act(float arg0) {
		// TODO Auto-generated method stub
		super.act(arg0);
	}
	
	public TextBounds getBoundsLarge(String s)
	{
		return largeFont.getBounds(s);
	}
	
	public TextBounds getBoundsMedium(String s)
	{
		return mediumFont.getBounds(s);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
		
		textbound = getBoundsMedium("HALL OF FAME");
		mediumFont.draw(batch, "HALL OF FAME", (WIDTH/2 - textbound.width)/2, (HEIGHT * 8f/5f - textbound.height)/2 + 15.0f * RATIO);
		
		int id = scr.activeboard;
		
		if (scoreboard == null) return;
		
		float offsetY = (HEIGHT * 8f/5f - textbound.height)/2 - 20.0f * RATIO; 		
		for (int j = 1; j <= 7; ++j)
		{			
			Player p = scoreboard[j-1][id];
			if (p == null) break;
			
			String s = j + ".   ";
			for (int i = 0; i < 4 - p.getName().length(); i++)
			if (j < 10) s = s + "  ";
			
			s += p.getName();
			for (int i = 0; i < 10-(""+p.getScore()).length(); ++i)
			{
				s += " ";
			}
			
			if (p.getScore() == 0)
				s += "-";
			else
				s += p.getScore();
			
			if (j == 2)
				offsetY -= 5f * RATIO;
			offsetY -= 10f * RATIO;
			if (j == 1)
			{
				textbound = getBoundsLarge(s);
				largeFont.draw(batch, s, WIDTH/10f, offsetY);
				
			}
			else
			{
				textbound = getBoundsMedium(s);
				mediumFont.draw(batch, s, WIDTH/10f, offsetY);
			}
			offsetY -= textbound.height;
		}
	}
}
