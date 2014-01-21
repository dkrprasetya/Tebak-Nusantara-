package com.deadlinestudio.tebaknusantara.tebakpeta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.screens.AbstractScreen;
import com.deadlinestudio.tebaknusantara.screens.LoadingScreen.DESTINATION;
import com.deadlinestudio.tebaknusantara.screens.TebakJamakScreen;
import com.deadlinestudio.tebaknusantara.services.MusicManager.TebakNusantaraMusic;
import com.deadlinestudio.tebaknusantara.services.SoundManager.TebakNusantaraSound;
import com.deadlinestudio.tebaknusantara.tebakbudaya.DialogNinePatch;
import com.deadlinestudio.tebaknusantara.tebakbudaya.Stopwatch;
import com.deadlinestudio.tebaknusantara.tebakpeta.RamaModel.RamaState;
import com.deadlinestudio.tebaknusantara.util.BGPattern;
import com.deadlinestudio.tebaknusantara.util.BGPattern.COLOR;
import com.deadlinestudio.tebaknusantara.util.CountdownDrawer;
import com.deadlinestudio.tebaknusantara.util.HighscoreDialog;
import com.deadlinestudio.tebaknusantara.util.Player;
import com.deadlinestudio.tebaknusantara.util.ScoreDialog;

public class TouchmapScreen extends AbstractScreen {
	
	private final float TRANSITION_TIME = 0.5f;
	
	public enum TouchmapState {
		PAUSE,
		PLAY,
		GAMEOVER
	}
	
	private BitmapFont barFont;
	
	private TouchmapState state;
	
	private TextureAtlas atlas;
	
	private boolean falling = false;
	
	private float width;
	private float height;

	Stage pauseStage;
	Stage gameStage;
	
	PauseBackground pauseBackground;
	PausePanel pausePanel;
	PauseButton resumeButton;
	PauseButton replyButton;
	PauseButton homeButton;
	PauseButton settingButton;
	
	BGPattern gameBackground;
	GameRunner2 runner;
	GameConveyor conveyor;
	GameCroc croc;
	GameBar bar;
	GameMap map;
	
	CountdownDrawer condon;
	Texture tx;

	//Sound hit = Gdx.audio.newSound(Gdx.files.internal("sound/correct.mp3"));
	//Sound miss = Gdx.audio.newSound(Gdx.files.internal("sound/wrong.mp3"));
	
	//------------------ STAGE FINAL BUAT ANIMASI GAMEOVER -------
	Stage finalStage;
	
	///////////// BUAT SCOREBOARD /////////////////
	HighscoreDialog highscoreDialog = null;
	ScoreDialog scoreDialog = null;
	Player player = null;
	//////////////////////////////////////////
	
	//Tambahan dari vai
	private DialogNinePatch settingDialog;
	private boolean changingSetting = false;
	private Stage settingStage;
	private InputProcessor tempSetting;
	private Sprite sBoard;
	
	float lastSoundSliderValue;
	float lastMusicSliderValue;
	boolean lastMuteCheckBoxValue;
	
	//Dari Vai lagi
	private Dialog confirmDialog;
	private Stage confirmStage;

	AssetManager assets;

	private Image benar;
	private Image salah;
	private TextureAtlas tttatlas;
	
	// Kayak vai
	private Sprite dim;
	private Sprite startCounter;
	private float startCounterAlpha = 1f;
	
	// start counter
	private Stopwatch startStopwatch;
	private boolean starting = true;
	

	public void showBola(boolean value, float x, float y) {
		Image toShow = value ? benar : salah;
		toShow.addAction(Actions.moveTo(x - toShow.getImageWidth() / 2, y - toShow.getImageWidth() / 2));
		toShow.addAction(Actions.sequence(Actions.alpha(1), Actions.delay(0.25f), Actions.fadeOut(0.5f)));
	}
	
	public TouchmapScreen(final TebakNusantara game) {
		super(game);

		tttatlas = new TextureAtlas(Gdx.files.internal("truefalse.txt"));
		benar = new Image(tttatlas.findRegion("true"));
		salah = new Image(tttatlas.findRegion("false"));
		benar.addAction(Actions.alpha(0));
		salah.addAction(Actions.alpha(0));
		
		
		Gdx.input.setInputProcessor(null);
		Texture tx = new Texture(Gdx.files.internal("321.png"));
		//condon = new CountdownDrawer(tx, game);
		
		assets = new AssetManager();
		
		atlas = new TextureAtlas("tebakpeta/touchmap.txt");
		if (atlas == null)
			Gdx.app.log("tes", "atlas nya null");
		barFont = new BitmapFont(Gdx.files.internal("tebakpeta/fredoka.fnt"), false);
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		pauseStage = new Stage(getWidth(), getHeight(), true, batch);
		pauseStage.addAction(Actions.alpha(0));
		gameStage = new Stage(getWidth(), getHeight(), true, batch);
		gameStage.addAction(Actions.alpha(1));

		state = TouchmapState.PLAY;

		RamaActor rama = new RamaActor(assets, RamaState.PANIC);
		
		croc = new GameCroc(this);
		conveyor = new GameConveyor(this);
		runner = new GameRunner2(this, rama.model);
		bar = new GameBar(this);
		map = new GameMap(this, Gdx.files.internal("tebakpeta/mapdata.txt"));

		
		pauseBackground = new PauseBackground(this);
		pausePanel = new PausePanel(this);
		resumeButton = new PauseButton(this, "resume", -1.1f) {
			@Override
			public void onTap() {
				setStateToPlay();
			}
		};
		replyButton = new PauseButton(this, "reply", -0.3f) {
			@Override
			public void onTap() {
				game.setScreen(game.getLoadingScreen(DESTINATION.TOUCHMAP, DESTINATION.TOUCHMAP));
			}
		};
		homeButton = new PauseButton(this, "home", 0.5f) {
			@Override
			public void onTap() { 
				//game.setScreen(game.getLoadingScreen(DESTINATION.MENU));
				//Dari Vai
				tempSetting = Gdx.input.getInputProcessor();
				Gdx.input.setInputProcessor(confirmStage);
				confirmDialog.show(confirmStage);
			}
		};
		settingButton = new PauseButton(this, "setting", 1.3f) {
			@Override
			public void onTap() {
				//super.onTap();
				
				//Dari vai
				tempSetting = Gdx.input.getInputProcessor();
				Gdx.input.setInputProcessor(settingStage);
				changingSetting = true;
				settingStage.addListener(settingDialog.getListener());
				TebakJamakScreen.prepareStage(settingStage);
				settingDialog.addAllActor(settingStage);
				settingDialog.show();
				
			}
		};
		
		gameBackground = new BGPattern();
		gameBackground.setColor(COLOR.YELLOW);

		gameStage.addActor(gameBackground);
		gameStage.addActor(map);
		gameStage.addActor(bar);
		gameStage.addActor(conveyor);
		gameStage.addActor(runner);
		gameStage.addActor(croc);

		
		benar.setVisible(false);
		salah.setVisible(false);
		
		pauseStage.addActor(pauseBackground);
		pauseStage.addActor(pausePanel);
		pauseStage.addActor(resumeButton);
		pauseStage.addActor(replyButton);
		pauseStage.addActor(homeButton);
		pauseStage.addActor(settingButton);
		
		//Dari vai
		settingStage = new Stage();
		confirmStage = new Stage();
		buildSettingMenu();
		buildConfirmDialog();
		
		// finalstage
		finalStage = new Stage();
		GameCroc croc2 = new GameCroc(this);
		croc2.setScale(1.5f);
		rama.setScale(4);
		rama.setX(SCREEN_WIDTH - rama.getWidth());
		rama.setY(200);
		finalStage.addActor(pauseBackground);
		finalStage.addActor(rama);
		finalStage.addActor(croc2);
		rama.addAction(Actions.sequence(Actions.moveBy(-1500, -700, 3.0f, Interpolation.pow2), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				int score = (int) bar.getScore();
				player = new Player(score);
				if (game.getScoreboardManager().isNewHighscore(player, 0)) {
					game.getSoundManager().play(TebakNusantaraSound.YAY);
					highscoreDialog = new HighscoreDialog(game, player, 0, getFont(24));
				} else {
					game.getSoundManager().play(TebakNusantaraSound.FAIL);
					scoreDialog = new ScoreDialog(game, player, 0, getFont(72));
				}
			}
		})));
		
		gameStage.addActor(benar);
		gameStage.addActor(salah);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
	
	private boolean ipHasNotBeenAdded = true;
	
	@Override
	public void render(float delta) {
		
		Gdx.gl10.glClearColor(0, 0, 0, 1);
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		if (starting)
		{
			gameStage.draw();
			drawStartingCounter();
			return;
		}

		if (ipHasNotBeenAdded) {
			Gdx.input.setInputProcessor(new InputMultiplexer(new GestureDetector(new PauseListener(this)), new GestureDetector(new GameListener(this))));
			ipHasNotBeenAdded = false;
			game.getMusicManager().play(TebakNusantaraMusic.GAME);
			benar.setVisible(true);
			salah.setVisible(true);
		}
		
		if (state == TouchmapState.GAMEOVER) {
			if (game.getMusicManager().isPlaying())
				game.getMusicManager().fadeOut(0.3f * delta);
			
			if (!falling)
			{
				game.getSoundManager().play(TebakNusantaraSound.FALLING);
				falling = true;
			}
			
			finalStage.act(delta);
			
			gameStage.draw();
			pauseStage.draw();
			finalStage.draw();

			//Dari vai
			batch.begin();
			drawSettingMenu();
			batch.end();

			if (scoreDialog != null)
			{
				scoreDialog.act();
				scoreDialog.draw();
			}
			else
			if (highscoreDialog != null)
			{
				highscoreDialog.act();
				highscoreDialog.draw();
				
				if (highscoreDialog.isClicked())
				{
					scoreDialog = new ScoreDialog(game, player, 0, getFont(72));
					game.getScoreboardManager().insertToHighscore(player, 0);
					game.getPreferencesManager().writeScores(game.getScoreboardManager().getScoreboard());
				}
			}
			
		} else {
			if (!changingSetting){
				pauseStage.act(delta);
				if (state != TouchmapState.PAUSE) gameStage.act(delta);
			} else if (changingSetting) settingStage.act(delta);
			confirmStage.act(delta);
			
			
			gameStage.draw();
			pauseStage.draw();
			settingStage.draw();
			confirmStage.draw();
			
			//Dari vai
			batch.begin();
			drawSettingMenu();
			batch.end();
			
			if ((runner.isGameOver()) && (state != TouchmapState.GAMEOVER)) {
				onGameOver();
				state = TouchmapState.GAMEOVER;
			}
			
			if (scoreDialog != null)
			{
				scoreDialog.act();
				scoreDialog.draw();
			}
			else
			if (highscoreDialog != null)
			{
				highscoreDialog.act();
				highscoreDialog.draw();
				
				if (highscoreDialog.isClicked())
				{
					scoreDialog = new ScoreDialog(game, player, 0, getFont(72));
					game.getScoreboardManager().insertToHighscore(player, 0);
				}
			}
		}
	}

	private void onGameOver() {
		gameBackground.setColor(COLOR.RED);
		
		game.getSoundManager().play(TebakNusantaraSound.FALLING);
		finalStage.addAction(Actions.fadeIn(1.0f, Interpolation.linear));
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		Texture r = new Texture(Gdx.files.internal("data/trans.png"));
		r.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		dim = new Sprite(r, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		startStopwatch = new Stopwatch(3, true);
		startStopwatch.start();
		
		setStartCounter(3);
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

	@Override
	public void hide() {
		setStateToPause();
	}

	@Override
	public void pause() {
		setStateToPause();
	}

	@Override
	public void resume() {
	}
	
	public TextureAtlas getAtlas() { return atlas; }
	public BitmapFont getBarFont() { return barFont; }
	public TouchmapState getState() { return state; }
	
	public float getWidth() { return width; }
	public float getHeight() { return height; }
	
	public void setStateToPause() {
		Action pauseAction = new Action() {
			@Override
			public boolean act(float delta) {
				Gdx.app.log(TouchmapScreen.this.getClass().getSimpleName(), "Change state to pause.");
				state = TouchmapState.PAUSE;
				return true;
			}
		};
		pauseStage.addAction(Actions.sequence(pauseAction, Actions.fadeIn(TRANSITION_TIME)));
	}
	
	public void setStateToPlay() {
		Action playAction = new Action() {
			@Override
			public boolean act(float delta) {
				Gdx.app.log(TouchmapScreen.this.getClass().getSimpleName(), "Change state to play.");
				state = TouchmapState.PLAY;
				return true;
			}
		};
		pauseStage.addAction(Actions.sequence(Actions.fadeOut(TRANSITION_TIME), playAction));
	}

	@Override
	public void onBackKeyPressed() {
		setStateToPause();
	}
	
	//Dari vai
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
			
			sBoard.draw(batch); 
			settingDialog.drawDialog(batch);
		}
	}
	
	private void drawStartingCounter(){
		if (starting) {
			batch.begin();
			dim.draw(batch);
			
			if (startStopwatch.getInterval() == 2 && startCounterAlpha <= 0f) setStartCounter(2);
			if (startStopwatch.getInterval() == 1 && startCounterAlpha <= 0f) setStartCounter(1);
			startCounterAlpha -= 0.015f;
			if (startCounterAlpha < 0) startCounterAlpha = 0f;
			startCounter.draw(batch,startCounterAlpha);
			
			batch.end();
			if (startStopwatch.isTimeUp()){
				starting = false;
				//game.getMusicManager().play(TebakNusantaraMusic.GAME);
			}
		}
	}
	
	
	private void buildSettingMenu(){
		float scaleX = (float) Gdx.graphics.getWidth() / 800f;
		float scaleY = (float) Gdx.graphics.getHeight() / 480f;
		
		
		sBoard = new Sprite(new Texture(Gdx.files.internal("data/PopUp8.png")));
		sBoard.setSize(sBoard.getWidth()*scaleX, sBoard.getHeight()*scaleY);
		sBoard.setPosition((Gdx.graphics.getWidth()-sBoard.getWidth())/2, (Gdx.graphics.getHeight()-sBoard.getHeight())/2);
		
		settingDialog = new DialogNinePatch(font, "",(Gdx.graphics.getWidth()-400*scaleX)/2,(Gdx.graphics.getHeight()-200*scaleY)/2, 400*scaleX, 200*scaleY);
		settingDialog.setDrawWindow(false);
		
		
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
		muteCheckBox.setChecked(game.getPreferencesManager().isMusicEnabled()); //isSoundEnabled?
		lastMuteCheckBoxValue = muteCheckBox.isChecked();
		//muteCheckBox.setc
		
		settingDialog.actor(muteCheckBox);
		settingDialog.getActor(4).setPosition(350*scaleX, 180*scaleY);
		settingDialog.getActor(4).setSize(100*scaleX, 50*scaleY);
				
		
		Texture selesai = new Texture(Gdx.files.internal("PopUpPause.png"));
		selesai.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Sprite selesaiSprite = new Sprite(selesai,0,17,135,64-17);
		Button selesaiButton = new Button(new SpriteDrawable(selesaiSprite));
		selesaiButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				changingSetting = false;
				settingStage.removeListener(settingDialog.getListener());
				settingDialog.removeAllActorFromStage(settingStage);
				TebakJamakScreen.relieveStage(settingStage);
				settingDialog.hide();
				Gdx.input.setInputProcessor(tempSetting);
				
			}
		});
		settingDialog.actor(selesaiButton);
		settingDialog.getActor(5).setPosition(330*scaleX, 135*scaleY);
		settingDialog.getActor(5).setSize(selesaiSprite.getWidth()*scaleX, selesaiSprite.getHeight()*scaleY);
	
	}
	
	private void buildConfirmDialog(){
		//Confirmation dialog
		float scaleX = (float) Gdx.graphics.getWidth() / 800f;
		float scaleY = (float) Gdx.graphics.getHeight() / 480f;
		
		
		ButtonStyle confirmYesStyle = new ButtonStyle();
		Sprite confirmYesUpSprite = new Sprite(game.getPilihanAtlas().findRegion("ya"));
		confirmYesUpSprite.setSize(confirmYesUpSprite.getWidth()*scaleX, confirmYesUpSprite.getHeight()*scaleY);
		confirmYesStyle.up = new SpriteDrawable(confirmYesUpSprite);
		
		Button confirmYes = new Button(confirmYesStyle);
		confirmYes.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent e,float x, float y){
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				game.setScreen(game.getLoadingScreen(DESTINATION.MENU, DESTINATION.TOUCHMAP));
				
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
				Gdx.input.setInputProcessor(tempSetting);
				setStateToPlay();
				//pauseClicked();
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
			
	}

	@Override
	public void semidispose() {
		// TODO Auto-generated method stub
		condon.dispose();
		pauseStage.dispose();
		gameStage.dispose();
		barFont.dispose();
		atlas.dispose();
		batch.dispose();
		//hit.dispose();
		//miss.dispose();
		//Dari vai
		settingDialog.dispose();
		
		finalStage.dispose();
		
		assets.clear();
		assets.dispose();
	}
}
