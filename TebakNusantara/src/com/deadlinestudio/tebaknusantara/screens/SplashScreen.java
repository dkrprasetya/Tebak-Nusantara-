package com.deadlinestudio.tebaknusantara.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.deadlinestudio.tebaknusantara.TebakNusantara;

public class SplashScreen extends AbstractScreen
{

	Texture splashTexture;
	
	public SplashScreen(final TebakNusantara game) {
		super(game);
		// TODO Auto-generated constructor stub

		stage.clear();
		
		splashTexture = new Texture(Gdx.files.internal("splash.png"));
		
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion splashRegion = new TextureRegion(splashTexture,  
				0,0,800,480);
		
		final Image splashImage = new Image(new TextureRegionDrawable(splashRegion),
				Scaling.stretch);
		
		splashImage.setFillParent(true);
		splashImage.getColor().a = 0f;
		
		splashImage.addAction(Actions.sequence(Actions.fadeIn(0.75f),
				Actions.delay(1.25f),
				Actions.fadeOut(1.25f),
				new RunnableAction(){
					@Override
					public void run()
					{						
						Gdx.app.log("Splash", "udahan");
						Gdx.app.log("Splash", "alpha : " + splashImage.getColor().a);
						
						game.setScreen(game.getStartScreen());
					}
		}));
		Gdx.app.log("Splash", "mulai");
		stage.addActor(splashImage);
		
		stage.addListener(new ClickListener()
		{
			@Override
        	public void clicked(InputEvent event, float x, float y)
        	{
				game.setScreen(game.getStartScreen());
        	}
		});
		
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void render(float delta)
	{
		super.render(delta);
		stage.act();
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
		
	}

	@Override
	public void semidispose() {
		// TODO Auto-generated method stub
		splashTexture.dispose();
	}

}
