package com.deadlinestudio.tebaknusantara.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deadlinestudio.tebaknusantara.TebakNusantara;

public class Button2 extends Button{

	private float offsetY; 
	private float elapsedtime;
	private boolean floating;
	private boolean wiggling;
	
	private int rotatemove;
	private float wiggletime;
	private float wiggleduration = 3.0f;
	private final float rotation = 2.0f;
	private float floatlength = 10.0f;
	
	public Button2(ButtonStyle style) {
		super(style);
		// TODO Auto-generated constructor stub
		
		offsetY = 0.0f;
		elapsedtime = 0.0f;
		wiggletime = 0.0f;
		floating = false;
		wiggling = false;
	}
	
	public void setFloating(boolean floating)
	{
		this.floating = floating;
		elapsedtime = 0.0f;
	}
	
	public void setFloatingLength(float len)
	{
		floatlength = len;
	}
	
	public void setWiggling(boolean wiggling)
	{
		this.wiggling = wiggling;
		elapsedtime = 0.0f;
		wiggletime = 0.0f;
		rotatemove = 1;
	}
	
	public boolean getFloating()
	{
		return floating;
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
	
	public void setWiggleDuration(float dur)
	{
		wiggleduration = dur;
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		
		if (wiggling)
		{
			elapsedtime += delta;
			if (elapsedtime >= wiggleduration - 0.5f)
			{
				wiggletime += delta;
				if (wiggletime >= 0.08f)
				{
					rotatemove = -1 * rotatemove;
					wiggletime = 0.0f;
				}
			}
			if (elapsedtime >= wiggleduration)
			{
				elapsedtime = 0.0f;
				wiggletime = 0.0f;
			}
		}
		else
		if (floating)
		{
			elapsedtime += delta;
			offsetY = (float)Math.sin((double)elapsedtime * 2.f) * floatlength * TebakNusantara.RATIO;
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		
		//super.draw(batch, parentAlpha);
		
		
		if (floating)
		{
			getStyle().up.draw(batch, getX(), getY()+offsetY, getWidth(), getHeight());
		}
		else
		if (isPressed())
		{
			float w = getWidth() * 11.0f / 12.0f, h = getHeight() * 11.0f / 12.0f;
			float x = getX() + getWidth() / 24.0f, y = getY() + getHeight() / 24.0f + offsetY;
			
			
			getStyle().up.draw(batch, x, y, w, h);
		}
		else
		if (wiggling)
		{
			if (elapsedtime >= wiggleduration-0.5f)
			{
				TextureRegion reg = ((TextureRegionDrawable)getStyle().up).getRegion();
				batch.draw(reg, getX(), getY(), getWidth()/2f, getHeight()/2f, getWidth(), getHeight(), 1f, 1f, rotation * rotatemove);
			}
			else
				getStyle().up.draw(batch, getX(), getY(), getWidth(), getHeight());
		}
		else
		{
			getStyle().up.draw(batch, getX(), getY(), getWidth(), getHeight());
		}
	}	
}
