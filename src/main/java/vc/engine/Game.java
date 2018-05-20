package vc.engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class Game {
	private static final String MAIN_THREAD = "SYSTEM";

	public static final GameObjectManager gameObjectManager = new GameObjectManager();

	private final String title;
	private final GameSettings settings;

	private long window; // The window handle
	private boolean running;

	protected Game(final String title) {
		Thread.currentThread().setName(MAIN_THREAD);
		running = false;
		Log.startLogWorker();
		this.title = title;
		GameSettings settings = GameSettingsLoader.load();
		this.settings = settings != null ? settings : GameSettings.getDefault();
	}

	protected final void start() {
		init();
		awake();
		loop();
		beforeExit();
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
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
	}

	private final void loop() {
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		running = true;
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double deltaS = 0.0;
		double ns = 1000000000.0 / 60.0;
		int ticks = 0;

		float width = 1;
		float height = 1;
		int wSwitch = -1;
		int hSwitch = -1;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			deltaS += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1.0) {
				if (width >= 2) wSwitch = -1;
				else if (width <= 0.2f) wSwitch = 1;
				if (height >= 2) hSwitch = -1;
				else if (height <= 0.2f) hSwitch = 1;
				width += 0.01f * wSwitch;
				height += 0.01f * hSwitch;
				tick0();
				ticks++;
				delta--;
			}

			if (deltaS >= 60.0) {
				if (ticks < 30) Log.warn("Low tick rate detected: " + ticks);
				ticks = 0;
				deltaS = 0;
			}

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			render(width, height);
			glfwSwapBuffers(window); // swap the color buffers
			glfwPollEvents(); // Poll for window events. The key callback above will only be invoked during this call.

			if (glfwWindowShouldClose(window)) running = false;
		}
	}

	private final void render(float width, float height) {
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(width / 2f * -1f, height / 2f * -1f);
		glTexCoord2f(0, 1);
		glVertex2f(width / 2f * -1f, height / 2f);
		glTexCoord2f(1, 1);
		glVertex2f(width / 2f, height / 2f);
		glTexCoord2f(1, 0);
		glVertex2f(width / 2f, height / 2f * -1f);
		glEnd();
	}

	private final void tick0() {
		tick();
		gameObjectManager.tickAll();
	}

	protected abstract void tick();

	protected abstract void beforeExit();

	private final void cleanup() {
		gameObjectManager.destroyAll();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();

		// Save settings and stop log worker
		GameSettingsLoader.save(settings);
		Log.stopLogWorker();
	}
}
