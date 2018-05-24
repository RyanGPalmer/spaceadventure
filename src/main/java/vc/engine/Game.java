package vc.engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import vc.engine.util.DataSerializer;
import vc.engine.util.FileUtils;
import vc.engine.util.TickTimer;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class Game {
	private static final String MAIN_THREAD = "SYSTEM";
	private static final String SETTINGS_FILE_PATH = "settings.dat";
	private static final String SHADERS_FILE_PATH = "res/shaders/";
	private static final String SHADERS_EXT = ".shader";
	private static final String SHADERS_LEGACY_SUFFIX = "_legacy";
	private static final String SHADERS_VERTEX = "vert";
	private static final String SHADERS_FRAGMENT = "frag";
	public static final GameObjectManager GAME_OBJECT_MANAGER = new GameObjectManager();

	private final String title;
	private final GameSettings settings;
	private final TickTimer timer;

	private long window;
	private int vao;
	private boolean legacy = false;

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
		clean();
	}

	protected abstract void awake();

	private final void init() {
		glfwSetErrorCallback(this::handleErrorCallback);
		if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		//glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE); // UNCOMMENT TO TEST LEGACY SETTINGS
		window = glfwCreateWindow(settings.screenX, settings.screenY, title, NULL, NULL);

		if (window == NULL) {
			Log.info("Falling back to legacy OpenGL context.");
			legacy = true;
			glfwDefaultWindowHints();
			glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
			window = glfwCreateWindow(settings.screenX, settings.screenY, title, NULL, NULL);
			if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwSetKeyCallback(window, this::handleKeyCallback);

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);
		}

		glfwMakeContextCurrent(window);
		glfwSwapInterval(settings.vsync ? 1 : 0); // V-sync
		glfwShowWindow(window);
		GL.createCapabilities();
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		Log.info("OpenGL initialized. Version: " + glGetString(GL_VERSION));
	}

	private final void loop() {
		timer.init();
		while (!glfwWindowShouldClose(window)) {
			timer.update();
			if (timer.shouldTick()) tick(timer.onTick());
			render0();
		}
	}

	private final void render0() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		if (legacy) renderLegacy();
		else render();
		glfwSwapBuffers(window); // Necessary because of double-buffering
		glfwPollEvents(); // Can do glfwWaitEvents() to wait for input
	}

	private final void render() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer vertices = stack.mallocFloat(3 * 6);
			vertices.put(-0.6f).put(-0.4f).put(0f).put(1f).put(0f).put(0f);
			vertices.put(0.6f).put(-0.4f).put(0f).put(0f).put(1f).put(0f);
			vertices.put(0f).put(0.6f).put(0f).put(0f).put(0f).put(1f);
			vertices.flip();

			int vbo = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		}

		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, loadShaderSource(SHADERS_VERTEX));
		glCompileShader(vertexShader);

		int compileStatus = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
		if (compileStatus != GL_TRUE) Log.error("Failed to compile vertex shader.", new RuntimeException());

		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, loadShaderSource(SHADERS_FRAGMENT));
		glCompileShader(fragmentShader);

		compileStatus = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
		if (compileStatus != GL_TRUE) Log.error("Failed to compile fragment shader.", new RuntimeException());
	}

	private final void renderLegacy() {
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
	}

	protected void tick(final double delta) {
		GAME_OBJECT_MANAGER.tick(delta);
	}

	private final void handleKeyCallback(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);
	}

	private final void handleErrorCallback(int error, long description) {
		String desc = GLFWErrorCallback.getDescription(description);
		Log.error("GLFW (" + error + "): " + desc, new Exception("GLFWException"));
	}

	private final void clean() {
		GAME_OBJECT_MANAGER.clean();
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		Log.info("OpenGL terminated.");
		if (new DataSerializer<>(SETTINGS_FILE_PATH).save(settings)) Log.info("Settings saved.");
		else Log.warn("Settings not saved.");
		Log.stopLogWorker();
	}

	private final String loadShaderSource(String name) {
		String path = SHADERS_FILE_PATH + name;
		if (legacy) path += SHADERS_LEGACY_SUFFIX;
		path += SHADERS_EXT;
		return FileUtils.read(path);
	}
}
