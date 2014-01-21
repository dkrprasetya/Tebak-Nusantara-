package com.deadlinestudio.tebaknusantara.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AddAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deadlinestudio.tebaknusantara.TebakNusantara;

public class CountdownDrawer extends Stage {
	
	private Sprite region[];
	private Image img;
	private boolean finished;
	
	public CountdownDrawer(Texture reg, final TebakNusantara game)
	{
		region = new Sprite[3];
		
		for (int i = 0; i < 3; ++i)
		{
			region[i] = new Sprite(new TextureRegion(reg,i*reg.getWidth()/3,0,reg.getWidth()/3, reg.getHeight()));
			region[i].setBounds(0, 0, reg.getWidth()/3f * TebakNusantara.RATIO * 0.7f, reg.getHeight() * TebakNusantara.RATIO * 0.7f);
			
		}
		
		finished = false;
				
		img = new Image(new SpriteDrawable(region[0]));
		img.setOrigin(img.getWidth()/2, img.getHeight()/2);
		img.setX((TebakNusantara.SCREEN_WIDTH - img.getWidth())/2);
		img.setY((TebakNusantara.SCREEN_HEIGHT - img.getHeight())/2);

		addActor(img);
		
		img.addAction(Actions.sequence(Actions.alpha(1f),Actions.parallel(Actions.scaleBy(0.5f, 0.5f, 1.2f),
						Actions.fadeOut(1f), Actions.sequence(Actions.delay(0.8f),
						new RunnableAction(){
			public void run() {
				// TODO Auto-generated method stub
						
				img = new Image(new SpriteDrawable(region[1]));
				img.setOrigin(img.getWidth()/2, img.getHeight()/2);
				img.setX((TebakNusantara.SCREEN_WIDTH - img.getWidth())/2);
				img.setY((TebakNusantara.SCREEN_HEIGHT - img.getHeight())/2);

				addActor(img);
				
				img.addAction(Actions.sequence(Actions.alpha(1f),Actions.parallel(Actions.scaleBy(0.5f, 0.5f, 1.2f),
						Actions.fadeOut(1f), Actions.sequence(Actions.delay(0.8f),
								new RunnableAction(){
					public void run() {
						// TODO Auto-generated method stub
						
						img = new Image(new SpriteDrawable(region[2]));
						img.setOrigin(img.getWidth()/2, img.getHeight()/2);
						img.setX((TebakNusantara.SCREEN_WIDTH - img.getWidth())/2);
						img.setY((TebakNusantara.SCREEN_HEIGHT - img.getHeight())/2);

						addActor(img);
						
						img.addAction(Actions.sequence(Actions.alpha(1f),Actions.parallel(Actions.scaleBy(0.5f, 0.5f, 1.2f),
										Actions.fadeOut(1f)),
										new RunnableAction(){
							public void run() {
								// TODO Auto-generated method stub
								
								finished = true;
																
							}
						}));					
					}
				}))));			
				
			}
		}))));
		
	}
		
	public boolean isFinished()
	{
		return finished;
	}
}

