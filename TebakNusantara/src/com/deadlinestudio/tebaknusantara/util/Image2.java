package com.deadlinestudio.tebaknusantara.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.deadlinestudio.tebaknusantara.TebakNusantara;

public class Image2 extends Image{

	public float RATIO = TebakNusantara.RATIO;
	
	public Image2(TextureRegion reg)
	{
		super(reg);
	}
	
	public void setCustomRatio(float w, float h)
	{
    	if (TebakNusantara.SCREEN_WIDTH * h < TebakNusantara.SCREEN_HEIGHT * w)
    		RATIO = (float)TebakNusantara.SCREEN_WIDTH / (float)w;
    	else
    		RATIO = (float)TebakNusantara.SCREEN_HEIGHT / (float)h;
	}
	
	public Image2(Drawable dwb)
	{
		super(dwb);
	}
	
	
	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return super.getWidth() * TebakNusantara.RATIO;
	}
	
	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return super.getHeight() * TebakNusantara.RATIO;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		//super.draw(batch, parentAlpha);
		Color c = batch.getColor();
		batch.setColor(c.r, c.g, c.b, parentAlpha);
		
		getDrawable().draw(batch, getX(),getY(),getWidth(),getHeight());
	}
}
