package vicinity.opengl;

import java.util.ArrayList;
import java.util.List;

public final class GLInput {
	// Actions
	public static final int RELEASE = 0;
	public static final int PRESS = 1;
	public static final int REPEAT = 2;

	// Keys
	public static final int KEY_SPACE = 32;
	public static final int KEY_LEFT_CTRL = 341;
	public static final int KEY_ESC = 256;
	public static final int KEY_W = 87;
	public static final int KEY_A = 65;
	public static final int KEY_S = 83;
	public static final int KEY_D = 68;

	private static final List<GLInputListener> listeners = new ArrayList<>();

	public static void handleInput(GLWindow window, int key, int action) {
		for (GLInputListener listener : listeners) listener.handleInput(window, key, action);
	}

	public static boolean addListener(GLInputListener listener) {
		return listeners.add(listener);
	}
}
