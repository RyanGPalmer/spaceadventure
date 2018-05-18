package vc.engine;

import java.io.Serializable;

public class GameSettings implements Serializable {
	private static final int DEFAULT_SCREEN_X = 640;
	private static final int DEFAULT_SCREEN_Y = 480;
	private static final int DEFAULT_QUALITY = 5;

	public int screenX;
	public int screenY;
	public int quality;

	public GameSettings(int screenX, int screenY, int quality) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.quality = quality;
	}

	public static GameSettings getDefault() {
		return new GameSettings(DEFAULT_SCREEN_X, DEFAULT_SCREEN_Y, DEFAULT_QUALITY);
	}
}
