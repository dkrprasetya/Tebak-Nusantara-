package com.deadlinestudio.tebaknusantara.tebakpeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.deadlinestudio.tebaknusantara.services.SoundManager.TebakNusantaraSound;

public class GameMap extends TouchmapActor {

	public static class MapData {
		String name = null;
		List<String> questions = null;
		float x;
		float y;
	}
	
	private List<MapData> mapDatabase = new ArrayList<GameMap.MapData>();
	private MapData currentData;

	private final float QUICKGUESS_MIN = 0.5f;
	private final float QUICKGUESS_MAX = 1.0f;
	private final float QUICKGUESS_STEP = 0.1f;
	private float stateTime = 0;
	private float lastGuess = 0;
	private float guessTimeout = QUICKGUESS_MAX;
	 
	private int comboCounter = 0;
	
	public GameMap(TouchmapScreen screen, FileHandle mapFile) {
		super(screen);
		
		sprite = new Sprite(getScreen().getAtlas().findRegion("map"));

		Rectangle barBounds = getScreen().bar.getBounds();
		float scale = (0.9f * barBounds.getWidth()) / sprite.getRegionWidth();
		float width = sprite.getRegionWidth() * scale;
		float height = sprite.getRegionHeight() * scale;
		float x = (getScreen().getWidth() - width) / 2;
		float y = barBounds.getY() - height + (0.67f * barBounds.getHeight());
		sprite.setBounds(x, y, width, height);
		
		loadMap(mapFile, scale, x, y, width, height);
		selectData(-1, -1);
	}
	
	private void loadMap(FileHandle file, float mapScale, float mapX, float mapY, float mapW, float mapH) {
		
		Scanner sc = new Scanner(file.readString());
		while (sc.hasNext()) {
			MapData newData = new MapData();
			newData.name = sc.next();
			
			float x = sc.nextInt();
			float y = sc.nextInt();
			x = mapX + (x * mapScale);
			y = mapY + (mapH - (y * mapScale));
			newData.x = x;
			newData.y = y;
			
			int qc = sc.nextInt();
			newData.questions = new ArrayList<String>(qc);
			for (int i = 0; i < qc; i++) {
				newData.questions.add(sc.next().replace('_', ' '));
			}
			mapDatabase.add(newData);
		}
		
		Gdx.app.log(getClass().getSimpleName(), String.format("%s loaded as map file, %d entries.", file.name(), mapDatabase.size()));
		for (MapData md : mapDatabase) {
			Gdx.app.log(getClass().getSimpleName(), String.format("%s (%f, %f):", md.name, md.x, md.y));
			for (String q : md.questions) {
				Gdx.app.log(getClass().getSimpleName(), String.format(md.questions.indexOf(q) + ". " + q));
			}
		}
	}
	
	public void selectData(int mapIndex, int questionIndex) {
		if (mapIndex == -1) {
			currentData = mapDatabase.get((int)(Math.random() * mapDatabase.size()));
		} else {
			currentData = mapDatabase.get(mapIndex);
		}
		if (questionIndex == -1) {
			getScreen().bar.setQuestion(currentData.questions.get(0));
		} else {
			getScreen().bar.setQuestion(currentData.questions.get(questionIndex));
		}
		Gdx.app.log(getClass().getSimpleName(), String.format("Now selected: %s", currentData.name));
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
	}

	public List<MapData> getDatabase() {

		return mapDatabase;
	}

	public MapData getCurrentData() {
		return currentData;
	}
	
	public void onMapTap(float x, float y) {
		
		final float DISTANCE = 40.0f * getScaleX();
		
		float dx = currentData.x - x;
		float dy = currentData.y - y;
		float distance = (float)Math.sqrt((dx * dx) + (dy * dy));
		GameRunner2 runner = getScreen().runner;
		float runnerX = runner.getX();
		setVisible(true);

		if (distance < DISTANCE) {

			runnerX += getScreen().runner.getScaleX() * 10;
			comboCounter++;
			
			if (stateTime - lastGuess < guessTimeout) {
				float newTimeout = guessTimeout - QUICKGUESS_STEP;
				guessTimeout = newTimeout < QUICKGUESS_MIN ? QUICKGUESS_MIN : newTimeout;
				Gdx.app.log(getClass().getSimpleName(), String.format("Quick answer! Timeout: %f.", guessTimeout));
			} else {
				guessTimeout = QUICKGUESS_MAX;
				Gdx.app.log(getClass().getSimpleName(), String.format("Slow answer! Timeout: %f.", guessTimeout));
			}
			
			getScreen().getGame().getSoundManager().play(TebakNusantaraSound.HIT);
			
	
			lastGuess = stateTime;

			getScreen().bar.setScore(getScreen().bar.getScore() + (int) (guessTimeout * 50) + (100 * (comboCounter > 10 ? 10 : comboCounter)));
			Gdx.app.log(getClass().getSimpleName(), String.format("Correct! Combo: %d.", comboCounter));
			
			getScreen().showBola(true, x, y);
		} else {

			comboCounter = 0;

			getScreen().getGame().getSoundManager().play(TebakNusantaraSound.MISS);
			Gdx.app.log(getClass().getSimpleName(), "Wrong!");
			
			getScreen().showBola(false, x, y);
			getScreen().showBola(true, currentData.x, currentData.y);
		}
		runner.setX(runnerX);
		
		selectData(-1, -1);
	}


	/*
	//-- debug to mark the map --
	@Override
	public void draw(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		for (MapData md : mapDatabase) {
			batch.draw(sprite, md.x, md.y, 10, 10);
		}
	}
	*/
}
