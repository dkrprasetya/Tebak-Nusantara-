package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class GameConveyor extends TouchmapActor {

	public GameConveyor(TouchmapScreen screen) {
		super(screen);
		
		sprite = new Sprite(getScreen().getAtlas().findRegion("conveyor"));
		
		Rectangle crocBounds = getScreen().croc.getBounds();
		float scale = (0.9f * (getScreen().getWidth() - crocBounds.getWidth())) / sprite.getRegionWidth();
		float width = sprite.getRegionWidth() * scale;
		float height = sprite.getRegionHeight() * scale;
		float x = crocBounds.getX() + crocBounds.getWidth() + ((getScreen().getWidth() - crocBounds.getWidth() - width) / 2);
		float y = - height / 2;
		sprite.setBounds(x, y, width, height);
	}

}
