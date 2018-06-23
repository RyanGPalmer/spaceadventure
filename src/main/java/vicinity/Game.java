package vicinity;

import vicinity.opengl.rendering.GLRenderer;
import vicinity.opengl.OpenGL;
import vicinity.util.DataSerializer;
import vicinity.util.TickTimer;

public abstract class Game {
	private static final String MAIN_THREAD = "SYSTEM";
	private static final String SETTINGS_FILE_PATH = "settings.dat";
	public static final GameObjectManager GAME_OBJECT_MANAGER = new GameObjectManager();

	private final TickTimer timer;
	private final OpenGL gl;
	private final GLRenderer renderer;
	private boolean glGracefulShutdown = false;

	protected Game(final String title) {
		Thread.currentThread().setName(MAIN_THREAD);
		Log.info("SYSTEM STARTUP");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown(), "ShutdownHook"));
		Log.start();
		GameSettings settings = new DataSerializer<GameSettings>(SETTINGS_FILE_PATH).load();
		Log.info(settings != null ? "Settings loaded." : "Settings not loaded. New settings generated.");
		if (settings == null) settings = GameSettings.getDefault();
		timer = new TickTimer(settings.tickRate);
		gl = new OpenGL(settings, title);
		renderer = new GLRenderer(gl.getWindow());
	}

	protected final void start() {
		if (init()) {
			awake();
			loop();
			glShutdown();
		} else Log.error("Initialization failed. Game will now exit.");
		System.exit(0);
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
			gl.pollEvents();
		}
	}

	protected void tick(final double delta) {
		GAME_OBJECT_MANAGER.tick(delta);
	}

	private void glShutdown() {
		if (renderer != null) renderer.close();
		if (gl != null) gl.close();
		glGracefulShutdown = true;
	}

	private void clean() {
		if (timer != null) timer.stop();
		GAME_OBJECT_MANAGER.clean();
	}

	private void saveSettings() {
		if (gl != null) {
			if (new DataSerializer<>(SETTINGS_FILE_PATH).save(gl.getSettings())) Log.info("Settings saved.");
			else Log.warn("Settings not saved.");
		}
	}

	private void shutdown() {
		clean();
		saveSettings();
		if (!glGracefulShutdown) Log.warn("OpenGL couldn't terminate gracefully.");
		Log.info("SYSTEM SHUTDOWN");
		Log.stop();
	}
}
