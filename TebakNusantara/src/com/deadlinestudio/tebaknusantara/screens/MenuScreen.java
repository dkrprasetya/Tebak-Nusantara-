package com.deadlinestudio.tebaknusantara.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deadlinestudio.tebaknusantara.BackKeyHandler;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.screens.LoadingScreen.DESTINATION;
import com.deadlinestudio.tebaknusantara.services.MusicManager.TebakNusantaraMusic;
import com.deadlinestudio.tebaknusantara.services.SoundManager.TebakNusantaraSound;
import com.deadlinestudio.tebaknusantara.util.BGPattern;
import com.deadlinestudio.tebaknusantara.util.BGPattern.COLOR;
import com.deadlinestudio.tebaknusantara.util.Button2;
import com.deadlinestudio.tebaknusantara.util.HighscoreDrawer;
import com.deadlinestudio.tebaknusantara.util.Image2;
import com.deadlinestudio.tebaknusantara.util.Point;


public class MenuScreen extends AbstractScreen
{
    private BGPattern background;
    private TextureAtlas atlas;
    private Image papanpulau;
    private Image papanbudaya;
    
	
    
    public int activeboard;
        
    public MenuScreen(final TebakNusantara game )
    {
        super(game);
        
        if (!game.getMusicManager().isPlaying())
        {
        	game.getMusicManager().play(TebakNusantaraMusic.MENU);
        }
        
        background = new BGPattern();
        background.setColor(COLOR.GREEN);
        stage.addActor(background);
                
        atlas = game.getAssetManager().get("menuscreen/menuscreen.txt", TextureAtlas.class);
        
        ButtonStyle btn_style;

        btn_style = new ButtonStyle();
        
        btn_style.up = new TextureRegionDrawable(atlas.findRegion("pulau"));
        btn_style.down = btn_style.up;
        
        final Button2 peta = new Button2(btn_style);
        peta.setX((TebakNusantara.SCREEN_WIDTH/2 - peta.getWidth())/2);
        peta.setY(80 * RATIO);
        		
        peta.setFloating(true);
        
        btn_style = new ButtonStyle();
        
        btn_style.up = new TextureRegionDrawable(atlas.findRegion("budaya"));
        btn_style.down = btn_style.up;
        
        final Button2 budaya = new Button2(btn_style);
        budaya.setX((TebakNusantara.SCREEN_WIDTH - budaya.getWidth())/2);
        budaya.setY(80 * RATIO);
        budaya.addListener(new ClickListener(){
        	@Override
        	public void clicked(InputEvent event, float x, float y)
        	{
        		game.getSoundManager().play(TebakNusantaraSound.TAP);
        		
        		stage.addAction(Actions.sequence(Actions.delay(0.05f),
        				new RunnableAction(){
					@Override
					public void run()
					{						
						activeboard = 1;
						
						papanbudaya.setVisible(true);
						papanpulau.setVisible(false);
						peta.setFloating(false);
						budaya.setFloating(true);
						//game.setScreen(game.getLoadingScreen(DESTINATION.TEBAKJAMAK));
					}
        		}));
        		
        	}
        });
        
        peta.addListener(new ClickListener(){
        	@Override
        	public void clicked(InputEvent event, float x, float y)
        	{
        		game.getSoundManager().play(TebakNusantaraSound.TAP);
        		        		
        		stage.addAction(Actions.sequence(Actions.delay(0.05f),
        				new RunnableAction(){
					@Override
					public void run()
					{						
						
						activeboard = 0;
						
						papanbudaya.setVisible(false);
						papanpulau.setVisible(true); 
						peta.setFloating(true);
						budaya.setFloating(false);
						//game.setScreen(game.getLoadingScreen(DESTINATION.TOUCHMAP));
					}
        		}));
        		
        	}
        });
        
        btn_style = new ButtonStyle();
        
        btn_style.up = new TextureRegionDrawable(atlas.findRegion("katalog"));
        btn_style.down = btn_style.up;
        
        Button2 katalog = new Button2(btn_style);
        
        Gdx.app.log("katalog", "ukuran : " + katalog.getWidth() + ", " + katalog.getHeight());
        
        katalog.setX((SCREEN_WIDTH * 3/2 - katalog.getWidth())/2);
        katalog.setY(80 * RATIO);
        katalog.addListener(new ClickListener(){
        	@Override
        	public void clicked(InputEvent event, float x, float y)
        	{
        		game.getSoundManager().play(TebakNusantaraSound.TAP);
        		
        		stage.addAction(Actions.sequence(Actions.delay(0.05f),
        				new RunnableAction(){
					@Override
					public void run()
					{	
						game.setScreen(game.getLoadingScreen(DESTINATION.KATALOG,DESTINATION.MENU));
					}
        		}));
        		
        	}
        });
                
        Sprite spr = new Sprite(atlas.findRegion("papanpulau"));
        spr.setScale(Math.min(SCREEN_HEIGHT/spr.getHeight(), SCREEN_WIDTH/spr.getWidth()));
        papanpulau = new Image(new SpriteDrawable(spr));
        papanpulau.setX((SCREEN_WIDTH - papanpulau.getWidth())/2);
        papanpulau.setY((SCREEN_HEIGHT - papanpulau.getHeight())/2);
        //papanpulau.setY((SCREEN_HEIGHT * 6.0f/5.0f - papanpulau.getHeight())/2);
        
        Gdx.app.log("SIZE", papanpulau.getWidth() + " + " + papanpulau.getHeight());
        
        TextureRegion tr = atlas.findRegion("papanbudaya");
        
        Gdx.app.log("SIZE", tr.getRegionHeight() + " + " + tr.getRegionWidth());

        spr = new Sprite(atlas.findRegion("papanbudaya"));
        spr.setScale(Math.min(SCREEN_HEIGHT/spr.getHeight(), SCREEN_WIDTH/spr.getWidth()));
        papanbudaya = new Image(new SpriteDrawable(spr));
        papanbudaya.setX((SCREEN_WIDTH - papanbudaya.getWidth())/2);
        papanbudaya.setY((SCREEN_HEIGHT - papanbudaya.getHeight())/2);
        
        papanpulau.setVisible(true);
        papanbudaya.setVisible(false);
        stage.addActor(papanpulau);
        stage.addActor(papanbudaya);
        stage.addActor(peta);
        stage.addActor(budaya);
        stage.addActor(katalog);
        stage.addActor(new HighscoreDrawer(game.getScoreboardManager().getScoreboard(), this));
        
        btn_style = new ButtonStyle();
        
        spr = new Sprite(atlas.findRegion("main"));
        spr.setScale(0.80f);
        btn_style.up = new TextureRegionDrawable(spr);
        btn_style.down = btn_style.up;
        
        Button2 start = new Button2(btn_style);
        
        //Gdx.app.log("katalog", "ukuran : " + start.getWidth() + ", " + start.getHeight());
        
        start.setX((SCREEN_WIDTH - start.getWidth())/2);
        start.setY(SCREEN_HEIGHT / 3);
        start.addListener(new ClickListener(){
        	@Override
        	public void clicked(InputEvent event, float x, float y)
        	{
        		game.getSoundManager().play(TebakNusantaraSound.TAP);
        		
        		stage.addAction(Actions.sequence(Actions.delay(0.05f),
        				new RunnableAction(){
					@Override
					public void run()
					{	
						if (activeboard == 0)
							game.setScreen(game.getLoadingScreen(DESTINATION.TOUCHMAP, DESTINATION.MENU));
						else
							game.setScreen(game.getLoadingScreen(DESTINATION.TEBAKJAMAK, DESTINATION.MENU));
					}
        		}));
        		
        	}
        });
        
        start.setWiggling(true);
        start.setWiggleDuration(5.0f);
        
        stage.addActor(start);
        
        stage.addAction(Actions.sequence(
        		Actions.alpha(0f),
        		Actions.fadeIn(0.25f)
        		));        
                        
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(
        int width,
        int height )
    {
    	super.resize( width, height );
    }

    @Override
    public void render(
        float delta )
    {
        super.render( delta );

        // update stage
        stage.act();
        
        // draw stage
        stage.draw();
    }
    
    @Override
    public void dispose()
    {
    	super.dispose();
    	
    }

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		game.setScreen(game.getStartScreen());
	}

	@Override
	public void semidispose() {
		// TODO Auto-generated method stub
		background.dispose();
	}
	
}
