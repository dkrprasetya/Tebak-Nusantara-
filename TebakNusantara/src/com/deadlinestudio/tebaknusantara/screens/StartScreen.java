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
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.deadlinestudio.tebaknusantara.BackKeyHandler;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.screens.LoadingScreen.DESTINATION;
import com.deadlinestudio.tebaknusantara.services.MusicManager;
import com.deadlinestudio.tebaknusantara.services.MusicManager.TebakNusantaraMusic;
import com.deadlinestudio.tebaknusantara.services.SoundManager.TebakNusantaraSound;
import com.deadlinestudio.tebaknusantara.util.BGPattern;
import com.deadlinestudio.tebaknusantara.util.Button2;
import com.deadlinestudio.tebaknusantara.util.CountdownDrawer;
import com.deadlinestudio.tebaknusantara.util.Image2;
import com.deadlinestudio.tebaknusantara.util.Point;


public class StartScreen extends AbstractScreen
{
    private BGPattern background;
    private TextureRegion logoRegion;
    private TextureRegion startRegion;
    private TextureAtlas atlas;
    private Dialog confirmDialog;
    
    public StartScreen(final TebakNusantara game )
    {
        super(game);
        
        /* Init music */
        if (!game.getMusicManager().isPlaying())
        	game.getMusicManager().play(TebakNusantaraMusic.MENU); 
                
        /* images */
        background = new BGPattern();
        background.setColor(BGPattern.COLOR.YELLOW);
        
        stage.addActor(background);
        
        atlas = game.getAssetManager().get("menuscreen/menuscreen.txt", TextureAtlas.class);
        
        logoRegion = atlas.findRegion("logo");
		
		Image2 logoImage = new Image2(new TextureRegionDrawable(logoRegion));
		
		logoImage.setX((SCREEN_WIDTH - logoImage.getWidth())/2);
		logoImage.setY(80 * RATIO);
		stage.addActor(logoImage);
        
		//startTexture = new Texture(Gdx.files.internal("startscreen/start.png"));
		        
		ButtonStyle btn_style;

        btn_style = new ButtonStyle();
        
        btn_style.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("mulai")));
        btn_style.down = btn_style.up;        
        
        Button2 start = new Button2(btn_style);
        
        start.setX((SCREEN_WIDTH - start.getWidth())/2);
        start.setY(80.0f * RATIO);
                
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
						
						game.setScreen(game.getMenuScreen());
					}
        		}));
        		
        		
        	}
        });
        
        start.setWiggling(true);
        //start.setFloating(true);
        stage.addActor(start);
        
        /* Init texture atlas */
                
        stage.addAction(Actions.sequence(
        		Actions.alpha(0f),
        		Actions.fadeIn(0.25f)
        		));        
                                
        Gdx.input.setInputProcessor(stage);
        buildConfirmDialog();
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
		stage.addAction(Actions.sequence(Actions.delay(0.05f),
				new RunnableAction(){
			@Override
			public void run()
			{						
				confirmDialog.show(stage);
				//Gdx.app.exit();
			}
		}));
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
				//game.setScreen(game.getLoadingScreen(DESTINATION.MENU));
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				game.dispose();
				dispose();
				Gdx.app.exit();
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
				//pauseClicked();
				confirmDialog.hide();
				game.getSoundManager().play(TebakNusantaraSound.TAP);
			}
		});
		
		
		Sprite confirmSprite = new Sprite(game.getPilihanAtlas().findRegion("keluar"));
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
		background.dispose();
	}
}
