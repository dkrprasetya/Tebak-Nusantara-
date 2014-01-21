
package com.deadlinestudio.tebaknusantara.tebakbudaya;

//TODO:
//edit positioning and scaling!

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deadlinestudio.tebaknusantara.TebakNusantara;
import com.deadlinestudio.tebaknusantara.screens.TebakJamakScreen;
import com.deadlinestudio.tebaknusantara.util.BGPattern;
import com.deadlinestudio.tebaknusantara.util.BGPattern.COLOR;

public class TebakJamakRenderer {
	//CONFIGURATION
	
	private int widthInUnit = 10;
	
	private int heightInUnit = 7;
	
	//END OF CONFIGURATION. do not edit out of this region unless you know what you're doing
	
	
	
	private TebakJamakScreen screen;
	private TebakanJamak problem;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private DialogNinePatch dialog;
	
	private Sprite sprite;
	private Image problemSprite;
	private int width = Gdx.graphics.getWidth();
	private int height = Gdx.graphics.getHeight();
	private float scaleX;
	private float scaleY;
	
	private float alpha = 0f;
	
	private boolean flagScore = false;
	private String scoreText = "";
	private int endDraw = 1;
	private int delay = 2;
	
	private boolean flagCombo = false;
	private String comboText= "";
	private int delayCombo = 30;
	
	
	private float posX;
	private Sprite bar;
	
	//debug
	String text = "Debug";
	private boolean debug = false;
	private int solved = 0;
	
	//tambahan dikra
	
	private BGPattern background;
	
	private TextureAtlas makananAtlas;
	private TextureAtlas tarianAtlas;
	
	//END OF CLASS' PROPERTIES DEFINITION
	
	public TebakJamakRenderer(TebakJamakScreen screen) {
		this.screen = screen;
		init();	
	}
			
	public void render(float delta){
		batch.begin();
			drawActingBackground(delta);
			drawBoards();
			drawStopwatchTime();
			drawProblemSprite(delta);
			drawScore();
			drawDebug();
			drawProblemDesc();
			drawBonus();
			drawCombo();
		batch.end();
		increaseSpriteAlpha();
	
	}
	
	
	
	
	//Private methods
	private void init(){
		camera = new OrthographicCamera(widthInUnit,heightInUnit);
		camera.position.set((float) widthInUnit/2,(float) heightInUnit/2,0);
		camera.update();
		
		batch = screen.getBatch();
		font = screen.getFont();
		dialog = new DialogNinePatch(font, "" , 0.2f*width, 0.82f*height, 0.57f*width, 0.14f*height);
		
		scaleX = (float) Gdx.graphics.getWidth() / 800f;
		scaleY = (float) Gdx.graphics.getHeight() / 480f;
		Texture r = new Texture(Gdx.files.internal("data/BAR.png"));
		r.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		bar = new Sprite(new TextureRegion (r,800,84));
		bar.setSize(bar.getWidth()*scaleX, bar.getHeight()*scaleY);
		bar.setPosition(0 , 0.8f*height);
		
		background = new BGPattern();
		background.setColor(COLOR.RED);
		
		screen.getStage().addListener(dialog.getListener());
		
		makananAtlas = screen.getGame().getAssetManager().get("katalogscreen/makanan.txt", TextureAtlas.class);
		tarianAtlas = screen.getGame().getAssetManager().get("katalogscreen/tarian.txt", TextureAtlas.class);
		
		
		initProblemSprite();
	}
	
	private void initProblemSprite(){
		problemSprite = new Image();
		problemSprite.setSize(0.6f*Gdx.graphics.getWidth(),0.7f*Gdx.graphics.getHeight());
		
		RotateToAction ro = new RotateToAction();
		ro.setDuration(2f);
		ro.setRotation(5);
		//ro.setInterpolation(Interpolation.bounceOut);
		//ro.setInterpolation(Interpolation.circleOut);
		ro.setInterpolation(Interpolation.swing);
		
		RotateToAction rot = new RotateToAction();
		rot.setDuration(2f);
		rot.setRotation(-5);
		//rot.setInterpolation(Interpolation.bounceOut);
		//rot.setInterpolation(Interpolation.circleOut);
		rot.setInterpolation(Interpolation.swing);
		
		
		RepeatAction rep = new RepeatAction();
		SequenceAction seq = new SequenceAction();
		seq.addAction(rot);
		seq.addAction(ro);
		rep.setAction(seq);
		rep.setCount(RepeatAction.FOREVER);
		
		problemSprite.addAction(rep);
	}
	
	
	//Drawing private methods
	private void drawBoards(){
		bar.draw(batch);
		DialogNinePatch.draw(batch, 0.78f*width, 0.01f*height, 0.15f*width, 0.07f*height); //stopwatch
	}
	
	private void drawStopwatchTime(){
		font.setColor(Color.WHITE);
		font.draw(batch,screen.getStopwatch().toString(),0.8f*width,0.06f*height);
		
	}
	
	private void drawProblemDesc(){
		posX = font.getBounds(problem.getDesc()).width;
		font.draw(batch, problem.getDesc(), (bar.getWidth()/2)-(posX/2), 0.94f*height);
	}
	
	private void drawScore(){
		posX = font.getBounds("Skor: "+screen.getScore()).width;
		font.draw(batch, "Skor: "+screen.getScore(), (bar.getWidth()/2)-(posX/2),0.86f*height);
	}
	
	private void drawDebug(){
		//if (debug) font.draw(batch,text,0.005f*width,0.2f*height);
		if (debug) font.draw(batch,"FPS : " + Gdx.graphics.getFramesPerSecond() +"Soal ke : " + solved,0.1f*width,0.9f*height);
	}
	
	private void drawProblemSprite(float delta){
		sprite.draw(batch,alpha);
		problemSprite.act(delta);
		sprite.setRotation(problemSprite.getRotation());
		//if (problemSprite.getDrawable() != null) problemSprite.draw(batch, alpha);
		
	}
	
	private void drawActingBackground(float delta){
		background.act(delta);
		background.draw(batch, 1f);
	}
	
	private void drawBonus(){
		if (flagScore) {
			float scaleX = font.getScaleX();
			float scaleY = font.getScaleY();
			font.draw(batch, scoreText, 0.58f*width, 0.86f*height, 0, endDraw > scoreText.length() ? scoreText.length() : endDraw);
			font.setScale(scaleX, scaleY);
			delay--;
			if (delay == 0){
				delay = 2;
				endDraw++;
			}
			if (endDraw == scoreText.length() + delay + 8) {
				flagScore = false;
				endDraw = 1;
			}
		}
	}
	
	private void drawCombo(){
		if (flagCombo){
			font.drawMultiLine(batch, "COMBO!\n" + comboText, 0.7f*width, 0.905f*height, 10, HAlignment.CENTER);
			delayCombo -= 1;
			if (delayCombo == 0){
				flagCombo = false;
				delayCombo = 30;
			}
			
		}
	}
	
	private void increaseSpriteAlpha(){
		if (alpha < 1f) alpha += 0.1f;
		else alpha = 1f;
	}
	
	
	
	//Public methods
	public void setProblem(TebakanJamak problem){
		this.problem = problem;
		//Texture r = new Texture(Gdx.files.internal("data/soal-"+problem.getId()+".png"));
		//r.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		//sprite = new Sprite(new TextureRegion(r, 0, 0, 348, 256));
		Gdx.app.log("", problem.getAbout()+problem.getDesc());
		
		Gdx.app.log("Makanan", makananAtlas.findRegion(problem.getAbout())+"");
		Gdx.app.log("Tarian", tarianAtlas.findRegion(problem.getAbout())+"");
		
		/*
		sprite = new Sprite(makananAtlas.findRegion(problem.getAbout()));
		if (sprite == null) sprite = new Sprite(tarianAtlas.findRegion(problem.getAbout()));
		*/
		if (makananAtlas.findRegion(problem.getAbout()) == null){
			sprite = new Sprite(tarianAtlas.findRegion(problem.getAbout()));
		} else if (tarianAtlas.findRegion(problem.getAbout()) == null) {
			sprite = new Sprite(makananAtlas.findRegion(problem.getAbout()));
		}
		
		
		//sprite.setSize(0.6f*Gdx.graphics.getWidth(),0.7f*Gdx.graphics.getHeight());
		sprite.setScale(TebakNusantara.RATIO);
		Gdx.app.log("WIDTH", sprite.getWidth()+"");
		Gdx.app.log("HEIGHT", sprite.getHeight()+"");
		//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		//sprite.setPosition(0.15f*Gdx.graphics.getWidth(), 0.14f*Gdx.graphics.getHeight());
		sprite.setPosition(((width-sprite.getWidth())/2)-width*0.17f, ((height-sprite.getHeight())/2)-height*0.1f);
		alpha = 0f;
		dialog.setText(problem.getDesc());
		
		problemSprite.setDrawable(new SpriteDrawable(sprite));
		
		solved++;
	}
	
	public TebakanJamak getProblem(){
		return problem;
	}
	
	public void addScoreAnimation(int score){
		flagScore = true;
		scoreText = "+" + score;
	}
	
	public void comboAnimation(int counter){
		flagCombo = true;
		comboText = "+ " + 50 + "x" + counter;
	}
	
		
	public void dispose(){
		//screen.dispose();
		background.dispose();
		sprite.getTexture().dispose();
		bar.getTexture().dispose();
		dialog.dispose();
		
		batch.dispose();
		font.dispose();
	}

	//debugging
	public void setText(String Text){
		text = Text + delayCombo;
	}
	
	
}
