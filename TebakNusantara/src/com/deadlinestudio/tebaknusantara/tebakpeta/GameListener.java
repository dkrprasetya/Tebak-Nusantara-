package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.deadlinestudio.tebaknusantara.tebakpeta.TouchmapScreen.TouchmapState;

public class GameListener implements GestureListener {
	
	private TouchmapScreen screen;
	
	public GameListener(TouchmapScreen screen) {
		this.screen = screen;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (screen.getState() == TouchmapState.PLAY) {
			Gdx.app.log(getClass().getSimpleName(), String.format("Tapped %d times at (%.3f, %.3f)", count, x, y));

			Vector2 stageTap = screen.gameStage.screenToStageCoordinates(new Vector2(x, y));
			
			if (screen.bar.getPauseButtonBounds().contains(stageTap.x, stageTap.y)) {
				screen.bar.onPauseButtonTap();
			} else if (screen.map.getBounds().contains(stageTap.x, stageTap.y)) {
				screen.map.onMapTap(stageTap.x, stageTap.y);
			}
			
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) { return false; }
	public boolean longPress(float x, float y) { return false; }
	public boolean fling(float vx, float vy, int btn) { return false; }
	public boolean pan(float x, float y, float dx, float dy) { return false; }
	public boolean zoom(float id, float d) { return false; }
	public boolean pinch(Vector2 i1, Vector2 i2, Vector2 p1, Vector2 p2) { return false; }

}
