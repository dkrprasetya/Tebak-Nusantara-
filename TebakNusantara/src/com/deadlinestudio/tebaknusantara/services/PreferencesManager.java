package com.deadlinestudio.tebaknusantara.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.deadlinestudio.tebaknusantara.util.Player;

/**
 * Handles the game preferences.
 */
public class PreferencesManager
{
    // constants
    private static final String PREF_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_SCORE = "score";
    private static final String PREF_NAME = "name";
    private static final String PREFS_NAME = "tebaknusantara";
	private static final Player[][] Player = null;
    
	private Preferences preferences = null;
    
    public PreferencesManager()
    {
    }

    protected Preferences getPrefs()
    {
    	if (preferences == null)
    		preferences = Gdx.app.getPreferences( PREFS_NAME ); 
        return preferences;
    }

    public boolean isSoundEnabled()
    {
        return getPrefs().getBoolean( PREF_SOUND_ENABLED, true );
    }

    public void setSoundEnabled(
        boolean soundEffectsEnabled )
    {
        getPrefs().putBoolean( PREF_SOUND_ENABLED, soundEffectsEnabled );
        getPrefs().flush();
    }

    public boolean isMusicEnabled()
    {
        return getPrefs().getBoolean( PREF_MUSIC_ENABLED, true );
    }

    public void setMusicEnabled(
        boolean musicEnabled )
    {
        getPrefs().putBoolean( PREF_MUSIC_ENABLED, musicEnabled );
        getPrefs().flush();
    }

    public float getVolume()
    {
        return getPrefs().getFloat( PREF_VOLUME, 0.5f );
    }

    public void setVolume(
        float volume )
    {
        getPrefs().putFloat( PREF_VOLUME, volume );
        getPrefs().flush();
    }
    
    public void writeScores(Player[][] scoreboard)
    {
    	for (int game_id = 0; game_id < 2; ++game_id)
    		for (int i = 0; i < 7; ++i)
    		{
    			Player p = scoreboard[i][game_id];
    			getPrefs().putString(game_id + PREF_NAME + i, p.getName());
    			getPrefs().flush();
    			getPrefs().putInteger(game_id + PREF_SCORE + i, p.getScore());
    			getPrefs().flush();
    		}
    }
    
    public Player[][] getScoreboard()
    {
    	Player[][] scoreboard = new Player[7][2];
    	for (int game_id = 0; game_id < 2; ++game_id)
    		for (int i = 0; i < 7; ++i)
    		{    			
    			scoreboard[i][game_id] = new Player(getPrefs().getString(game_id + PREF_NAME + i, "-"), getPrefs().getInteger(game_id + PREF_SCORE + i, 0));
    		}
    	
    	return scoreboard;
    }
}

