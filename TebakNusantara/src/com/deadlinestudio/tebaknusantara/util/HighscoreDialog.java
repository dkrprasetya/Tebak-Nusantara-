package com.deadlinestudio.tebaknusantara.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.services.SoundManager.TebakNusantaraSound;

public class HighscoreDialog extends Stage{

	private final int GAME_ID_PULAU = 0;
	private final int GAME_ID_BUDAYA = 1;
	
	private TextureAtlas atlas;
	private TebakNusantara game;
	private TextField field;
	
	private boolean clicked;
	
	private Player p;
	
	public HighscoreDialog(final TebakNusantara game, Player p, int game_id, BitmapFont font) {
		// TODO Auto-generated constructor stub
		super();
		
		this.p = p;
		this.game = game;
		
	//	game.getSoundManager().play(TebakNusantaraSound.YAY);
		
		addAction(Actions.alpha(0.0f));
		
		atlas = game.getAssetManager().get("scorescreen/highscoredialog.txt", TextureAtlas.class);
		
		Image2 box = new Image2(atlas.findRegion("boxhs"));
		box.setX((game.SCREEN_WIDTH - box.getWidth())/2);
		box.setY((game.SCREEN_HEIGHT - box.getHeight())/2 + 100.0f * game.RATIO);
		
		
		Image2 space = new Image2(atlas.findRegion("spacehs"));
		space.setX(box.getWidth() / 3+ box.getX() - 10f * game.RATIO);
		space.setY((box.getHeight()-space.getHeight())/2 + box.getY() - 8f * game.RATIO); 
				
		TextFieldStyle fld_style = new TextFieldStyle();
		//fld_style.background = new SpriteDrawable(spaceregion);
		fld_style.font = font;
		fld_style.fontColor = new Color(99f/255f,50f/255f,19f/255f,1f);
		
		field = new TextField("",fld_style);
		field.setMaxLength(4);
		field.setBlinkTime(0.5f);
		//field.getOnscreenKeyboard().show(true);
		field.setTextFieldListener(new TextFieldListener() {
			
			@Override
			public void keyTyped(TextField field, char key) {
				// TODO Auto-generated method stub
				if (key == '\n') field.getOnscreenKeyboard().show(false);
			}
		});
		field.setX(space.getX()+ 50f * game.RATIO);
		field.setY((box.getHeight()-space.getHeight())/2 + box.getY() - 8f * game.RATIO);
		
		ButtonStyle btn_style = new ButtonStyle();
		btn_style.up = new TextureRegionDrawable(atlas.findRegion("selesaihs"));
		btn_style.down = btn_style.up;
		
		Button2 selesai = new Button2(btn_style);
		selesai.setX((game.SCREEN_WIDTH - selesai.getWidth())/2 );
		selesai.setY((box.getY() * 2 - selesai.getHeight())/2 );
		
		clicked = false;
		selesai.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				game.getSoundManager().play(TebakNusantaraSound.TAP);
				field.getOnscreenKeyboard().show(false);
				clicked = true;
			}
		});
		
		addActor(box);
		addActor(space);
		addActor(field);
		addActor(selesai);
		
		addAction(Actions.fadeIn(1f));
				
		Gdx.input.setInputProcessor(this);
	}
	
	public boolean isClicked()
	{
		if (p.getName().length() == 0)
			p.setName(":3");
		else
			p.setName(field.getText());
		return clicked;
	}
}
