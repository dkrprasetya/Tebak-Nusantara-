package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PausePanel extends TouchmapActor {

	public PausePanel(TouchmapScreen screen) {
		super(screen);
		
		TextureRegion region = getScreen().getAtlas().findRegion("pause_panel");
		sprite = new Sprite(region);
		
		float scale = getScreen().getWidth() / region.getRegionWidth();
		if (scale > 1) scale = 1;
		
		setScale(scale);
		setX((screen.getWidth() - getWidth()) / 2);
		setY((screen.getHeight() - getHeight()) / 2);
	}
	
}
