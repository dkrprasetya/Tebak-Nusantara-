package com.deadlinestudio.tebaknusantara.util;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.deadlinestudio.tebaknusantara.TebakNusantara;

public class BGPattern extends Actor implements Disposable{
	
	public enum COLOR {YELLOW, RED, GREEN, BLUE };
	
	private final static int width = 200;
	private final static int height = 80;
	
	private int scale_w;
	private int scale_h;
	
	private final float speed;
		
	private List<Point> bg_pos;
    private Texture bg_img;
        
    private Texture bgTexture;
    private TextureRegion bgRegion;
    
    private Color currentColor;
    
    public BGPattern()
    {
    	bg_img = new Texture(Gdx.files.internal("BG/motif.png"));
                            
    	scale_w = (int)(width * TebakNusantara.SCREEN_WIDTH / 800.0f  + 0.5f);
    	scale_h = (int)(height * TebakNusantara.SCREEN_HEIGHT / 600.0f  +0.5f);
    	
        bg_pos = new ArrayList<Point>();
        for (int i = 0; i < TebakNusantara.SCREEN_WIDTH * 2; i += scale_w)
        	for (int j = 0; j < TebakNusantara.SCREEN_HEIGHT * 2; j += scale_h)
        	{
        		bg_pos.add(new Point(i, TebakNusantara.SCREEN_HEIGHT-scale_h-j));
        	}
        
        speed = 40.00f * TebakNusantara.RATIO;
    }
    
    @Override
    public void act(float delta)
    {
    	for (int i = bg_pos.size()-1; i >= 0; --i) {
        	Point p = bg_pos.get(i);
        	
        	p.x -= speed * delta;
        	p.y += speed * delta;
			
			//Gdx.app.log("pos", p.x + ", " + p.y);
			
			
			if (p.x+scale_w< 0)
			{
				bg_pos.remove(i);
				bg_pos.add(new Point(p.x + 2 * TebakNusantara.SCREEN_WIDTH, p.y));
				continue;
			}
			
			if (p.y > TebakNusantara.SCREEN_HEIGHT)
			{
				bg_pos.remove(i);
				bg_pos.add(new Point(p.x, p.y - 2 * TebakNusantara.SCREEN_HEIGHT));
				continue;
			}
		}
    }
    
    public void setColor(COLOR color)
    {
    	if (bgTexture != null) bgTexture.dispose();
    	bgTexture = new Texture(Gdx.files.internal("BG/whiteback.png"));
    	switch (color)
    	{
    	case YELLOW : currentColor = new Color(1f, 0.788f, 0.209f, 1f); break;
    	case GREEN : currentColor = new Color(0.78f, 0.792f,0.204f,1f ); break;
    	case RED : currentColor = new Color(0.827f, 0.067f,0.204f,1f ); break;
    	case BLUE : currentColor = new Color(0.302f, 0.659f, 0.871f,1f); break;
    	}
    	
    	bgTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
    }
        
    @Override
    public void draw(SpriteBatch batch, float parentAlpha)
    {
	    Color c = batch.getColor();
	    
	    batch.setColor(currentColor.r, currentColor.g, currentColor.b, parentAlpha);
	    batch.draw(bgTexture, 0, 0, TebakNusantara.SCREEN_WIDTH, TebakNusantara.SCREEN_HEIGHT);
	    
	    batch.setColor(c.r, c.g, c.b, parentAlpha);
	    TextureRegion reg = new TextureRegion(bg_img, 0, 0, width, height);
    	for (Point p : bg_pos) {    		
    		if (p.x + scale_w > 0.00 && p.x < TebakNusantara.SCREEN_WIDTH && p.y+scale_h > 0.00 && p.y < TebakNusantara.SCREEN_HEIGHT)
    			batch.draw(reg, p.x, p.y, scale_w, scale_h);
		}
    }
    
    public void dispose()
    {
    	bg_pos.clear();
    	bg_img.dispose();
    	bgTexture.dispose();
    }
}
