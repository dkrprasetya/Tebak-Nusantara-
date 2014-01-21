package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class GameCroc extends TouchmapActor {

	private int FRAME_WIDTH = 169;
	private int FRAME_HEIGHT = 96;
	private float FRAME_TIME = 0.1f;

	private TextureRegion[] waitingRegions;
	private Animation eating;
	private float stateTime = 0;
	
	private boolean isWaiting = true;
	private int waitingFrame = 0;
	
	public GameCroc(TouchmapScreen screen) {
		super(screen);
		
		TextureRegion waitingMaster = getScreen().getAtlas().findRegion("croc_waiting");
		if (waitingMaster == null)
			Gdx.app.log("tes", "crocnya null");
		waitingRegions = new TextureRegion[waitingMaster.getRegionWidth() / FRAME_WIDTH];
		for (int i = 0; i < waitingRegions.length; i++) {
			waitingRegions[i] = new TextureRegion(waitingMaster, i * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT);
		}
		
		TextureRegion eatingMaster = getScreen().getAtlas().findRegion("croc_eating");
		TextureRegion[] eatingRegions = new TextureRegion[eatingMaster.getRegionWidth() / FRAME_WIDTH];
		for (int i = 0; i < eatingRegions.length; i++) {
			eatingRegions[i] = new TextureRegion(eatingMaster, i * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT);
		}
		eating = new Animation(FRAME_TIME, eatingRegions);
		eating.setPlayMode(Animation.LOOP);
		
		sprite = new Sprite();
		float scale = (getScreen().getHeight() / 4) / FRAME_HEIGHT;
		sprite.setBounds(0, 0, scale * FRAME_WIDTH, scale * FRAME_HEIGHT);
	}
	
	@Override
	public void act(float delta) {
		stateTime += delta;
		
		Rectangle conveyorBounds = getScreen().conveyor.getBounds();
		float conveyorMax = conveyorBounds.getWidth() - getScreen().runner.getBounds().getWidth();
		float runnerX = getScreen().runner.getX() - conveyorBounds.getX();
		float percentage = runnerX / conveyorMax < 0 ? 0 : (runnerX / conveyorMax > 1) ? 1 : (runnerX / conveyorMax);
		waitingFrame = (int)((1 - percentage) * (waitingRegions.length - 1));
		
		super.act(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		if (isWaiting) {
			sprite.setRegion(waitingRegions[waitingFrame]);
		} else {
			sprite.setRegion(eating.getKeyFrame(stateTime));
		}
		
		super.draw(batch, parentAlpha);
	}

}
