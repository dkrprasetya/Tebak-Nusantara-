package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class GameBar extends TouchmapActor {

	private BitmapFont font;
	
	private Sprite pauseButton;
	
	private String question = "I never asked to be squad leader.";
	private float score = 0;
	
	public GameBar(TouchmapScreen screen) {
		super(screen);
		
		sprite = new Sprite(getScreen().getAtlas().findRegion("bar"));
		pauseButton = new Sprite(getScreen().getAtlas().findRegion("bar_pause"));

		float margin = 0.025f * getScreen().getWidth();
		float scale = (getScreen().getWidth() - (2 * margin)) / sprite.getRegionWidth();
		
		float width = sprite.getRegionWidth() * scale;
		float height = sprite.getRegionHeight() * scale;
		float x = (getScreen().getWidth() - width) / 2;
		float y = getScreen().getHeight() - height - margin;
		sprite.setBounds(x, y, width, height);
		
		float pauseWidth = pauseButton.getRegionWidth() * scale;
		float pauseHeight = pauseButton.getRegionHeight() * scale;
		float pauseX = x + width - pauseWidth - (0.015f * width);
		float pauseY = y + (0.525f * height);
		pauseButton.setBounds(pauseX, pauseY, pauseWidth, pauseHeight);

		font = getScreen().getBarFont();
		font.setScale((height / 7) / (font.getAscent() - font.getDescent()));
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		pauseButton.draw(batch, parentAlpha);
		
		String questionText = question;
		TextBounds fontBounds1 = font.getBounds(questionText);
		float x1 = (getScreen().getWidth() - fontBounds1.width) / 2;
		float y1 = getY() + getHeight() - font.getCapHeight() - font.getDescent();
		font.draw(batch, questionText, x1, y1);

		String scoreText = Integer.toString((int)score);
		TextBounds fontBounds2 = font.getBounds(scoreText);
		float x2 = (getScreen().getWidth() - fontBounds2.width) / 2;
		float y2 = getY() + (getHeight() / 2) - font.getCapHeight() - font.getDescent();
		font.draw(batch, scoreText, x2, y2);
	}
	
	public Rectangle getPauseButtonBounds() {
		return pauseButton.getBoundingRectangle();
	}

	public void onPauseButtonTap() {
		getScreen().setStateToPause();
	}
	
	public float getScore() {
		return score;
	}
	
	public void setScore(float score) {
		this.score = score;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}

}
