package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class PauseButton extends TouchmapActor {

	public boolean isSelected = false;
	private TextureRegion normalRegion;
	private TextureRegion selectedRegion;
	
	public PauseButton(TouchmapScreen screen, String name, float index) {
		super(screen, String.format("%s.%s", PauseButton.class.getSimpleName(), name));

		normalRegion = screen.getAtlas().findRegion("pause_" + name);
		selectedRegion = screen.getAtlas().findRegion("pause_" + name + "_s");
		
		sprite = new Sprite(normalRegion);
		float scale = screen.pausePanel.getScaleX();
		setScale(screen.pausePanel.getScaleX(), screen.pausePanel.getScaleY());
		
		float x = (screen.getWidth() - getWidth()) / 2 + (index * (scale * getWidth()));
		float y = (screen.getHeight() - getHeight()) / 2; 
		setPosition(x, y);
	}
	
	public void onTap() {
		Gdx.app.log(getName(), "Tapped.");
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		sprite.setRegion(isSelected ? selectedRegion : normalRegion);
		super.draw(batch, parentAlpha);
	}

}
