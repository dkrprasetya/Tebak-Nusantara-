package com.deadlinestudio.tebaknusantara.tebakbudaya;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class DialogNinePatch implements InputProcessor{
	private static NinePatch instance;
	private Array<CharSequence> text;
	private int activeLine = 0;
	private ClickListener listener;
	private float availableHeight;
	private float availableWidth;
	private float textTotalHeight;
	private int maxLine;
	private int totalLine;
	private int charPerLine;
	private int charPadding;
	private int counter;
	
	private BitmapFont font;
	private CharSequence str;
	private float x;
	private float y;
	private float width;
	private float height;
	private boolean visible = true;
	
	private Array<Button> buttonList;
	private Array<Slider> sliderList;
	private Array<Actor> actorList;
	
	private float xFadeInRate = 0.2f;
	private float yFadeInRate = 0.2f;
	private float tempHeight;
	private float tempY;
	private float tempWidth;
	private float tempX;
	private boolean xFading = true;
	private boolean yFading = true;
	
	private boolean drawWindow = true;
	private Sprite background;
	private float backAlpha = 0f;
	
	public DialogNinePatch(BitmapFont font,CharSequence str,float x, float y, float width,float height){
		instance = getNinePatchInstance();
		text = new Array<CharSequence>();
		buttonList = new Array<Button>();
		sliderList = new Array<Slider>();
		actorList = new Array<Actor>();
		this.font = font;
		this.str = str;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		//yFadeInRate =( (float) (height/width)) * (xFadeInRate);
		calculateBound();		
	}
	
	public static NinePatch getNinePatchInstance(){
		if (instance == null){
			//instance = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("data/menuSkin.png")),24,24), 8, 8, 8, 8);
			Texture r = new Texture(Gdx.files.internal("data/bar8.png"));
			r.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			instance = new NinePatch(new TextureRegion(r,62,23), 62/3, 62/3, 23/3, 23/3);
		}
		return instance;
	}
	
	private void calculateBound(){
		availableHeight = height-instance.getPadTop()-instance.getPadBottom();
		availableWidth = width-instance.getPadLeft()-instance.getPadRight();
		textTotalHeight = font.getWrappedBounds(str, availableWidth, font.getWrappedBounds(str, availableWidth)).height;
		maxLine = (int) (availableHeight/font.getCapHeight()) - 1;
		if (maxLine == 0) maxLine = 1;
		totalLine = (int) (textTotalHeight / font.getCapHeight());
		charPerLine = (font.computeVisibleGlyphs(str, 0, str.length(), availableWidth)) - 1; //cari pendekatan yang lebih baik!
		if (charPerLine < 0) charPerLine = 0;
		charPadding = charPerLine*maxLine;
		counter = totalLine > maxLine ? totalLine/maxLine : 1;
		for (int i = 0; i < text.size; i++) {
			text.removeIndex(i);
		}
		for (int i=0;i < counter;i++){
			text.add(str.subSequence(i*charPadding, (i*charPadding + charPadding) > str.length() ? str.length() : (i*charPadding + charPadding)));
			
		}
		
		if (yFading) {
			tempHeight = 0;
			tempY = y+(height/2);
		} else {
			tempHeight = height;
			tempY = y;
		}
		if (xFading){
			tempWidth = 0;
			tempX = x+(width/2);
		} else {
			tempWidth = width;
			tempX = x;
		}
		
		if (background != null){
			backAlpha = 0f;
			background.setBounds(tempX, y, tempWidth, tempHeight);
		}
		
	}
	
	public ClickListener getListener(){
		if (listener == null){
			listener = new ClickListener(){
				@Override
				public void clicked(InputEvent e, float x, float y){
					if (activeLine < text.size-1) activeLine++;
					else activeLine = 0;
				}
			};
		}
		return listener;
	}
	
	public static void draw(SpriteBatch batch,float x,float y, float width,float height){
		getNinePatchInstance().draw(batch, x, y, width, height);
	}
	
	public void drawDialog(SpriteBatch batch){
		if (visible) {
			update();
			if (drawWindow) draw(batch, tempX, tempY, tempWidth, tempHeight);
			else if (background != null){
				background.draw(batch,backAlpha);
				if (backAlpha < 1) backAlpha += 0.1f;
			}
			if (text.size > 0 && tempHeight == height && tempWidth == width) font.drawWrapped(batch,
					text.get(activeLine), x+instance.getPadLeft(), y+instance.getPadBottom()+availableHeight, availableWidth);
			if (buttonList.size > 0){
				for (Button b : buttonList) {
					b.draw(batch, 1f);
				}
			}
			if (sliderList.size > 0){
				for (Slider b : sliderList) {
					b.draw(batch, 1f);
				}
			}
			if (actorList.size > 0){
				for (Actor a : actorList){
					a.draw(batch, 1f);
				}
			}
		}
		
		
	}
	
	public void update(){
		if (tempHeight < height){
			tempHeight += yFadeInRate*height;
			tempY -= (yFadeInRate*height)/2;
		}
		else{
			tempHeight = height;
			tempY = y;
		}
		if (tempWidth < width){
			tempWidth += xFadeInRate*width;
			tempX -= (xFadeInRate*width)/2;
		}
		else{
			tempWidth = width;
			tempX = x;
		}
	}
	
	public void show(){
		visible = true;
		calculateBound();
	}
	
	public void hide(){
		visible = false;
	}
	
	public void setFadeInDirection(boolean xFading,boolean yFading){
		this.xFading = xFading;
		this.yFading = yFading;
	}
	
	public void setXFadeInRate(float rate){
		xFadeInRate = rate;
	}
	
	public void setYFadeInRate(float rate){
		yFadeInRate = rate;
	}
	
	public void setText(CharSequence str){
		this.str = str;
		calculateBound();
	}
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
		calculateBound();
	}
	
	public void setSize(float width,float height){
		this.width = width;
		this.height = height;
		calculateBound();
	}
	
	public void setBackground(Sprite sprite){
		background = sprite;
		calculateBound();
	}
	
	public void setDrawWindow(boolean draw){
		drawWindow = draw;
	}
	
	public void setFont(BitmapFont font){
		this.font = font;
		calculateBound();
	}
	
	public void button(Button button) {
		//button.getStyle().
		button.setWidth(availableWidth);
		//button.getStyle().up.
		button.setPosition(x+instance.getPadLeft(),(y+height-button.getHeight()) - (buttonList.size* (button.getHeight())));
		if (button instanceof TextButton) ((TextButton) button).getLabel().setColor(Color.RED);//((TextButton) button).setText("lalalal"+button.getOriginX()+" "+y);
		buttonList.add(button);
	}
	
	public void slider(Slider slider){
		if (slider.getWidth() > availableWidth) slider.setWidth(availableWidth);
		slider.setPosition(x+instance.getPadLeft(),(y+height-slider.getHeight()) - (sliderList.size* (slider.getHeight())));
		sliderList.add(slider);
	}
	
	public void actor(Actor actor){
		if (actor.getWidth() > availableWidth) actor.setWidth(availableWidth);
		actor.setPosition(x+instance.getPadLeft(),(y+height-actor.getHeight()) - (actorList.size* (actor.getHeight())));
		actorList.add(actor);
	}
	
	public Actor getActor(int index){
		return actorList.get(index);
	}
	
	public void addActorToStage(int index, Stage stage){
		stage.addActor(actorList.get(index));
	}
	
	public void addAllActor(Stage stage){
		for (int i = 0; i <actorList.size;i++){
			stage.addActor(actorList.get(i));
		}
	}
	
	
	public void addSliderToStage(int index, Stage stage){
		stage.addActor(sliderList.get(index));
	}
	
	public void addAllSlider(Stage stage){
		for (int i = 0; i <sliderList.size;i++){
			stage.addActor(sliderList.get(i));
		}
	}
	
	public void addButtonToStage(int index, Stage stage){
		stage.addActor(buttonList.get(index));
	}
	
	public void addAllButtonToStage(Stage stage){
		for (int i = 0; i <buttonList.size;i++){
			stage.addActor(buttonList.get(i));
		}
	}
	

	public void removeActorFromStage(int index, Stage stage){
		stage.getActors().removeValue(actorList.get(index), true);
	}
	
	public void removeAllActorFromStage(Stage stage){
		for (int i = 0; i <actorList.size;i++){
			stage.getActors().removeValue(actorList.get(i), true);
		}
	}
	
	
	public void removeSliderFromStage(int index, Stage stage){
		stage.getActors().removeValue(sliderList.get(index), true);
	}
	
	public void removeAllSliderFromStage(Stage stage){
		for (int i = 0; i <sliderList.size;i++){
			stage.getActors().removeValue(sliderList.get(i), true);
		}
	}
	
	public void removeButtonFromStage(int index, Stage stage){
		stage.getActors().removeValue(buttonList.get(index), true);
	}
	
	public void removeAllButtonFromStage(Stage stage){
		for (int i = 0; i <buttonList.size;i++){
			stage.getActors().removeValue(buttonList.get(i), true);
		}
	}
	
	public void prepareStage(Stage stage){
		for (int i = 0; i < stage.getActors().size; i++){
			stage.getActors().get(i).setTouchable(Touchable.disabled);
			
		}
	}
	
	public void relieveStage(Stage stage){
		for (int i = 0; i < stage.getActors().size;i++){
			stage.getActors().get(i).setTouchable(Touchable.enabled);
		}
	}
	
	public void dispose(){
		font.dispose();
		if (background != null) background.getTexture().dispose();
	}
	
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (activeLine < text.size) activeLine++;
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
