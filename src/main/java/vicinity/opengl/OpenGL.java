package vicinity.opengl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import vicinity.GameSettings;
import vicinity.Log;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGL {
	private static final int OPEN_GL_MAJOR_VERSION = 3;
	private static final int OPEN_GL_MINOR_VERSION = 3;

	private final GameSettings settings;

	private String title;
	private long window;

	public OpenGL(GameSettings settings, String title) {
		this.settings = settings;
		this.title = title;
	}

	public boolean init() {
		glfwSetErrorCallback(this::handleErrorCallback);
		if (!glfwInit()) throw new IllegalStateException("Unable to initialize OpenGL");
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, OPEN_GL_MAJOR_VERSION);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, OPEN_GL_MINOR_VERSION);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		window = glfwCreateWindow(settings.screenX, settings.screenY, title, settings.fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);

		if (window == NULL) {
			Log.error("Failed to create the OpenGL window.");
			return false;
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
		Log.info("OpenGL initialized. Version: " + glGetString(GL_VERSION));
		return true;
	}

	private final void handleKeyCallback(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);
		if (key == GLFW_KEY_0 && action == GLFW_RELEASE) setScreenSize(getScreenX() + 20, getScreenY() + 20);
		if (key == GLFW_KEY_9 && action == GLFW_RELEASE) setScreenSize(getScreenX() - 20, getScreenY() - 20);
	}

	private final void handleErrorCallback(int error, long description) {
		String desc = GLFWErrorCallback.getDescription(description);
		Log.error("GLFW (" + error + "): " + desc, new Exception("GLFWException"));
	}

	public void swapAndPoll() {
		glfwSwapBuffers(window); // Necessary because of double-buffering
		glfwPollEvents(); // Can do glfwWaitEvents() to wait for input instead
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}

	public void close() {
		if (window != NULL) {
			glfwFreeCallbacks(window);
			glfwDestroyWindow(window);
		}
		glfwTerminate();
		Log.info("OpenGL terminated.");
	}

	public void setScreenSize(int x, int y) {
		settings.screenX = x;
		settings.screenY = y;
		glfwSetWindowSize(window, x, y);
	}

	public int getScreenX() {
		return settings.screenX;
	}

	public int getScreenY() {
		return settings.screenY;
	}

	public GameSettings getSettings() {
		return settings;
	}
}
