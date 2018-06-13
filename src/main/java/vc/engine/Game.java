package vc.engine;

import vc.engine.util.DataSerializer;
import vc.engine.util.TickTimer;

public abstract class Game {
	private static final String MAIN_THREAD = "SYSTEM";
	private static final String SETTINGS_FILE_PATH = "settings.dat";
	public static final GameObjectManager GAME_OBJECT_MANAGER = new GameObjectManager();

	private final TickTimer timer;
	private final OpenGL gl;
	private final Renderer renderer;

	protected Game(final String title) {
		Thread.currentThread().setName(MAIN_THREAD);
		Log.startLogWorker();
		GameSettings settings = new DataSerializer<GameSettings>(SETTINGS_FILE_PATH).load();
		Log.info(settings != null ? "Settings loaded." : "Settings not loaded. New settings generated.");
		if (settings == null) settings = GameSettings.getDefault();
		timer = new TickTimer(settings.tickRate);
		gl = new OpenGL(settings, title);
		renderer = new Renderer(gl);
	}

	protected final void start() {
		if (init()) {
			awake();
			loop();
		} else Log.error("Initialization failed. Game will now exit.");
		clean();
	}

	protected abstract void awake();

	private final boolean init() {
		if (gl.init()) return renderer.init();
		else return false;
	}

	private final void loop() {
		timer.init();
		while (!gl.shouldClose()) {
			timer.update();
			if (timer.shouldTick()) tick(timer.onTick());
			renderer.render();
		}
	}

	protected void tick(final double delta) {
		GAME_OBJECT_MANAGER.tick(delta);
	}

	private final void clean() {
		if (timer != null) timer.stop();
		if (GAME_OBJECT_MANAGER != null) GAME_OBJECT_MANAGER.clean();
		if (renderer != null) renderer.close();
		if (gl != null) {
			gl.close();
			if (new DataSerializer<>(SETTINGS_FILE_PATH).save(gl.getSettings())) Log.info("Settings saved.");
			else Log.warn("Settings not saved.");
		}
		Log.stopLogWorker();
	}
}
