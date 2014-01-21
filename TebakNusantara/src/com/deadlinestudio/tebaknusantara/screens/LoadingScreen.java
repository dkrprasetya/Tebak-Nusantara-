package com.deadlinestudio.tebaknusantara.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.util.Animator;
import com.deadlinestudio.tebaknusantara.util.Image2;

public class LoadingScreen extends AbstractScreen {

	public enum DESTINATION {
		START, MENU, TEBAKJAMAK, TOUCHMAP, KATALOG, NO
	}
	
	private TextureAtlas atlas;
	private final int RUN_WIDTH = 75;
	private final int RUN_HEIGHT = 115;
	private boolean isDelaying;
	
	TextureRegion texture;
	TextureRegion[] frames;
	
	Texture loadtext;
		
	Sprite textSprite;
	
	Animator runner;
	
	private final DESTINATION dest;

	/// ----------- dari kesgar
	private Label titleLabel;
	private Label contentLabel;
	
	BitmapFont titleFont;
	BitmapFont contentFont;
	
	public LoadingScreen(final TebakNusantara game, final DESTINATION dest, final DESTINATION src) {
		super(game);


		Gdx.app.log("LOADING", "start loading");
		
		/// dari kesgar begin

		//int fontSize = (int) (0.04f * stage.getHeight());
		//int titleFontSize = fontSize * 2;
		
		titleFont = getFont(60);
		titleLabel = new Label("Kamu harus tahu...", new LabelStyle(titleFont, Color.WHITE));
		titleLabel.setAlignment(Align.left | Align.top);
		stage.addActor(titleLabel);
		
		contentFont = getFont(28);
		contentLabel = new Label("Lorem ipsum dolor sit amet.", new LabelStyle(contentFont, Color.WHITE));
		contentLabel.setAlignment(Align.left | Align.bottom);
		stage.addActor(contentLabel);
		
		float margin = 0.1f * stage.getHeight();
		titleLabel.setPosition(margin, stage.getHeight() - 2 * margin);
		contentLabel.setPosition(margin, margin);
		
		List<String> facts = new ArrayList<String>();
		Scanner scanner = new Scanner(Gdx.files.internal("loadingfacts.txt").readString());
		while (scanner.hasNextLine()) {
			facts.add(scanner.nextLine());
		}
		CharSequence randomFact = facts.get(new Random().nextInt(facts.size()));
		String clean = "";
		for (int i = 0; i < randomFact.length(); i++) {
			clean += randomFact.charAt(i) == '+' ? '\n' : randomFact.charAt(i);
		}
		contentLabel.setText(clean);
		
		/// dari kesgar end
		
		Gdx.app.log("LOADING", "end of kesgar");
		stage.addAction(Actions.alpha(0.0f));
		
		this.dest = dest;
		
		atlas = new TextureAtlas(Gdx.files.internal("tebakpeta/touchmap.txt"));
		loadtext = new Texture(Gdx.files.internal("loadingscreen/loading.png"));	
		
		texture = atlas.findRegion("runner_normal");
		
		int n_frame = 1024 / RUN_HEIGHT;
						
		frames = new TextureRegion[n_frame];
		for (int i = 0; i < n_frame; ++i)
		{
			frames[i] = new TextureRegion(texture,i*RUN_WIDTH,0,RUN_WIDTH,RUN_HEIGHT);
		}
				
		runner = new Animator(new Animation(0.1f, frames));
		
		runner.setX(game.SCREEN_WIDTH - RUN_WIDTH * RATIO * 5/3);
		runner.setY(RUN_HEIGHT * RATIO * 2 /3);
				
		Image2 textImg = new Image2(new TextureRegion(loadtext));
		textImg.setX(TebakNusantara.SCREEN_WIDTH - loadtext.getWidth()*RATIO*10/9);
		textImg.setY(loadtext.getHeight()*RATIO);
		stage.addActor(textImg);
		
		stage.addActor(runner);
		
		//game.getSoundManager().clearCache();
		
		Gdx.app.log("LOADING", "end of kesgar");
		
		switch (src)
		{
		case MENU :
		case START :
			game.getAssetManager().unload("menuscreen/menuscreen.txt");
			break;
		case TEBAKJAMAK :
			game.getAssetManager().unload("scorescreen/highscoredialog.txt");
			game.getAssetManager().unload("scorescreen/scoredialog.txt");
			game.getAssetManager().unload("katalogscreen/makanan.txt");
			game.getAssetManager().unload("katalogscreen/tarian.txt");
										//game.getAssetManager().load("", TextureAtlas.class);
			break;
		case TOUCHMAP :
			game.getAssetManager().unload("scorescreen/highscoredialog.txt");
			game.getAssetManager().unload("scorescreen/scoredialog.txt");
			//game.getAssetManager().load("", TextureAtlas.class);
			break;
		case KATALOG :
			game.getAssetManager().unload("katalogscreen/katalogscreen.txt");
			game.getAssetManager().unload("katalogscreen/makanan.txt");
			game.getAssetManager().unload("katalogscreen/tarian.txt");
			break;
		}
		
		Gdx.app.log("LOADING", "before cleaning");
		game.clearAssetManager();
		Gdx.app.log("LOADING", "after cleaning");
		
		
		isDelaying = true;
		stage.addAction(Actions.sequence(
        		Actions.alpha(0f),
        		Actions.fadeIn(1f), Actions.delay(1.0f),
        		new RunnableAction(){
					@Override
					public void run()
					{						
						isDelaying = false;
												
						switch (dest)
						{
						case MENU :
						case START :
							game.getAssetManager().load("menuscreen/menuscreen.txt", TextureAtlas.class);
							break;
						case TEBAKJAMAK :
							game.getAssetManager().load("scorescreen/highscoredialog.txt", TextureAtlas.class);
							game.getAssetManager().load("scorescreen/scoredialog.txt", TextureAtlas.class);

game.getAssetManager().load("katalogscreen/makanan.txt", TextureAtlas.class);
							game.getAssetManager().load("katalogscreen/tarian.txt", TextureAtlas.class);
														//game.getAssetManager().load("", TextureAtlas.class);
							break;
						case TOUCHMAP :
							game.getAssetManager().load("scorescreen/highscoredialog.txt", TextureAtlas.class);
							game.getAssetManager().load("scorescreen/scoredialog.txt", TextureAtlas.class);
							//game.getAssetManager().load("", TextureAtlas.class);
							break;
						case KATALOG :
							game.getAssetManager().load("katalogscreen/katalogscreen.txt", TextureAtlas.class);
							game.getAssetManager().load("katalogscreen/makanan.txt", TextureAtlas.class);
							game.getAssetManager().load("katalogscreen/tarian.txt", TextureAtlas.class);
							break;
						}
						
					}
        		})); 
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		
		if (game.getMusicManager().isPlaying())
		{
			game.getMusicManager().fadeOut(delta * 0.3f);
		}
		
		if (game.getAssetManager().update())
		{
			if (!isDelaying)
			{
				stage.addAction(Actions.sequence(Actions.fadeOut(1f),
						new RunnableAction(){
					@Override
					public void run()
					{						
						switch (dest)
						{
						case MENU :
							game.setScreen(game.getMenuScreen());
							break;
						case START :
							game.setScreen(game.getStartScreen());
							break;
						case TEBAKJAMAK :
							game.setScreen(game.getTebakJamakScreen());
							break;
						case TOUCHMAP :
							game.setScreen(game.getTouchMapScreen());
							break;
						case KATALOG :
							game.setScreen(game.getKatalogScreen());
							break;
						}
					}
        		}));
			}
		}
		
		stage.act();
		stage.draw();
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
				
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void semidispose() {
		// TODO Auto-generated method stub

		Gdx.app.log("LOADING", "disposing load");
		
		atlas.dispose();
		loadtext.dispose();
		titleFont.dispose();
		contentFont.dispose();	
	}
}


