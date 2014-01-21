package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class GameRunner extends TouchmapActor {

	private int FRAME_WIDTH = 75;
	private int FRAME_HEIGHT = 115;
	private int FRAME_COUNT = 10;
	private float FRAME_TIME = 0.1f;
	
	private Animation normalAnimation;
	private float stateTime = 0;
	
	public GameRunner(TouchmapScreen screen) {
		super(screen);
		
		TextureRegion normalRegion = getScreen().getAtlas().findRegion("runner_normal");
		TextureRegion[] normalSplitted = new TextureRegion[FRAME_COUNT];
		for (int i = 0; i < FRAME_COUNT; i++) {
			normalSplitted[i] = new TextureRegion(normalRegion, i * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT);
		}
		normalAnimation = new Animation(FRAME_TIME, normalSplitted);
		normalAnimation.setPlayMode(Animation.LOOP);
		
		sprite = new Sprite(normalSplitted[0]);
		
		Rectangle conveyorBounds = getScreen().conveyor.getBounds();
		setOrigin(0, 0);
		setScale((getScreen().getHeight() / 4) / (getHeight() * getScaleY()));
		setX(getScreen().getWidth() / 2);
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
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		sprite.setRegion(normalAnimation.getKeyFrame(stateTime));
		super.draw(batch, parentAlpha);
	}

	public boolean isGameOver() {
		Rectangle conveyorBounds = getScreen().conveyor.getBounds();
		return getX() < conveyorBounds.getX();
	}

}
