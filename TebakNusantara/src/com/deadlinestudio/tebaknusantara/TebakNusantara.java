package com.deadlinestudio.tebaknusantara;


import java.util.Stack;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.deadlinestudio.tebaknusantara.screens.LoadingScreen;
import com.deadlinestudio.tebaknusantara.screens.LoadingScreen.DESTINATION;
import com.deadlinestudio.tebaknusantara.screens.KatalogScreen;
import com.deadlinestudio.tebaknusantara.screens.AbstractScreen;
//import com.badlogic.gdx.graphics.FPSLogger;
import com.deadlinestudio.tebaknusantara.screens.MenuScreen;
import com.deadlinestudio.tebaknusantara.screens.SplashScreen;
import com.deadlinestudio.tebaknusantara.screens.StartScreen;
import com.deadlinestudio.tebaknusantara.screens.TebakJamakScreen;
import com.deadlinestudio.tebaknusantara.services.MusicManager;
import com.deadlinestudio.tebaknusantara.services.PreferencesManager;
import com.deadlinestudio.tebaknusantara.services.ScoreboardManager;
import com.deadlinestudio.tebaknusantara.services.SoundManager;
import com.deadlinestudio.tebaknusantara.tebakpeta.TouchmapScreen;
import com.deadlinestudio.tebaknusantara.util.Player;


public class TebakNusantara extends Game
{
	//debug string LOG
	public static final String LOG = "TebakNusantara";
		
	//resize ratio
	public static float RATIO;
	
	//screen attributes
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	
	//services
	private PreferencesManager preferencesManager;
    private MusicManager musicManager;
    private SoundManager soundManager;
    private AssetManager assetManager;    
    private ScoreboardManager scoreboardManager;
    
    private SpriteBatch batch;
    
    //loading texture atlas
    TextureAtlas pilihanAtlas;
	// a libgdx helper class that logs the current FPS each second
    //private FPSLogger fpsLogger;

    public TebakNusantara()
    {
    }
    
    //service methods
    public PreferencesManager getPreferencesManager()
    {
        return preferencesManager;
    }

    public MusicManager getMusicManager()
    {
        return musicManager;
    }

    public SoundManager getSoundManager()
    {
        return soundManager;
    }
    
    public AssetManager getAssetManager()
    {
    	return assetManager;
    }
    
    //screen methods
    public MenuScreen getMenuScreen()
    {
    	return new MenuScreen(this);
    }
    
    public SplashScreen getSplashScreen()
    {
    	return new SplashScreen(this);
    }
    
    public StartScreen getStartScreen()
    {
    	return new StartScreen(this);
    }
        
    
    public TebakJamakScreen getTebakJamakScreen()
    {
    	return new TebakJamakScreen(this);    	
    }
    
    public TouchmapScreen getTouchMapScreen()
    {
    	return new TouchmapScreen(this);    	
    }
 
    public LoadingScreen getLoadingScreen(DESTINATION dest, DESTINATION src)
    {
    	return new LoadingScreen(this, dest, src);
    }
    
    public KatalogScreen getKatalogScreen()
    {
    	return new KatalogScreen(this);
    }
	
	@Override
	public void create() {
		Gdx.app.log(LOG, "Game.create()");

		//fpsLogger = new FPSLogger();
		
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		SCREEN_HEIGHT = Gdx.graphics.getHeight();
		
		assetManager = new AssetManager();
		
		//create the preferences service
		preferencesManager = new PreferencesManager();
		
		//create the music manager service
		musicManager = new MusicManager();
		musicManager.setVolume(preferencesManager.getVolume());
		musicManager.setEnabled(preferencesManager.isMusicEnabled());
		
		//create the sound manager service
		soundManager = new SoundManager();
		soundManager.setVolume(preferencesManager.getVolume());
		soundManager.setEnabled(preferencesManager.isSoundEnabled());
		
		//initialize scores
		scoreboardManager = new ScoreboardManager(preferencesManager.getScoreboard());
		
		pilihanAtlas = new TextureAtlas(Gdx.files.internal("pilihan/pilihan.txt"));
		
		batch = new SpriteBatch();
		
		Gdx.input.setCatchBackKey(true);
	}
		
	public ScoreboardManager getScoreboardManager()
	{
		return scoreboardManager;
	}
		
	
	public TextureAtlas getPilihanAtlas()
	{
		return pilihanAtlas;
	}
	
	@Override
	public void setScreen(Screen screen) {
		System.gc();
		
		AbstractScreen scr = (AbstractScreen)getScreen();
				
		//if (scr != null)
//			scr.semidispose();
		
		super.setScreen(screen);
		/*
		if (scr != null)
			scr.dispose();
			*/
		System.gc();
	}
	
	@Override
	public void dispose() {
		Gdx.app.log(LOG, "Game.dispose()");
		
		//dispose objects
		musicManager.dispose();
		soundManager.dispose();
		
		pilihanAtlas.dispose();
				
		batch.dispose();
	}
	
	public SpriteBatch getSpriteBatch()
	{
		return batch;
	}

	@Override
	public void pause() {
		Gdx.app.log(LOG, "Game.pause()");
		
		//pause
	}

	@Override
	public void render() {
		super.render();
		//Gdx.app.log(LOG, "Game.render()");
		
		//fpsLogger.log();
		
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
		
		
		if (getScreen() == null)
		{
			SCREEN_WIDTH = arg0;
	    	SCREEN_HEIGHT = arg1;
	    	
	    	float w = 800, h = 550;
	    	if (SCREEN_WIDTH * h < SCREEN_HEIGHT * w)
	    		RATIO = (float)SCREEN_WIDTH / (float)w;
	    	else
	    		RATIO = (float)SCREEN_HEIGHT / (float)h;
	    	
	    	Gdx.app.log("RATIO", ""+RATIO);
			setScreen(getLoadingScreen(DESTINATION.START,DESTINATION.NO));
	    	//setScreen(getStartScreen());
		}
		else
			this.getScreen().resize(arg0, arg1);
	}

	public void clearAssetManager()
	{
		System.gc();
		assetManager.clear();
		System.gc();
		//assetManager.dispose();
		//assetManager = new AssetManager();
	}
	
	
	@Override
	public void resume() {
		Gdx.app.log("MyLibGDXGame", "Game.resume()");
	}
}
