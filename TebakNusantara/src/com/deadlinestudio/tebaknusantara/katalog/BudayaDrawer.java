package com.deadlinestudio.tebaknusantara.katalog;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.screens.KatalogScreen;

public class BudayaDrawer extends Actor {
	
	Budaya activeList[];
	BitmapFont font;
	KatalogScreen scr;
	
	public BudayaDrawer(Budaya activeList[], KatalogScreen scr) {
		// TODO Auto-generated constructor stub
		
		this.activeList = activeList;
		this.font = scr.getFont(24);
		this.scr = scr;
	}
	
	@Override
	public void act(float arg0) {
		// TODO Auto-generated method stub
		super.act(arg0);
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
		
		
		
		for (int i = 0; i < 7; ++i)
		{
			if (activeList[i] == null) continue;
			
			Budaya budaya = activeList[i];
			
			float x,y,w,h;
						
			w = budaya.getWidth();
			h = budaya.getHeight();
			
			
			x = (TebakNusantara.SCREEN_WIDTH-w)/2 + TebakNusantara.SCREEN_WIDTH / 2 * (i-3) + scr.offsetX;
			y = (TebakNusantara.SCREEN_HEIGHT-h)/2;
			
			if (x+w > 0.00 && x < TebakNusantara.SCREEN_WIDTH && y+h > 0.00 && y < TebakNusantara.SCREEN_HEIGHT)
			{
				budaya.getDrawable().draw(batch, x, y, w, h);
			}
			
			if (i == 3)
			{
				TextBounds textbound = font.getBounds(budaya.getNama());
				font.draw(batch, budaya.getNama(), (TebakNusantara.SCREEN_WIDTH - textbound.width)/2,
						(scr.bar.getHeight()*3/2 - textbound.height)/2 + scr.bar.getY() + 20.0f * TebakNusantara.RATIO);
				
				textbound = font.getBounds("Asal Daerah : " + budaya.getAsal());
				font.draw(batch, "Asal Daerah : " + budaya.getAsal(), (TebakNusantara.SCREEN_WIDTH - textbound.width)/2,
						(scr.bar.getHeight()/2 - textbound.height)/2 + scr.bar.getY() + 20.0f * TebakNusantara.RATIO);
			}
		}
	}
	
	@Override
	public boolean remove() {
		// TODO Auto-generated method stub
		font.dispose();
		return super.remove();
	}
	
	
}
