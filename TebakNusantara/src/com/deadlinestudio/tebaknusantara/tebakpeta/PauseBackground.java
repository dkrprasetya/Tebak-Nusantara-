package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PauseBackground extends TouchmapActor {

	public PauseBackground(TouchmapScreen screen) {
		super(screen);
		
		TextureRegion region = getScreen().getAtlas().findRegion("pause_bg");
		sprite = new Sprite(region);
		sprite.setBounds(0, 0, getScreen().getWidth(), getScreen().getHeight());
	}
	
}
