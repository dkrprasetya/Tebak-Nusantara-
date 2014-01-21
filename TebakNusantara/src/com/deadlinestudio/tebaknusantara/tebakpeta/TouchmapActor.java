package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TouchmapActor extends Actor {
	
	private TouchmapScreen screen;
	protected Sprite sprite;
	
	
	public TouchmapActor(TouchmapScreen screen) {
		this.screen = screen;
		setName(getClass().getSimpleName());
		Gdx.app.log(getName(), "Created.");

	}
	
	public TouchmapActor(TouchmapScreen screen, String name) {
		this.screen = screen;
		setName(name);
		Gdx.app.log(getName(), "Created.");
	}
	
	public TouchmapScreen getScreen() {
		return screen;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		sprite.draw(batch, parentAlpha);
	}
	
	public Rectangle getBounds() {
		return sprite.getBoundingRectangle();
	}
	
	public Color getColor() { return sprite.getColor(); }
	public float getRotation() { return sprite.getRotation(); }
	public float getOriginX() { return sprite.getOriginX(); }
	public float getOriginY() { return sprite.getOriginY(); }
	public float getScaleX() { return sprite.getScaleX(); }
	public float getScaleY() { return sprite.getScaleY(); }
	public float getWidth() { return sprite.getWidth(); }
	public float getHeight() { return sprite.getHeight(); }
	public float getX() { return sprite.getX(); }
	public float getY() { return sprite.getY(); }
	
	public void setColor(Color color) { sprite.setColor(color); }
	public void setColor(float r, float g, float b, float a) { sprite.setColor(r, g, b, a); }
	public void setRotation(float degrees) { sprite.setRotation(degrees); }
	public void setPosition(float x, float y) { sprite.setPosition(x, y); }
	public void setSize(float width, float height) { sprite.setSize(width, height); }
	public void setOrigin(float originX, float originY) { sprite.setOrigin(originX, originY); }
	public void setOriginX(float originX) { setOrigin(originX, getOriginY()); }
	public void setOriginY(float originY) { setOrigin(getOriginX(), originY); }
	public void setScale(float scale) { sprite.setScale(scale); }
	public void setScale(float scaleX, float scaleY) { sprite.setScale(scaleX, scaleY); }
	public void setWidth(float width) { setSize(getHeight(), width); }
	public void setHeight(float height) { setSize(height, getWidth()); }
	public void setX(float x) { sprite.setX(x); }
	public void setY(float y) { sprite.setY(y); }

}
