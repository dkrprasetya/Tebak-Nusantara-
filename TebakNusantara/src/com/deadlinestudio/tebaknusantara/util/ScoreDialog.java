package com.deadlinestudio.tebaknusantara.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.screens.LoadingScreen.DESTINATION;
import com.deadlinestudio.tebaknusantara.services.SoundManager.TebakNusantaraSound;

public class ScoreDialog extends Stage{

	private final int GAME_ID_PULAU = 0;
	private final int GAME_ID_BUDAYA = 1;
	
	private TextureAtlas atlas;
	private TebakNusantara game;
	private Player p;
	private BitmapFont font;
	private TextBounds bounds;
	private Image2 kotaknilai;
	
	public ScoreDialog(final TebakNusantara game, Player p, final int game_id, BitmapFont font) {
		// TODO Auto-generated constructor stub
		super();
		
		this.game = game;
		this.font = font;
		this.p = p;
		
		addAction(Actions.alpha(0.0f));
		
		//if (p.getName() == "-")
			//game.getSoundManager().play(TebakNusantaraSound.FAIL);
		
		atlas = game.getAssetManager().get("scorescreen/scoredialog.txt", TextureAtlas.class);
		
		Image2 box = new Image2(atlas.findRegion("box"));
		box.setX((game.SCREEN_WIDTH - box.getWidth())/2);
		box.setY((game.SCREEN_HEIGHT - box.getHeight())/2);
		
		kotaknilai = new Image2(atlas.findRegion("kotaknilai"));
		kotaknilai.setX((game.SCREEN_WIDTH - kotaknilai.getWidth())/2);
		kotaknilai.setY((game.SCREEN_HEIGHT)/2);
				
		Image2 title;
		if (p.getName() == "-")
			title = new Image2(atlas.findRegion("oops"));
		else
			title = new Image2(atlas.findRegion("selamat"));
		
		title.setX((game.SCREEN_WIDTH - title.getWidth())/2);
		title.setY((box.getHeight() * 3 /4  - title.getHeight())/2 + game.SCREEN_HEIGHT/2);
		
		ButtonStyle btn_style = new ButtonStyle();
		btn_style.up = new TextureRegionDrawable(atlas.findRegion("retry"));
		btn_style.down = btn_style.up;
		
		Button2 retry = new Button2(btn_style);
		retry.setX(game.SCREEN_WIDTH / 2 - 50 * game.RATIO - retry.getWidth());
		retry.setY((box.getHeight()/2 - retry.getHeight())/2 + box.getY());
		retry.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				if (game_id == 0)
					game.setScreen(game.getLoadingScreen(DESTINATION.TOUCHMAP, DESTINATION.TOUCHMAP));
				else
					game.setScreen(game.getLoadingScreen(DESTINATION.TEBAKJAMAK, DESTINATION.TEBAKJAMAK));
			}
		});
		
		btn_style = new ButtonStyle();
		btn_style.up = new TextureRegionDrawable(atlas.findRegion("next"));
		btn_style.down = btn_style.up;
		
		Button2 next = new Button2(btn_style);
		next.setX(game.SCREEN_WIDTH / 2 + 50 * game.RATIO);
		next.setY((box.getHeight()/2 - next.getHeight())/2 + box.getY());
		next.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				
				if (game_id == 0)
					game.setScreen(game.getLoadingScreen(DESTINATION.MENU, DESTINATION.TOUCHMAP));
				else
					game.setScreen(game.getLoadingScreen(DESTINATION.MENU, DESTINATION.TEBAKJAMAK));
			}
		});
		
		bounds = font.getBounds(""+p.getScore());
		
		addActor(box);
		addActor(title);
		addActor(kotaknilai);
		addActor(retry);
		addActor(next);
		
		addAction(Actions.fadeIn(1f));
		
		font.setColor(1f,1f,1f,1f);
		
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub
		super.draw();
		
		SpriteBatch batch = getSpriteBatch();
		
		batch.begin();
		font.draw(batch, "" + p.getScore(), 
			(TebakNusantara.SCREEN_WIDTH - bounds.width)/2, 
			(kotaknilai.getY()*2+kotaknilai.getHeight()*7/3-bounds.height)/2);
		
		batch.end();
	}
	
}
