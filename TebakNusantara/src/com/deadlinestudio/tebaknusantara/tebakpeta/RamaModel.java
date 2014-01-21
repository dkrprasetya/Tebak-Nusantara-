package com.deadlinestudio.tebaknusantara.tebakpeta;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RamaModel {
	
	public enum RamaState {
		HAPPY,
		SCARED,
		CONFUSED,
		PANIC;
		
		private RamaState() { }
		
		public String getTextureName(int count) {
			return String.format("runner_%s%d", name().toLowerCase(), count + 1);
		}
		
		private float getFrameDuration() {
			switch (this) {
			case CONFUSED: return 0.08f;
			case HAPPY: return 0.1f;
			case PANIC: return 0.06f;
			case SCARED: return 0.07f;
			default: return 0.1f;
			}
		}
	}

	public static final String ATLAS = "tebakpeta/sprites.atlas";
	public static final int FRAME_COUNT = 10;
	
	private static Map<RamaState, Animation> animations = null;
	
	public RamaModel(AssetManager assets) {
		if (animations == null) {
			animations = new EnumMap<RamaState, Animation>(RamaState.class);
			
			if (!assets.isLoaded(ATLAS)) {
				assets.load(ATLAS, TextureAtlas.class);
				assets.finishLoading();
			}
			TextureAtlas atlas = assets.get(ATLAS);
			
			for (RamaState s : RamaState.values()) {
				TextureRegion[] regions = new TextureRegion[FRAME_COUNT];
				for (int k = 0; k < regions.length; k++) {
					regions[k] = atlas.findRegion(s.getTextureName(k));
					System.out.println(s.getTextureName(k) + " " + regions[k]);
				}
				Animation animation = new Animation(s.getFrameDuration(), regions);
				animation.setPlayMode(Animation.LOOP);
				animations.put(s, animation);
			}
		}
	}
	
	public RamaModel(AssetManager assets, float frameTime) {
		if (animations == null) {
			animations = new EnumMap<RamaState, Animation>(RamaState.class);
			
			if (!assets.isLoaded(ATLAS)) {
				assets.load(ATLAS, TextureAtlas.class);
				assets.finishLoading();
			}
			TextureAtlas atlas = assets.get(ATLAS);
			
			for (RamaState s : RamaState.values()) {
				TextureRegion[] regions = new TextureRegion[FRAME_COUNT];
				for (int k = 0; k < regions.length; k++) {
					regions[k] = atlas.findRegion(s.getTextureName(k));
					System.out.println(s.getTextureName(k) + " " + regions[k]);
				}
				Animation animation = new Animation(frameTime, regions);
				animation.setPlayMode(Animation.LOOP);
				animations.put(s, animation);
			}
		}
	}

	public TextureRegion getTextureRegion(RamaState state, float stateTime) {
		return animations.get(state).getKeyFrame(stateTime);
	}

}
