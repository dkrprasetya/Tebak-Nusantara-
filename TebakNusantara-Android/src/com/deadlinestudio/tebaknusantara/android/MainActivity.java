package com.deadlinestudio.tebaknusantara.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.deadlinestudio.tebaknusantara.TebakNusantara;

public class MainActivity extends AndroidApplication {

	public void onCreate (android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(new TebakNusantara(), false);
	}
}
