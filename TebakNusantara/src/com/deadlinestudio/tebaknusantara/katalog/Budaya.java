package com.deadlinestudio.tebaknusantara.katalog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deadlinestudio.tebaknusantara.TebakNusantara;

public class Budaya {
	private Sprite spr;
	private SpriteDrawable img;
	private String nama;
	private String asal;
	
	public Budaya(String nama, String asal, Sprite spr)
	{
		//img = new Texture(Gdx.files.internal(nama + ".png"));
		//region = new Sprite(img,0,0,w,h);
		
		spr.setScale(TebakNusantara.RATIO);
		img = new SpriteDrawable(spr);
		
		
		//region.setScale(TebakNusantara.RATIO);
		this.spr = spr;
		this.nama = nama;
		this.asal = asal;
		this.img = img;
	}
		
	public String getNama()
	{
		return nama;
	}
	
	public String getAsal()
	{
		return asal;
	}
	
	public float getWidth()
	{
		return spr.getWidth();
	}
	
	public float getHeight()
	{
		return spr.getHeight();
	}
	
	public Drawable getDrawable()
	{
		return img;
	}
}
