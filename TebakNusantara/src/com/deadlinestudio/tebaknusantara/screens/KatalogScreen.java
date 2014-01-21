package com.deadlinestudio.tebaknusantara.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.katalog.Budaya;
import com.deadlinestudio.tebaknusantara.katalog.BudayaDrawer;
import com.deadlinestudio.tebaknusantara.screens.LoadingScreen.DESTINATION;
import com.deadlinestudio.tebaknusantara.services.MusicManager.TebakNusantaraMusic;
import com.deadlinestudio.tebaknusantara.services.SoundManager.TebakNusantaraSound;
import com.deadlinestudio.tebaknusantara.util.BGPattern;
import com.deadlinestudio.tebaknusantara.util.BGPattern.COLOR;
import com.deadlinestudio.tebaknusantara.util.Button2;

public class KatalogScreen extends AbstractScreen implements GestureListener{

	private enum TIPE_BUDAYA { MAKANAN, TARIAN, LAGU };
	
	private final int N_BUDAYA = 2;
	
	private BGPattern background;
	private List<Budaya> budayaList[];
	
	private Budaya activeList[];
	
	private int motion;
	
	private TIPE_BUDAYA active_id;
	
	private int currentBudaya;
	public float offsetX;
	private TextBounds textbound;
	
	private final float motionSpeed;
	
	private TextureAtlas atlas;
	private TextureAtlas makananAtlas;
	private TextureAtlas tarianAtlas;
	private FileHandle makananFile;
	private FileHandle tarianFile;
	
	public Image bar;
	
	public KatalogScreen(final TebakNusantara game) {
		super(game);
		// TODO Auto-generated constructor stub		
		
		game.getMusicManager().play(TebakNusantaraMusic.GAMELAN);
		
		atlas = game.getAssetManager().get("katalogscreen/katalogscreen.txt", TextureAtlas.class);
		makananAtlas = game.getAssetManager().get("katalogscreen/makanan.txt", TextureAtlas.class);
		tarianAtlas = game.getAssetManager().get("katalogscreen/tarian.txt", TextureAtlas.class);
				
		makananFile = Gdx.files.internal("katalogscreen/makananlist.txt");
		tarianFile = Gdx.files.internal("katalogscreen/tarianlist.txt");
		//makananFile = game.getAssetManager().get("katalogscreen/makananlist.txt", FileHandle.class);
		//tarianFile = game.getAssetManager().get("katalogscreen/tarianlist.txt", FileHandle.class);
		
		loadBudaya();
		
		background = new BGPattern();
		background.setColor(COLOR.BLUE);
				 		
		motion = 0;
		offsetX = 0.0f;
		motionSpeed = 2.00f * SCREEN_WIDTH;
		currentBudaya = 0;
		
		active_id = TIPE_BUDAYA.MAKANAN;
		
		activeList = new Budaya[7];
		setActiveBudaya(TIPE_BUDAYA.MAKANAN);
				
		// font setting
		setFontSize(72);
		//font.setScale(RATIO);
		
		Sprite spr = new Sprite(atlas.findRegion("BG2"));
		//spr.setScale(SCREEN_WIDTH / spr.getWidth());
		spr.setBounds(0, 0, SCREEN_WIDTH, spr.getHeight() * SCREEN_WIDTH / spr.getWidth());
		Image backmap = new Image(new SpriteDrawable(spr));
		backmap.setX((SCREEN_WIDTH-spr.getWidth())/2);
		backmap.setY(0.0f);
		
		spr = new Sprite(atlas.findRegion("BAR"));
		//spr.setScale(SCREEN_WIDTH / spr.getWidth());
		spr.setBounds(0, 0, SCREEN_WIDTH, spr.getHeight() * SCREEN_WIDTH / spr.getWidth());
		bar = new Image(new SpriteDrawable(spr));
		bar.setX((SCREEN_WIDTH-spr.getWidth())/2);
		bar.setY(SCREEN_HEIGHT-spr.getHeight());
		
		Gdx.app.log("BG2", backmap.getWidth() + ", " +backmap.getHeight());
		
		ButtonStyle btn_style = new ButtonStyle();
		btn_style.up = new TextureRegionDrawable(atlas.findRegion("makanan"));
		btn_style.down = btn_style.up;
		
		final Button2 makanan = new Button2(btn_style);
		makanan.setX((SCREEN_WIDTH/2 - makanan.getWidth())/2);
		makanan.setY((SCREEN_HEIGHT * 3/2 - makanan.getHeight())/2);
		
		makanan.setFloating(true);
				
		
		btn_style = new ButtonStyle();
		btn_style.up = new TextureRegionDrawable(atlas.findRegion("tarian"));
		btn_style.down = btn_style.up;
		
		final Button2 tarian = new Button2(btn_style);
		tarian.setX((SCREEN_WIDTH/2 - tarian.getWidth())/2 +SCREEN_WIDTH / 2 );
		tarian.setY((SCREEN_HEIGHT * 3/2 - tarian.getHeight())/2);
		tarian.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				setActiveBudaya(TIPE_BUDAYA.TARIAN);
				
				makanan.setFloating(false);
				tarian.setFloating(true);
			}
		});
		
		makanan.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				setActiveBudaya(TIPE_BUDAYA.MAKANAN);
				
				makanan.setFloating(true);
				tarian.setFloating(false);
			}
		});
		
		btn_style = new ButtonStyle();
		btn_style.up = new TextureRegionDrawable(atlas.findRegion("panahkanan"));
		btn_style.down = btn_style.up;
		
		Button2 kanan = new Button2(btn_style);
		kanan.setX(SCREEN_WIDTH - kanan.getWidth()- 50.0f * RATIO);
		kanan.setY((SCREEN_HEIGHT - kanan.getHeight())/2);
		
		kanan.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				if (currentBudaya+1 < budayaList[getId()].size())
					motion = 1;				
			}
		});
		
		btn_style = new ButtonStyle();
		btn_style.up = new TextureRegionDrawable(atlas.findRegion("panahkiri"));
		btn_style.down = btn_style.up;
		
		Button2 kiri = new Button2(btn_style);
		kiri.setX(50.0f * RATIO);
		kiri.setY((SCREEN_HEIGHT - kiri.getHeight())/2);
		
		kiri.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				game.getSoundManager().play(TebakNusantaraSound.TAP);
			
				if (currentBudaya > 0)
					motion = -1;
			}
		});
		
		makanan.setFloatingLength(7.5f);
		tarian.setFloatingLength(7.5f);
		
		stage.addActor(background);
		stage.addActor(backmap);
		stage.addActor(bar);
		stage.addActor(new BudayaDrawer(activeList, this));
		stage.addActor(makanan);
		stage.addActor(tarian);
		stage.addActor(kanan);
		stage.addActor(kiri);
		
		InputMultiplexer multi_input = new InputMultiplexer(stage, new GestureDetector(this));
		
		Gdx.input.setInputProcessor(multi_input);
	}
	
	private void setActiveBudaya(TIPE_BUDAYA tipe)
	{
		active_id = tipe;
		
		activeList[0] = null;
		activeList[1] = null;
		activeList[2] = null;
		
		int id;
		switch (tipe)
		{
		case MAKANAN : id = 0; break;
		case TARIAN : id = 1; break;
		case LAGU : id = 2; break;  
		default : id = 0;
		} 
		
		currentBudaya = 0;
		for (int i = 0; i < 4; ++i)
		{
			if (i >= budayaList[id].size()) break;
			activeList[i+3] = budayaList[id].get(i);
		}
	}
	
	private void loadBudaya()
	{
		Scanner sc;
		
		budayaList = new List[N_BUDAYA];

		for (int i = 0; i < N_BUDAYA; ++i)
			budayaList[i] = new ArrayList<Budaya>();
		
		/* file makanan */
		sc = new Scanner(makananFile.readString());
		while (sc.hasNext())
		{
			String nama = sc.next();
			String asal = sc.next();
			
			String nama1 = "";
			String asal1 = "";
			
			String s = "";
			for (int i = 0; i < nama.length(); ++i)
			{
				if (nama.charAt(i) >= 'A' && nama.charAt(i) <= 'Z')
					s += (char)((int)nama.charAt(i) - (int)'A' + (int)'a');
				else
				if (nama.charAt(i) >= 'a' && nama.charAt(i) <= 'z')
					s += nama.charAt(i);
				
				if (nama.charAt(i) == '_')
					nama1 += " ";
				else
					nama1 += nama.charAt(i);
			}
			
			for (int i = 0; i < asal.length(); ++i)
			{
				if (asal.charAt(i) == '_')
					asal1 += " ";
				else
					asal1 += asal.charAt(i);
			}
			
			Gdx.app.log("MASUKIN BUDAYA", "-" + nama + ", " + asal +", " + s + "-");
			
			Sprite spr = new Sprite(makananAtlas.findRegion(s));
			spr.setScale(RATIO);
			budayaList[0].add(new Budaya(nama1, asal1, spr));
		}
		
		/* Tarian */
		sc = new Scanner(tarianFile.readString());
		while (sc.hasNext())
		{
			String nama = sc.next();
			String asal = sc.next();
			
			String nama1 = "";
			String asal1 = "";
			
			String s = "";
			for (int i = 0; i < nama.length(); ++i)
			{
				if (nama.charAt(i) >= 'A' && nama.charAt(i) <= 'Z')
					s += (char)((int)nama.charAt(i) - (int)'A' + (int)'a');
				else
				if (nama.charAt(i) >= 'a' && nama.charAt(i) <= 'z')
					s += nama.charAt(i);
				
				if (nama.charAt(i) == '_')
					nama1 += " ";
				else
					nama1 += nama.charAt(i);
			}
			
			for (int i = 0; i < asal.length(); ++i)
			{
				if (asal.charAt(i) == '_')
					asal1 += " ";
				else
					asal1 += asal.charAt(i);
			}			
			Sprite spr = new Sprite(tarianAtlas.findRegion(s));
			
			spr.setScale(RATIO);
			budayaList[1].add(new Budaya(nama1, asal1, spr));
		}
	}
	
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		
		motionUpdate(delta);
		
		stage.act();
		
		stage.draw();
		
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		
		/*
		for (List<Budaya> blist : budayaList)
			for (Budaya budaya : blist) {
				budaya.dispose();
			}
			*/
	}
	
	private int getId()
	{
		switch (active_id)
		{
		case MAKANAN : return 0;
		case TARIAN : return 1;
		}
		return 0;
	}
	
	private void motionUpdate(float delta)
	{
		if (motion != 0)
		{
			offsetX -= motion * delta * motionSpeed;
			if (Math.abs(offsetX) >= SCREEN_WIDTH * Math.abs(motion) / 2)
			{
				offsetX += motion * SCREEN_WIDTH / 2;
				
				
				
				if (currentBudaya+motion >= 0 && currentBudaya+motion < budayaList[getId()].size())
				{
					currentBudaya += motion;
					
					for (int i = 0; i < 7; i++)
					{
						int j = currentBudaya - 3 + i;
						int id = getId();
						
						if (j < 0 || j >= budayaList[id].size())
							activeList[i] = null;
						else
							activeList[i] = budayaList[id].get(j);
					}
					
					game.getSoundManager().play(TebakNusantaraSound.TAP);
				}
				motion = 0;
			}
		}
		else
		if (!Gdx.input.isTouched())
		{
			if (Math.abs(offsetX) > SCREEN_WIDTH/4)
			{
				if (offsetX > 0.0f){
					if (currentBudaya > 0)
						motion = -1;
				}
				else {
					if (currentBudaya+1 < budayaList[0].size())
						motion = 1;
				}
			}
			
			if (offsetX < 0.0f)
			{
				offsetX += delta * motionSpeed;
				if (offsetX > 0.0f) offsetX = 0.0f;
				Gdx.app.log("NOW", "motion : " + motion + ", index : " + currentBudaya + "offset : " +offsetX);
			}
			if (offsetX > 0.0f)
			{
				offsetX -= delta * motionSpeed;
				if (offsetX < 0.0f) offsetX = 0.0f;
				Gdx.app.log("NOW", "motion : " + motion + ", index : " + currentBudaya + "offset : " +offsetX);
			}		
		}	
	}
	
	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		game.setScreen(game.getLoadingScreen(DESTINATION.MENU, DESTINATION.KATALOG));
	}

	@Override
	public boolean fling(float vx, float vy, int btn) {
		// TODO Auto-generated method stub
		Gdx.app.log("FLING", "vx : " + vx + ", motion = " + motion);
		
		if (motion != 0) return false;		
		
		if (Math.abs(vx) > Math.abs(vy))
		{
			if (vx < 0)
			{
				if (currentBudaya+1 < budayaList[getId()].size())
					motion = 1;
			}
			else
			{
				if (currentBudaya > 0)
					motion = -1;
			}
		}
		return true;
	}

	@Override
	public boolean longPress(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		offsetX += deltaX;
		Gdx.app.log("PAN", "deltaX : " + deltaX);
		
		return true;
	}

	@Override
	public boolean pinch(Vector2 arg0, Vector2 arg1, Vector2 arg2, Vector2 arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void semidispose() {
		// TODO Auto-generated method stub
		background.dispose();
	}

}
