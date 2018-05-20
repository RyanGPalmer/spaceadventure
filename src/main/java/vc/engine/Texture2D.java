package vc.engine;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.stb.STBImage.*;

public class Texture2D {
	private int id;
	private int width;
	private int height;

	public Texture2D() {
		id = glGenTextures();
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void setParameter(int name, int value) {
		glTexParameteri(GL_TEXTURE_2D, name, value);
	}

	public void uploadData(int width, int height, ByteBuffer data) {
		uploadData(GL_RGBA8, width, height, GL_RGBA, data);
	}

	public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
	}

	public void delete() {
		glDeleteTextures(id);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (width > 0) this.width = width;
		else Log.error("Tried to set invalid texture width: " + width);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if (height > 0) this.height = height;
		else Log.error("Tried to set invalid texture height: " + height);
	}

	public static Texture2D create(int width, int height, ByteBuffer data) {
		Texture2D texture = new Texture2D();
		texture.setWidth(width);
		texture.setHeight(height);
		texture.bind();

		texture.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		texture.setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		texture.uploadData(width, height, data);

		return texture;
	}

	public static Texture2D load(String path) {
		ByteBuffer img;
		int width, height;

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			stbi_set_flip_vertically_on_load(true);
			img = stbi_load(path, w, h, comp, 4);
			if (img == null) {
				Log.error("Failed to load a texture: " + path);
				Log.exception(new RuntimeException(stbi_failure_reason()));
			}

			width = w.get();
			height = h.get();
		}

		return create(width, height, img);
	}
}
