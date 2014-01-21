package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.deadlinestudio.tebaknusantara.tebakpeta.RamaModel.RamaState;

public class GameRunner2 extends TouchmapActor {

	private RamaModel model;
	private RamaState state = RamaState.SCARED;
	private float stateTime = 0.0f;
	
	public GameRunner2(TouchmapScreen screen, RamaModel model) {
		super(screen);
		this.model = model;
		
		sprite = new Sprite(model.getTextureRegion(state, 0));
		Rectangle conveyorBounds = getScreen().conveyor.getBounds();
		setOrigin(0, 0);
		setScale((getScreen().getHeight() / 4) / (getHeight() * getScaleY()));
		setX(conveyorBounds.getX() + 0.9f * conveyorBounds.getWidth());
		setY(conveyorBounds.getY() + conveyorBounds.getHeight());
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
		
		setX(getX() - delta * 2 * (getScaleX() * (stateTime / 2)));
		if (getScreen().getState() != TouchmapScreen.TouchmapState.GAMEOVER) {
			getScreen().bar.setScore(getScreen().bar.getScore() + delta * 10);
		}

		Rectangle conveyorBounds = getScreen().conveyor.getBounds();
		float maxX = conveyorBounds.getX() + conveyorBounds.getWidth() - getWidth() * getScaleX();
		if (getX() > maxX) {
			setX(maxX);
		}
		
		float left = conveyorBounds.x;
		//float right = conveyorBounds.x + conveyorBounds.width;
		float percentage = (getX() - left) / conveyorBounds.width;
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> " + left + " " + right + " " + conveyorBounds.width + " " + percentage);
		if (percentage > 0.75f) {
			state = RamaState.HAPPY;
		} else if (percentage > 0.5f) {
			state = RamaState.CONFUSED;
		} else if (percentage > 0.25f) {
			state = RamaState.SCARED;
		} else {
			state = RamaState.PANIC;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		sprite.setRegion(model.getTextureRegion(state, stateTime));
		super.draw(batch, parentAlpha);
	}

	public boolean isGameOver() {
		Rectangle conveyorBounds = getScreen().conveyor.getBounds();
		return getX() < conveyorBounds.getX();
	}

}
