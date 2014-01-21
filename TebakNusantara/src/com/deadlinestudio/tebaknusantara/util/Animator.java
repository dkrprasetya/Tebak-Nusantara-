package com.deadlinestudio.tebaknusantara.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.deadlinestudio.tebaknusantara.TebakNusantara;

public class Animator extends Actor
{
	private final Animation animation;
	private float stateTime;
	
	public Animator(Animation animation)
	{
		this.animation = animation;
		stateTime = 0.0f;
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		
		stateTime += delta;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
		
		TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
		
		Color c = batch.getColor();
	    batch.setColor(c.r, c.g, c.b, parentAlpha);
	    
		batch.draw(currentFrame, getX(), getY(), currentFrame.getRegionWidth() * TebakNusantara.RATIO, currentFrame.getRegionHeight() * TebakNusantara.RATIO);
	} 
}