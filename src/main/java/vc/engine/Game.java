package vc.engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import vc.engine.util.DataSerializer;
import vc.engine.util.TickTimer;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class Game {
	private static final String MAIN_THREAD = "SYSTEM";
	private static final String SETTINGS_FILE_PATH = "settings.dat";
	public static final GameObjectManager GAME_OBJECT_MANAGER = new GameObjectManager();

	private final String title;
	private final GameSettings settings;
	private final TickTimer timer;

	private long window; // The window handle
	private boolean running = false;

	protected Game(final String title) {
		Thread.currentThread().setName(MAIN_THREAD);
		Log.startLogWorker();
		this.title = title;
		GameSettings settings = new DataSerializer<GameSettings>(SETTINGS_FILE_PATH).load();
		this.settings = settings != null ? settings : GameSettings.getDefault();
		Log.info(settings != null ? "Settings loaded." : "Settings not loaded. New settings generated.");
		timer = new TickTimer(this.settings.tickRate);
	}

	protected final void start() {
		init();
		awake();
		loop();
		cleanup();
	}

	protected abstract void awake();

	private final void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(settings.screenX, settings.screenY, title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scanCode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
					window,
					(vidMode.width() - pWidth.get(0)) / 2,
					(vidMode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(settings.vsync ? 1 : 0);

		// Make the window visible
		glfwShowWindow(window);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	private final void loop() {
		running = true;
		timer.init();

		while (running) {
			timer.update();
			if (timer.shouldTick()) tick(timer.onTick());
			render();
			running = !glfwWindowShouldClose(window);
		}
	}

	private final void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-1, -1);
		glTexCoord2f(0, 1);
		glVertex2f(-1, 1);
		glTexCoord2f(1, 1);
		glVertex2f(1, 1);
		glTexCoord2f(1, 0);
		glVertex2f(1, -1);
		glEnd();

		glfwSwapBuffers(window); // swap the color buffers
		glfwPollEvents(); // Poll for window events. The key callback above will only be invoked during this call.
	}

	protected void tick(final double delta) {
		GAME_OBJECT_MANAGER.tick(delta);
	}

	private final void cleanup() {
		GAME_OBJECT_MANAGER.destroyAll();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();

		// Save settings and stop log worker
		new DataSerializer<>(SETTINGS_FILE_PATH).save(settings);
		Log.stopLogWorker();
	}
}
