package com.deadlinestudio.tebaknusantara.screens;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Json;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.screens.LoadingScreen.DESTINATION;
import com.deadlinestudio.tebaknusantara.services.MusicManager.TebakNusantaraMusic;
import com.deadlinestudio.tebaknusantara.services.SoundManager.TebakNusantaraSound;
import com.deadlinestudio.tebaknusantara.tebakbudaya.DialogNinePatch;
import com.deadlinestudio.tebaknusantara.tebakbudaya.Stopwatch;
import com.deadlinestudio.tebaknusantara.tebakbudaya.TebakJamakRenderer;
import com.deadlinestudio.tebaknusantara.tebakbudaya.TebakanJamak;
import com.deadlinestudio.tebaknusantara.util.CountdownDrawer;
import com.deadlinestudio.tebaknusantara.util.HighscoreDialog;
import com.deadlinestudio.tebaknusantara.util.Player;
import com.deadlinestudio.tebaknusantara.util.ScoreDialog;

//TODO
//prepare stage when showing setting dialog :checked
//change font when changing screen
//change font color when showing combo


public class TebakJamakScreen extends AbstractScreen {
	
	//CONFIGURATION
	
	//the time given to player to answer the problem set (in seconds)
	private int stopwatchTime = 30;
	
	//determine whether the stopwatch's format include centiseconds value or not
	private boolean stopwatchIncludeCenti = true;
	
	//the score given to player each time he/she answers correctly
	private int scorePerProblem = 50;
	
	//UPDATE 10 Juli 2013
	//added feature: bonus score if player answers correctly within the time specified
	
	private int bonusTime = 2;
	
	private int bonusScore = 100;
	

	private int comboTreshold = 3;
	//END OF CONFIGURATION. do not edit out of this region unless you know what you're doing
	
	//Stopwatches
	//Game timer
	private Stopwatch stopwatch;
		
	//Stopwatch to determine player's bonuses
	private Stopwatch bonusStopwatch;
	private int correctCounter = 0;
	
	// start counter
	private Stopwatch startStopwatch;
	private boolean starting = true;
	
	
	
	//Sounds
	//Sound hit = Gdx.audio.newSound(Gdx.files.internal("sound/correct.mp3"));
	//Sound miss = Gdx.audio.newSound(Gdx.files.internal("sound/wrong.mp3"));
	
	private static ArrayList<TebakanJamak> problemSet;
	private TebakJamakRenderer renderer;
	private int answer;
	private boolean play = true;
	private int score = 0;
	
	private boolean whistle = false;
		
	int lastInterval;
	
	//UIs
	//Buttons
	private TextButton firstChoice,secondChoice,thirdChoice,fourthChoice;
	private Button pauseButton;
	
	//Sprites
	private Sprite dim;
	private boolean paused = false;
		
	private Sprite startCounter;
	private float startCounterAlpha = 1f;
	
	//Dialogs
	private Dialog selectionDialog;
	
	private DialogNinePatch settingDialog;
	private boolean changingSetting = false;
	
	///////////// BUAT SCOREBOARD /////////////////
	HighscoreDialog highscoreDialog = null;
	ScoreDialog scoreDialog = null;
	Player player = null;
	//////////////////////////////////////////
	
	//float fontScaleX;
	//float fontScaleY;
	boolean end = false;
	float lastSoundSliderValue;
	float lastMusicSliderValue;
	boolean lastMuteCheckBoxValue;
	

	private Dialog confirmDialog;
	
	Drawable upDrawable;
	Drawable downDrawable; 
	Drawable wrongDrawable;
	
	TebakanJamak nextProb;
	private final int NEXTDELAY = 60;
	private int nextDelay = NEXTDELAY;
	private boolean changeProb = true;
	private Color buttonColor;
	
	private Sprite timeout;
	private float timeoutTime;
	private float timeoutRotation;
	private float wiggleTime;
	

	boolean timeupfinish = false;
	
	//END of Properties Definition

	//Constructor
	public TebakJamakScreen(TebakNusantara game) {
		super(game);
		//fontScaleX = font.getScaleX();
		//fontScaleY = font.getScaleY();
		//font.setScale((float) Gdx.graphics.getWidth()/800f,(float) Gdx.graphics.getHeight()/480f);

		problemSet();
		Gdx.graphics.setVSync(false);
	}
	
	//Overriden methods
	@Override
	public void render(float delta) {
		super.render(delta);
		Gdx.gl.glClearColor(0.1f,0.6f,0.9f,1);
		Gdx.gl.glClearColor(0f,0f,0f,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//Gdx.app.log("STPWTCH", ""+stopwatch.toString());
		
		if (!play && !end)
		{
			if (game.getMusicManager().isPlaying())
				game.getMusicManager().fadeOut(0.5f * delta);
			
			endGame();
		} else {
			if (changeProb && nextDelay <= 0){
				setRendererProblem();
				Gdx.app.log("", "Ganti masalah");
			}
			nextDelay -= nextDelay > 0 ? 1 : 0;
			
			if (stopwatch.getInterval() < lastInterval)
			{
				lastInterval = stopwatch.getInterval();
				if (lastInterval <= 0)
				{
					if (game.getMusicManager().isPlaying())
						game.getMusicManager().fadeOut(0.3f);
					game.getSoundManager().play(TebakNusantaraSound.TIMEOUT);
				}
				else
				if (lastInterval <= 3)
					game.getSoundManager().play(TebakNusantaraSound.ALARM);
				else
					game.getSoundManager().play(TebakNusantaraSound.TICK);
			}
			
			renderer.render(delta);
			
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();
			
			checkEndGame();
			
			batch.begin();
			drawPauseMenu();
			drawSettingMenu();
			drawStartingCounter();
			batch.end();
		}
		
		
		if (timeoutTime > 0.00f)
		{
			timeoutTime -= delta;
			wiggleTime += delta;
			
			if (wiggleTime > 0.08f)
			{
				timeoutRotation *= -1;
				wiggleTime = 0.00f;
			}
			
			batch.begin();
			dim.draw(batch);
			batch.draw(timeout, (TebakNusantara.SCREEN_WIDTH-timeout.getWidth())/2,
					(TebakNusantara.SCREEN_HEIGHT-timeout.getHeight())/2,
					timeout.getWidth()/2, timeout.getHeight()/2, timeout.getWidth(), timeout.getHeight(),
					RATIO, RATIO, timeoutRotation);
			batch.end();
			
			if (timeoutTime <= 0.0f)
			{
				if (!timeupfinish)
				{
					if (game.getScoreboardManager().isNewHighscore(player,  1))
						game.getSoundManager().play(TebakNusantaraSound.YAY);
					else
						game.getSoundManager().play(TebakNusantaraSound.FAIL);
				}
				
				timeupfinish = true;
			}
			return;
		}
		
		
		if (scoreDialog != null)
		{
			if (game.getMusicManager().isPlaying())
				game.getMusicManager().fadeOut(0.5f * delta);
			
			batch.begin();
			dim.draw(batch);
			batch.end();
			scoreDialog.act();
			scoreDialog.draw();
		}
		else
		if (highscoreDialog != null)
		{
			if (game.getMusicManager().isPlaying())
				game.getMusicManager().fadeOut(0.5f * delta);
			
			batch.begin();
			dim.draw(batch);
			batch.end();
			highscoreDialog.act();
			highscoreDialog.draw();
			
			if (highscoreDialog.isClicked())
			{
				scoreDialog = new ScoreDialog(game, player, 1, getFont(72));
				game.getScoreboardManager().insertToHighscore(player, 1);
				game.getPreferencesManager().writeScores(game.getScoreboardManager().getScoreboard());
			}
		}
				
	}

	@Override
	public void resize(int width, int height) {
	
	}
	
	
	@Override
	public void show() {
		//ambil soal dari problemSet,soal yang sudah diambil dihilangkan dari problem set
		stopwatch = new Stopwatch(stopwatchTime,stopwatchIncludeCenti);
		bonusStopwatch = new Stopwatch(bonusTime, true);
		startStopwatch = new Stopwatch(3, true);
		startStopwatch.start();
		
		lastInterval = 11;

		Texture totxt = new Texture(Gdx.files.internal("timeout.png"));
		timeout = new Sprite(totxt);
		timeoutTime = 0.00f;
		timeoutRotation = 1.00f;
		
		Gdx.app.log("STPWTCH", ""+stopwatch.getInterval());
		setStage();
		prepareStage(stage);
		Texture r = new Texture(Gdx.files.internal("data/trans.png"));
		r.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		dim = new Sprite(r, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		setStartCounter(3);
		renderer = new TebakJamakRenderer(this);
		buildMenu();
		Gdx.input.setInputProcessor(stage);
		nextProblem();
		setRendererProblem();
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
	
	}

	@Override
	public void dispose() {
		super.dispose();

	}
	
	//other methods	
	
	//Private methods
	private void checkEndGame(){
		if (stopwatch.isTimeUp()){
			if (play)
			{
				timeoutTime = 2.00f;
				
			}
			play = false;
			
		}
	}
			
	private void setStage(){

		
		stage.clear();
		//debug
		stage.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e, float x, float y){
					if (play) renderer.setText("Touched X:"+x+",y:"+y); //debugging
			}
		});
		
		float scaleX = (float) Gdx.graphics.getWidth() / 800f;
		float scaleY = (float) Gdx.graphics.getHeight() / 480f;
		
			
		//load semua resource yang di pake untuk ui widget
		//move these into a Skin implementation later for reusability
		Texture up = new Texture(Gdx.files.internal("data/KotakJawab8_00.png"));
		up.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Texture down = new Texture(Gdx.files.internal("data/KotakJawab8_01.png"));
		down.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		//Texture wrong = new Texture(Gdx.files.internal("data/KotakJawab_02.png"));
		//wrong.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		Sprite upButton = new Sprite(new TextureRegion(up));
		Sprite downButton = new Sprite(new TextureRegion(down));
		//Sprite wrongButton = new Sprite(new TextureRegion(wrong));
		
		Gdx.app.log(getName(), ""+upButton.getWidth());
		
		upButton.setSize(upButton.getWidth()*scaleX, upButton.getHeight()*scaleY);
		downButton.setSize(downButton.getWidth()*scaleX, downButton.getHeight()*scaleY);
		//wrongButton.setSize(wrongButton.getWidth()*scaleX, wrongButton.getHeight()*scaleY);
		
		Gdx.app.log(getName(), ""+upButton.getWidth());
		
		upDrawable = new SpriteDrawable(upButton);
		downDrawable = new SpriteDrawable(downButton);
		//wrongDrawable = new SpriteDrawable(wrongButton);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = upDrawable; //tekstur yang digambar pas button-nya dicuekkin
		style.down = downDrawable; //tekstur yang digambar pas button-nya diteken
		style.font = font; //font yang dipake untuk draw string
		
		firstChoice = new TextButton("", new TextButtonStyle(style));
		firstChoice.setPosition(0.65f*Gdx.graphics.getWidth() , 0.57f*Gdx.graphics.getHeight());
		firstChoice.getLabel().setFillParent(true);//setFontScale(2f);
		firstChoice.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				validateAnwser(1);
			}
		});
		Gdx.app.log(getName(), ""+firstChoice.getWidth());
		
		secondChoice = new TextButton("", new TextButtonStyle(style));
		secondChoice.setPosition(0.65f*Gdx.graphics.getWidth() , 0.41f*Gdx.graphics.getHeight());
		secondChoice.getLabel().setFillParent(true);//setFontScale(2f);
		secondChoice.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				validateAnwser(2);
			}
		});
		
		thirdChoice = new TextButton("", new TextButtonStyle(style));
		thirdChoice.setPosition(0.65f*Gdx.graphics.getWidth() , 0.25f*Gdx.graphics.getHeight());
		thirdChoice.getLabel().setFillParent(true);//setFontScale(2f);
		thirdChoice.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				validateAnwser(3);
			}
		});
		
		
		fourthChoice = new TextButton("", new TextButtonStyle(style));
		fourthChoice.setPosition(0.65f*Gdx.graphics.getWidth() , 0.09f*Gdx.graphics.getHeight());
		fourthChoice.getLabel().setFillParent(true);//setFontScale(2f);
		
		fourthChoice.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				validateAnwser(4);
			}
		});
		
		Texture upPause = new Texture(Gdx.files.internal("data/Pause8.png"));
		upPause.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Sprite upPauseButton =new Sprite(new TextureRegion(upPause));
		upPauseButton.setSize(upPauseButton.getWidth()*scaleX, upPauseButton.getHeight()*scaleY);
		TextButtonStyle pauseStyle = new TextButtonStyle();
		pauseStyle.up = new SpriteDrawable(upPauseButton);
		pauseButton = new Button(pauseStyle);
		pauseButton.setPosition(0.85f*Gdx.graphics.getWidth() , 0.885f*Gdx.graphics.getHeight());
		pauseButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				if (!paused){
					game.getSoundManager().play(TebakNusantaraSound.TAP);
					pauseClicked();
				}
			}
		});
		
		/*
		pauseButton.setScale(scaleX, scaleY-0.3f);
		firstChoice.setScale(scaleX, scaleY-0.3f);
		secondChoice.setScale(scaleX, scaleY-0.3f);
		thirdChoice.setScale(scaleX, scaleY-0.3f);
		fourthChoice.setScale(scaleX, scaleY-0.3f);
		*/
		
		buttonColor = new Color(firstChoice.getColor());
		//Gdx.app.log("", buttonColor.toString());
		
		firstChoice.getLabel().setColor(Color.WHITE);
		secondChoice.getLabel().setColor(Color.WHITE);
		thirdChoice.getLabel().setColor(Color.WHITE);
		fourthChoice.getLabel().setColor(Color.WHITE);
		
		
		Gdx.app.log(getName(), "pertama " + firstChoice.getLabel().getOriginX() + " " + firstChoice.getLabel().getOriginY());
		firstChoice.getLabel().setFontScale(0.75f*font.getScaleX(),0.75f*font.getScaleY());
		secondChoice.getLabel().setFontScale(0.75f*font.getScaleX(),0.75f*font.getScaleY());
		thirdChoice.getLabel().setFontScale(0.75f*font.getScaleX(),0.75f*font.getScaleY());
		fourthChoice.getLabel().setFontScale(0.75f*font.getScaleX(),0.75f*font.getScaleY());
		Gdx.app.log(getName(), "pertama " + firstChoice.getLabel().getOriginX() + " " + firstChoice.getLabel().getOriginY());
		
		stage.addActor(pauseButton);		
		stage.addActor(firstChoice);
		stage.addActor(secondChoice);
		stage.addActor(thirdChoice);
		stage.addActor(fourthChoice);
		
		
		TextButtonStyle bStyle = new TextButtonStyle();
		bStyle.downFontColor = Color.DARK_GRAY;
		bStyle.font = font; //font yang dipake untuk draw string
		bStyle.font.setColor(Color.RED);

	}
			
	private void buildMenu(){
		buildPauseMenu();
		//buildSettingMenu();
	}
	
	private void buildPauseMenu(){
		float scaleX = (float) Gdx.graphics.getWidth() / 800f;
		float scaleY = (float) Gdx.graphics.getHeight() / 480f;
		
		
		
		Texture buttonsTexture = new Texture(Gdx.files.internal("data/button8.png"));
		buttonsTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		ButtonStyle settingStyle = new ButtonStyle();
		Sprite settingUpSprite = new Sprite(new TextureRegion(buttonsTexture,0,0,77,128));
		Sprite settingDownSprite = new Sprite(new TextureRegion(buttonsTexture,128,0,77,128));
		
		settingUpSprite.setSize(settingUpSprite.getWidth()*scaleX, settingUpSprite.getHeight()*scaleY);
		settingDownSprite.setSize(settingDownSprite.getWidth()*scaleX, settingDownSprite.getHeight()*scaleY);
		
		settingStyle.up = new SpriteDrawable(settingUpSprite);
		settingStyle.down = new SpriteDrawable(settingDownSprite);
		Button setting = new Button(settingStyle);
		setting.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				paused = false;
				selectionDialog.hide();
				changingSetting = true;
				
				stage.addListener(settingDialog.getListener());
				prepareStage(stage);
				settingDialog.addAllActor(stage);
				settingDialog.show();
				//stage.getActors(); //simpen kemana dulu, biar tombol2 gak bisa dipencet dulu
				//game.setScreen(new KatalogueScreen(game));
			}
		});
		
		ButtonStyle homeStyle = new ButtonStyle();
		Sprite homeUpSprite = new Sprite(new TextureRegion(buttonsTexture,256,0,77,128));
		Sprite homeDownSprite = new Sprite(new TextureRegion(buttonsTexture,384,0,77,128));
		
		homeUpSprite.setSize(homeUpSprite.getWidth()*scaleX, homeUpSprite.getHeight()*scaleY);
		homeDownSprite.setSize(homeDownSprite.getWidth()*scaleX, homeDownSprite.getHeight()*scaleY);
		
		
		homeStyle.up = new SpriteDrawable(homeUpSprite);
		homeStyle.down = new SpriteDrawable(homeDownSprite);
		Button home = new Button(homeStyle);
		/*home.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				screenChanged();
				game.setScreen(game.getLoadingScreen(DESTINATION.MENU));
				//game.setScreen(new MenuScreen(game));
			}
		});*/
		home.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				paused = false;
				selectionDialog.hide();
				confirmDialog.show(stage);
			}
		});
		
		ButtonStyle resumeStyle = new ButtonStyle();
		Sprite resumeUpSprite = new Sprite(new TextureRegion(buttonsTexture,512,0,103,128));
		Sprite resumeDownSprite = new Sprite(new TextureRegion(buttonsTexture,640,0,103,128));
		
		resumeUpSprite.setSize(resumeUpSprite.getWidth()*scaleX, resumeUpSprite.getHeight()*scaleY);
		resumeDownSprite.setSize(resumeDownSprite.getWidth()*scaleX, resumeDownSprite.getHeight()*scaleY);
		
		
		resumeStyle.up = new SpriteDrawable(resumeUpSprite);
		resumeStyle.down = new SpriteDrawable(resumeDownSprite);
		Button resume = new Button(resumeStyle);
		resume.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				pauseClicked();
			}
		});
		
		ButtonStyle replyStyle = new ButtonStyle();
		Sprite replyUpSprite = new Sprite(new TextureRegion(buttonsTexture,768,0,77,128));
		Sprite replyDownSprite = new Sprite(new TextureRegion(buttonsTexture,896,0,77,128));
		
		replyUpSprite.setSize(replyUpSprite.getWidth()*scaleX, replyUpSprite.getHeight()*scaleY);
		replyDownSprite.setSize(replyDownSprite.getWidth()*scaleX, replyDownSprite.getHeight()*scaleY);
		
		
		replyStyle.up = new SpriteDrawable(replyUpSprite);
		replyStyle.down = new SpriteDrawable(replyDownSprite);
		Button reply = new Button(replyStyle);
		reply.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				screenChanged();
				game.setScreen(new TebakJamakScreen(game));
			}
		});
		
		Texture window = new Texture(Gdx.files.internal("data/PopUp8.png"));
		window.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Sprite windowSprite = new Sprite(new TextureRegion(window));
		windowSprite.setSize(windowSprite.getWidth()*scaleX, windowSprite.getHeight()*scaleY);
		WindowStyle windowStyle = new WindowStyle(font, Color.YELLOW, new SpriteDrawable(windowSprite));
		selectionDialog = new Dialog("", windowStyle);
		selectionDialog.setBounds(0.18f*Gdx.graphics.getWidth(), 0.2f*Gdx.graphics.getHeight(), 512 * (Gdx.graphics.getWidth()/800), 256 * (Gdx.graphics.getHeight()/480));
		selectionDialog.padTop(selectionDialog.getPadTop()-50*scaleY);
		selectionDialog.padBottom(selectionDialog.getPadBottom()+75*scaleY);
		selectionDialog.padLeft(selectionDialog.getPadLeft()-80*scaleX);
		selectionDialog.padRight(selectionDialog.getPadRight()-70*scaleX);
		
		selectionDialog.button(resume);
		selectionDialog.button(reply);
		selectionDialog.button(home);
		selectionDialog.button(setting);
		
		selectionDialog.hide();
		
		//Confirmation dialog
		ButtonStyle confirmYesStyle = new ButtonStyle();
		Sprite confirmYesUpSprite = new Sprite(game.getPilihanAtlas().findRegion("ya"));
		confirmYesUpSprite.setSize(confirmYesUpSprite.getWidth()*scaleX, confirmYesUpSprite.getHeight()*scaleY);
		confirmYesStyle.up = new SpriteDrawable(confirmYesUpSprite);
		
		Button confirmYes = new Button(confirmYesStyle);
		confirmYes.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				screenChanged();
				game.setScreen(game.getLoadingScreen(DESTINATION.MENU, DESTINATION.TEBAKJAMAK));
				
			}
		});
		
		ButtonStyle confirmNoStyle = new ButtonStyle();
		Sprite confirmNoUpSprite = new Sprite(game.getPilihanAtlas().findRegion("tidak"));
		confirmNoUpSprite.setSize(confirmNoUpSprite.getWidth()*scaleX, confirmNoUpSprite.getHeight()*scaleY);
		confirmNoStyle.up = new SpriteDrawable(confirmNoUpSprite);
		
		
		Button confirmNo = new Button(confirmNoStyle);
		confirmNo.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				pauseClicked();
			}
		});
		
		
		Sprite confirmSprite = new Sprite(game.getPilihanAtlas().findRegion("menu"));
		confirmSprite.setSize(confirmSprite.getWidth()*scaleX, confirmSprite.getHeight()*scaleY);
		WindowStyle confirmStyle = new WindowStyle(font, Color.YELLOW, new SpriteDrawable(confirmSprite));
		
		confirmDialog = new Dialog("", confirmStyle);
		confirmDialog.setBounds(0.18f*Gdx.graphics.getWidth(), 0.2f*Gdx.graphics.getHeight(), 512 * (Gdx.graphics.getWidth()/800), 256 * (Gdx.graphics.getHeight()/480));
		confirmDialog.padTop(confirmDialog.getPadTop()-50*scaleY);
		confirmDialog.padBottom(confirmDialog.getPadBottom()+75*scaleY);
		confirmDialog.padLeft(confirmDialog.getPadLeft()-80*scaleX);
		confirmDialog.padRight(confirmDialog.getPadRight()-70*scaleX);
		
		confirmDialog.button(confirmYes);
		confirmDialog.button(confirmNo);
		
		confirmDialog.hide();
	
		
	//}
	
	//private void buildSettingMenu(){
		/*float scaleX = (float) Gdx.graphics.getWidth() / 800f;
		float scaleY = (float) Gdx.graphics.getHeight() / 480f;
		
		Texture window = new Texture(Gdx.files.internal("data/PopUp8.png"));
		window.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Sprite windowSprite = new Sprite(new TextureRegion(window));
		windowSprite.setSize(windowSprite.getWidth()*scaleX, windowSprite.getHeight()*scaleY);
		*/
		
		settingDialog = new DialogNinePatch(font, "",(Gdx.graphics.getWidth()-400*scaleX)/2,(Gdx.graphics.getHeight()-200*scaleY)/2, 400*scaleX, 200*scaleY);
		settingDialog.setDrawWindow(false);
		settingDialog.setBackground(windowSprite);
		
		
		Texture back = new Texture(Gdx.files.internal("bar_geser.png"));
		back.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Texture mark = new Texture(Gdx.files.internal("mark_geser.png"));
		mark.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Sprite backSlider = new Sprite(back);
		Sprite markSlider = new Sprite(mark,6,0,19,32);
		
		SliderStyle sStyle = new SliderStyle(new SpriteDrawable(backSlider), new SpriteDrawable(markSlider));
		Slider soundSlider  =  new Slider(0, 1, 0.1f, false, sStyle);
		soundSlider.setValue(game.getPreferencesManager().getVolume());
		lastSoundSliderValue = soundSlider.getValue();
		//soundSlider.setValueChange bla bla
		
		Slider musicSlider = new Slider(0,1,0.1f,false,sStyle);
		musicSlider.setValue(game.getPreferencesManager().getVolume());
		lastMusicSliderValue = musicSlider.getValue();
		//game.getPreferencesManager().setVolume(musicSlider.getValue());
		//todo : add listener add lisetener
		
		settingDialog.actor(soundSlider);
		settingDialog.actor(musicSlider);
		settingDialog.getActor(0).setPosition(350*scaleX, 260*scaleY);
		settingDialog.getActor(0).setSize(backSlider.getWidth()*scaleX, backSlider.getHeight()*scaleY);
		settingDialog.getActor(1).setPosition(350*scaleX, 220*scaleY);
		settingDialog.getActor(1).setSize(backSlider.getWidth()*scaleX, backSlider.getHeight()*scaleY);
		
		
		LabelStyle lStyle = new LabelStyle(font, new Color(0.4f, 0.1f, 0.1f, 0.8f));
		Label soundLabel = new Label("Sound", lStyle);
		Label musicLabel = new Label("Music", lStyle);
		
		settingDialog.actor(soundLabel);
		settingDialog.actor(musicLabel);
		settingDialog.getActor(2).setPosition(230*scaleX, 260*scaleY);
		settingDialog.getActor(2).setSize(50*scaleX, backSlider.getHeight()*scaleY);
		settingDialog.getActor(3).setPosition(230*scaleX, 220*scaleY);
		settingDialog.getActor(3).setSize(50*scaleX, backSlider.getHeight()*scaleY);
		
		Texture check = new Texture(Gdx.files.internal("x_kotak.png"));
		check.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		CheckBoxStyle cStyle = new CheckBoxStyle(new SpriteDrawable(new Sprite(check)), new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("0_kotak.png")))), font, new Color(0.4f, 0.1f, 0.1f, 0.8f));
		CheckBox muteCheckBox = new CheckBox(" Mute", cStyle);
		muteCheckBox.setChecked(game.getPreferencesManager().isMusicEnabled() && game.getPreferencesManager().isSoundEnabled()); //isSoundEnabled?
		lastMuteCheckBoxValue = muteCheckBox.isChecked();
		//muteCheckBox.setc
		
		settingDialog.actor(muteCheckBox);
		settingDialog.getActor(4).setPosition(350*scaleX, 180*scaleY);
		settingDialog.getActor(4).setSize(100*scaleX, 50*scaleY);
				
		
		Texture selesai = new Texture(Gdx.files.internal("PopUpPause.png"));
		selesai.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Sprite selesaiSprite = new Sprite(selesai,0,17,135,64-17);
		//selesaiSprite.setSize(;, height)
		Button selesaiButton = new Button(new SpriteDrawable(selesaiSprite));
		selesaiButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				paused = true;
				selectionDialog.show(stage);
				changingSetting = false;
				stage.removeListener(settingDialog.getListener());
				settingDialog.removeAllActorFromStage(stage);
				relieveStage(stage);
				settingDialog.hide();
				
			}
		});
		settingDialog.actor(selesaiButton);
		settingDialog.getActor(5).setPosition(330*scaleX, 135*scaleY);
		settingDialog.getActor(5).setSize(selesaiSprite.getWidth()*scaleX, selesaiSprite.getHeight()*scaleY);
	
	}
	
	
	//Drawing private methods
	private void drawPauseMenu(){
		if (paused){
			dim.draw(batch);
			selectionDialog.draw(batch, 1);
		}
	}
	
	private void drawSettingMenu(){
		if (changingSetting){
			float newSoundVolume = ((Slider) settingDialog.getActor(0)).getValue();
			if (newSoundVolume != lastSoundSliderValue){ //soundSlider
				game.getSoundManager().setVolume(newSoundVolume);
			}
			lastSoundSliderValue = newSoundVolume;
			
			float newMusicVolume = ((Slider) settingDialog.getActor(1)).getValue();
			if (newMusicVolume != lastMusicSliderValue){ //musicSlider
				game.getMusicManager().setVolume(newMusicVolume);
			}
			lastMusicSliderValue = newMusicVolume;
			
			boolean newValue = ((CheckBox) settingDialog.getActor(4)).isChecked();
			if (newValue != lastMuteCheckBoxValue){ //muteCheckBox
				game.getMusicManager().setEnabled(newValue);
				game.getSoundManager().setEnabled(newValue);
			}
			lastMuteCheckBoxValue = newValue;
			dim.draw(batch);
			settingDialog.drawDialog(batch);
		}
	}
	
	
	private void drawStartingCounter(){
		if (starting) {
			dim.draw(batch);
			
			if (startStopwatch.getInterval() == 2 && startCounterAlpha <= 0f) setStartCounter(2);
			if (startStopwatch.getInterval() == 1 && startCounterAlpha <= 0f) setStartCounter(1);
			startCounterAlpha -= 0.015f;
			if (startCounterAlpha < 0) startCounterAlpha = 0f;
			startCounter.draw(batch,startCounterAlpha);
			
			if (startStopwatch.isTimeUp()){
				starting = false;
				stopwatch.start();
				game.getMusicManager().play(TebakNusantaraMusic.GAME);
				relieveStage(stage);
			}
		}
	}

	
	//Public Methods
	public void addScore(int score){
		this.score += score; 
		renderer.addScoreAnimation(score);
	}
			
	public void toggleStopwatch(){
		if (stopwatch.isRunning()) stopwatch.pause();
		else stopwatch.start();
	}
	
	
	public void setStartCounter(int i){
		startCounterAlpha = 1f;
		int position = 3-i;
		float scaleX = (float) Gdx.graphics.getWidth() / 800f;
		float scaleY = (float) Gdx.graphics.getHeight() / 480f;
		Texture r = new Texture(Gdx.files.internal("321.png"));
		r.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		startCounter = new Sprite(r,0+((2048/3)*position),0,(int) (2048/3),512);
		startCounter.setSize((512/3)*scaleX, (128)*scaleY);
		startCounter.setPosition((Gdx.graphics.getWidth()-startCounter.getWidth()+20*scaleX)/2, (Gdx.graphics.getHeight()-startCounter.getHeight()*scaleY)/2);
	}

	public void nextProblem(){
		if (problemSet.size() > 0) {
			//ambil soal berikutnya secara acak
			int next = new Random().nextInt(problemSet.size());
			nextProb = problemSet.get(next);
			changeProb = true;
			nextDelay = NEXTDELAY;
			
			firstChoice.setTouchable(Touchable.disabled);
			secondChoice.setTouchable(Touchable.disabled);
			thirdChoice.setTouchable(Touchable.disabled);
			fourthChoice.setTouchable(Touchable.disabled);
			
			
			
			//setRendererProblem();
			//renderer.setProblem(nextProb);
			nextProb.setAnswered(true);
			//nextProb.setDesc("sudah");
			
			//sementara diremove dulu biar banyak soalnya			
			problemSet.remove(next);
			answer = nextProb.getCorrect();
		} else { //waktu masih tersisa tapi semua soal sudah dijawab
			renderer.setText("Out of Problems!");
			play = false;
			
		}
		bonusStopwatch.reset();
		bonusStopwatch.start();
	}
	
	private void setRendererProblem(){
		firstChoice.setText(nextProb.getChoice(0));
		secondChoice.setText(nextProb.getChoice(1));
		thirdChoice.setText(nextProb.getChoice(2));
		fourthChoice.setText(nextProb.getChoice(3));
		
		firstChoice.getColor().set(buttonColor);
		secondChoice.getColor().set(buttonColor);
		thirdChoice.getColor().set(buttonColor);
		fourthChoice.getColor().set(buttonColor);
		
		
		firstChoice.getStyle().up = upDrawable;
		secondChoice.getStyle().up = upDrawable;
		thirdChoice.getStyle().up = upDrawable;
		fourthChoice.getStyle().up = upDrawable;
		
		
		firstChoice.setTouchable(Touchable.enabled);
		secondChoice.setTouchable(Touchable.enabled);
		thirdChoice.setTouchable(Touchable.enabled);
		fourthChoice.setTouchable(Touchable.enabled);
		
		renderer.setProblem(nextProb);
		changeProb = false;
	}
	
	public void validateAnwser(int answer){
		if (play){
			if (stopwatch.isRunning()){
		
				if (!stopwatch.isTimeUp()) {
					if (this.answer == answer) {
						if (!bonusStopwatch.isTimeUp()){
							renderer.setText("Bonus!");
							addScore(bonusScore);
						}
						//jawaban benar,tambahkan skor
						else addScore(scorePerProblem);
						correctCounter++;
						game.getSoundManager().play(TebakNusantaraSound.HIT);
						if (correctCounter >= comboTreshold){
							renderer.comboAnimation(correctCounter);
							this.score += correctCounter*scorePerProblem;
							//addComboScore(correctCounter*bonusScore);
						}
						
						
					}
					else{
						correctCounter = 0;
						game.getSoundManager().play(TebakNusantaraSound.MISS);
						switch(answer){
						case 1:
							firstChoice.getColor().set(Color.RED);
							break;
						case 2:
							secondChoice.getColor().set(Color.RED);
							break;
						case 3:
							thirdChoice.getColor().set(Color.RED);
							break;
						case 4:
							fourthChoice.getColor().set(Color.RED);
							break;
						}
					}
					
					switch (renderer.getProblem().getCorrect()){
					case 1:
						firstChoice.getStyle().up = downDrawable;
						break;
					case 2:
						secondChoice.getStyle().up = downDrawable;
						break;
					case 3:
						thirdChoice.getStyle().up = downDrawable;
						break;
					case 4:
						fourthChoice.getStyle().up = downDrawable;
						break;
					}
					/*
					switch(renderer.getProblem().getCorrect()){
					case 1:
						firstChoice.getColor().set(Color.GREEN);
						break;
					case 2:
						secondChoice.getColor().set(Color.GREEN);
						break;
					case 3:
						thirdChoice.getColor().set(Color.GREEN);
						break;
					case 4:
						fourthChoice.getColor().set(Color.GREEN);
						break;
					}*/
					nextProblem();
					
				} else {
					play = false;
					
				}
				
			
				
			} //else the game is being paused
		} //else the game ends
		else endGame();
	}
	
	public void pauseClicked(){
		game.getSoundManager().play(TebakNusantaraSound.TAP);
		Gdx.app.log(getName(), "pauseclick"+stopwatch.isRunning());
		toggleStopwatch();
		if (stopwatch.isRunning()){
			nextProblem();
			paused = false;
			selectionDialog.hide();
		}
		else{
			selectionDialog.show(stage);
			paused = true;
		}
		
	}

	public void screenChanged(){
		//disini supposed to be nge dispose,
		//tapi kalo screennya ganti ke scoreScreen bakal nge hang, gak tau kenapa
		//kalo ganti ke selectionScreen gak masalah
		//dispose();
		//font.setScale(fontScaleX, fontScaleY);
	}
	
	public void endGame(){
		
		
		Gdx.app.log(getName(), "masuk end game");
		screenChanged();
				
		player = new Player(score);
		
		
		if (game.getScoreboardManager().isNewHighscore(player,  1))
		{
			highscoreDialog = new HighscoreDialog(game, player, 1, getFont(24));
		} else
		{
			scoreDialog = new ScoreDialog(game, player, 1, getFont(72));
		}
		end = true;
		//game.setScreen(game.getScoreScreen(ScoreScreen.TEBAK_BUDAYA, score));
	}
	
	public void onBackKeyPressed(){
		if (!paused) pauseClicked();		
	}
	
	//Getters
	public int getScore(){
		return score;
	}
		
	public Stopwatch getStopwatch(){
		return stopwatch;
	}
		
	public BitmapFont getFont(){
		return font;
	}
	
	public SpriteBatch getBatch(){
		return batch;
	}
	
	public TebakNusantara getGame(){
		return game;
	}
	
	public Stage getStage(){
		return stage;
	}

	
	//Static methods
	@SuppressWarnings("unchecked")
	public static ArrayList<TebakanJamak> problemSet(){
		FileHandle path = Gdx.files.internal("data/masalah.json");
		problemSet = new ArrayList<TebakanJamak>();
		problemSet = (ArrayList<TebakanJamak>) load(path);
		
		return problemSet;
	}
	
	public static Object load(FileHandle path){
		Json json = new Json();
		Gdx.app.log("JSON", json.prettyPrint(json.fromJson(ArrayList.class, path)));
		return json.fromJson(ArrayList.class, path);
	}
	
	public static void prepareStage(Stage stage){
		for (int i = 0; i < stage.getActors().size; i++){
			stage.getActors().get(i).setTouchable(Touchable.disabled);
			
		}
	}
	
	public static void relieveStage(Stage stage){
		for (int i = 0; i < stage.getActors().size;i++){
			stage.getActors().get(i).setTouchable(Touchable.enabled);
		}
	}

	@Override
	public void semidispose() {
		// TODO Auto-generated method stub
		/*for (int i = 0;i < problemSet.size();i++) {
			problemSet.get(i).getSprite().getTexture().dispose();
		}*/
		Gdx.app.log("", "disposed");
		//startCounter.getTexture().dispose();
		settingDialog.dispose();
		dim.getTexture().dispose();
		renderer.dispose();
		//stage.dispose();
		Gdx.input.setInputProcessor(null);
	}
}