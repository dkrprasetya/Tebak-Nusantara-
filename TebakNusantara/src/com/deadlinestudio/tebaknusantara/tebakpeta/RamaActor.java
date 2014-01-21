package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.deadlinestudio.tebaknusantara.tebakpeta.RamaModel.RamaState;

public class RamaActor extends Actor {
	
	RamaModel model;
	private RamaState state;
	private float stateTime = 0.0f;
	
	public RamaActor(AssetManager assets, RamaState state) {
		model = new RamaModel(assets);
		this.state = state;
		TextureRegion region = model.getTextureRegion(state, stateTime);
		setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
	}
	
	public RamaActor(AssetManager assets, RamaState state, float frameTime) {
		model = new RamaModel(assets, frameTime);
		this.state = state;
		TextureRegion region = model.getTextureRegion(state, stateTime);
		setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(model.getTextureRegion(state, stateTime), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
}
