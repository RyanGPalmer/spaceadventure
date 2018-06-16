package vicinity.opengl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import vicinity.GameSettings;
import vicinity.Log;
import vicinity.opengl.glfw.GLFWWindow;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;

public class OpenGL {
	private static final int OPEN_GL_MAJOR_VERSION = 3;
	private static final int OPEN_GL_MINOR_VERSION = 3;

	private final GameSettings settings;
	private final GLFWWindow window;
	private final String title;

	public OpenGL(GameSettings settings, String title) {
		this.settings = settings;
		this.title = title;
		window = new GLFWWindow(OPEN_GL_MAJOR_VERSION, OPEN_GL_MINOR_VERSION);
	}

	public boolean init() {
		glfwSetErrorCallback(this::handleErrorCallback);
		if (!glfwInit()) Log.error("Unable to initialize OpenGL", new IllegalStateException());

		if (!window.init(title, settings.screenX, settings.screenY, settings.fullscreen, settings.vsync)) {
			Log.error("Failed to create the OpenGL window.");
			return false;
		}

		try {
			GL.createCapabilities();
		} catch (Exception e) {
			Log.error("Failed to create OpenGL capabilities.", e);
			return false;
		}

		window.show();
		Log.info("OpenGL initialized. Version: " + glGetString(GL_VERSION));
		return true;
	}

	public void pollEvents() {
		glfwPollEvents();
	}

	private final void handleErrorCallback(int error, long description) {
		String desc = GLFWErrorCallback.getDescription(description);
		Log.error("GLFW (" + error + "): " + desc, new Exception("GLFWException"));
	}

	public boolean shouldClose() {
		return window.shouldClose();
	}

	public void close() {
		window.destroy();
		glfwTerminate();
		Log.info("OpenGL terminated.");
	}

	public GameSettings getSettings() {
		return settings;
	}

	public GLFWWindow getWindow() {
		return window;
	}
}
