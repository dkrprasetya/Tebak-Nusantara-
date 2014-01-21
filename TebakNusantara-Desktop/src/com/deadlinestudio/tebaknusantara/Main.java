package com.deadlinestudio.tebaknusantara;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Tebak Nusantara";
		cfg.useGL20 = false;
		
		boolean large = true;
		cfg.width = large ? 1280 : 480;
		cfg.height = large ? 768 : 320;
        cfg.resizable = false;
		
        new LwjglApplication(new TebakNusantara(), cfg);
	}
}
