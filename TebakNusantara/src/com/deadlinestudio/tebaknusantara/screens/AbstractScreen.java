package com.deadlinestudio.tebaknusantara.screens;

import java.awt.Font;
import java.util.List;

import org.omg.CORBA.FREE_MEM;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import com.deadlinestudio.tebaknusantara.TebakNusantara;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen
{
	protected final float SCREEN_WIDTH = TebakNusantara.SCREEN_WIDTH;
	protected final float SCREEN_HEIGHT = TebakNusantara.SCREEN_HEIGHT;
	protected final float RATIO;
	
	//CONFIGURATION
	
	private int widthInUnit = 10;
		
	private int heightInUnit = 7;
		
	//END OF CONFIGURATION. do not edit out of this region unless you know what you're doing
	
	private final int VIRTUAL_WIDTH = 800;
    private final int VIRTUAL_HEIGHT = 480;
    private final float ASPECT_RATIO = (float)VIRTUAL_WIDTH / (float)VIRTUAL_HEIGHT;
    
    private static Rectangle viewport;
	
	protected static FreeTypeFontGenerator fontGenerator = null;
    protected static TebakNusantara game = null;
    protected static SpriteBatch batch = null;
    protected OrthographicCamera camera = null;
    //protected static ShapeRenderer shapeRenderer = null;
    //protected static Stage stage =null;
    protected Stage stage;
    
    protected BitmapFont font;
    
    protected boolean backPressed;
    
    public AbstractScreen(
        TebakNusantara game )
    {
    	
        this.game = game;
        
        RATIO = game.RATIO;
        
        /*
        fontGenerator = new FreeTypeFontGenerator(
        		Gdx.files.internal("skin/KGSecondChancesSketch.ttf"));
        
        setFontSize(18);
        */
        
        if (this.batch == null)
        	this.batch = game.getSpriteBatch();

        ///////////////////////////////// DISINIIIIIIIIIIIIIIIIIIIIIII
        
        if (fontGenerator == null)
        	fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("skin/FredokaOne-Regular.ttf"));
        
        setFontSize(18);
        
      //  if (shapeRenderer == null)
       // 	this.shapeRenderer = new ShapeRenderer();
        //this.stage = new Stage(TebakNusantara.SCREEN_WIDTH, TebakNusantara.SCREEN_HEIGHT, true);
        if (stage == null)
        	this.stage = new Stage(TebakNusantara.SCREEN_WIDTH, TebakNusantara.SCREEN_HEIGHT, true,  this.batch);
        stage.clear();
        
        
        if (camera == null)
        {
	        camera = new OrthographicCamera(widthInUnit,heightInUnit);
			camera.position.set((float) widthInUnit/2,(float) heightInUnit/2,0);
			camera.update();
        }
		
        backPressed = false;
        /*
        VIRTUAL_WIDTH = TebakNusantara.SCREEN_WIDTH;
        VIRTUAL_HEIGHT = TebakNusantara.SCREEN_HEIGHT;
        ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;*/ 
        
		/*
        camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        camera.setToOrtho(false, TebakNusantara.SCREEN_WIDTH, TebakNusantara.SCREEN_HEIGHT);
        camera.position.set(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, 0);
        camera.update();
        */
    }

    public TebakNusantara getGame()
    {
    	return game;    	
    }
    
    protected String getName()
    {
        return getClass().getSimpleName();
    }

    protected Stage getStage()
    {
    	return stage;
    }
    
    // Font functions
    public void setFontSize(int size)
    {
    	//font.dispose();
    	font = fontGenerator.generateFont((int)(size * RATIO));
    }
    
    public BitmapFont getFont(int size)
    {
    	return fontGenerator.generateFont((int)(size * RATIO));
    }
    
    // Screen implementation

    @Override
    public void show()
    {
        Gdx.app.log( TebakNusantara.LOG, "Showing screen: " + getName() );
    }

    @Override
    public void resize(
        int width,
        int height )
    {  
        Gdx.app.log( TebakNusantara.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height );
        
        float aspectRatio = (float) height / (float) width; // swapped these
        camera = new OrthographicCamera(32f, 32f * aspectRatio);
        /*
        // calculate new viewport
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        
        if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }        
 
        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h); */ 
    }

    @Override
    public void render(
        float delta )
    {
    	// update camera
    	
    	
    	/*camera.update();
    	camera.apply(Gdx.gl10);
    	batch.setProjectionMatrix(camera.combined);
    	shapeRenderer.setProjectionMatrix(camera.combined);  */
    	
        // set viewport
    	//Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
        
    	
        // the following code clears the screen with the given RGB color (black)
        //Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Keys.BACK))
        {
        	backPressed = true;
        }
        
        if (backPressed)
        {
        	backPressed = false;
        	onBackKeyPressed();
        }
        // update and draw the stage actors
    }

    @Override
    public void hide()
    {
        Gdx.app.log( TebakNusantara.LOG, "Hiding screen: " + getName() );
    }

    @Override
    public void pause()
    {
        Gdx.app.log( TebakNusantara.LOG, "Pausing screen: " + getName() );
    }

    @Override
    public void resume()
    {
        Gdx.app.log( TebakNusantara.LOG, "Resuming screen: " + getName() );
    }

    @Override
    public void dispose()
    {
        Gdx.app.log( TebakNusantara.LOG, "Disposing screen: " + getName() );

        // dispose the collaborators
        if (stage != null)
        stage.dispose();
        stage = null;
        if (font != null)
        font.dispose();
        font = null;
        if (fontGenerator != null)
        fontGenerator.dispose();
        fontGenerator = null;
        if (batch != null)
        batch.dispose();
        batch = null;
     
        //semidispose();
    }
    
    public abstract void semidispose();
    
    public abstract void onBackKeyPressed();
}

