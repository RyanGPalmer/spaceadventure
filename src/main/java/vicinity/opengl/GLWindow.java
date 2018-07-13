package vicinity.opengl;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import vicinity.opengl.GLInput;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLWindow {
	private long id;
	private int width;
	private int height;
	private final int majorVersion;
	private final int minorVersion;

	public GLWindow(int majorVersion, int minorVersion) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
	}

	public boolean init(String title, int x, int y, boolean fullscreen, boolean vsync) {
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, majorVersion);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, minorVersion);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		width = x;
		height = y;
		id = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
		if (!isValid()) return false;

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(id, pWidth, pHeight);
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(id, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);
		}

		glfwMakeContextCurrent(id);
		glfwSwapInterval(vsync ? 1 : 0);
		glfwSetKeyCallback(id, this::handleKeyCallback);
		return true;
	}

	private boolean isValid() {
		return id != NULL;
	}

	public void show() {
		glfwShowWindow(id);
	}

	public void setSize(int x, int y) {
		width = x;
		height = y;
		glfwSetWindowSize(id, width, height);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getAspectRatio() {
		return (float) width / (float) height;
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(id);
	}

	public void swapBuffers() {
		glfwSwapBuffers(id);
	}

	public void destroy() {
		if (isValid()) {
			glfwFreeCallbacks(id);
			glfwDestroyWindow(id);
		}
		int i = GLFW_KEY_W;
	}

	private final void handleKeyCallback(long window, int key, int scancode, int action, int mods) {
		GLInput.handleInput(this, key, action);
	}

	public void close() {
		glfwSetWindowShouldClose(id, true);
	}
}
