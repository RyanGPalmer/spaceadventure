package vicinity.opengl;

import org.lwjgl.stb.STBEasyFont;

import java.nio.ByteBuffer;

public final class GLConsole {
	private static final float X = 0.5f;
	private static final float Y = 0.5f;

	private GLConsole() {
	}

	public static void print(String text) {
		STBEasyFont.stb_easy_font_print(X, Y, text, null, ByteBuffer.allocate(2000));
	}
}
