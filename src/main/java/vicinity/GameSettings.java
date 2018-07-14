package vicinity;

import java.io.Serializable;

public class GameSettings implements Serializable {
	private static final int DEFAULT_SCREEN_X = 640;
	private static final int DEFAULT_SCREEN_Y = 480;
	private static final int DEFAULT_QUALITY = 5;
	private static final int DEFAULT_TICK_RATE = 60;
	private static final boolean DEFAULT_VSYNC = true;
	private static final boolean DEFAULT_FULLSCREEN = false;

	public int screenX;
	public int screenY;
	public int quality;
	public int tickRate;
	public boolean vsync;
	public boolean fullscreen;

	public GameSettings(int screenX, int screenY, int quality, int tickRate, boolean vsync, boolean fullscreen) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.quality = quality;
		this.tickRate = tickRate;
		this.vsync = vsync;
		this.fullscreen = fullscreen;
	}

	public static GameSettings getDefault() {
		return new GameSettings(DEFAULT_SCREEN_X, DEFAULT_SCREEN_Y, DEFAULT_QUALITY, DEFAULT_TICK_RATE, DEFAULT_VSYNC, DEFAULT_FULLSCREEN);
	}
}
