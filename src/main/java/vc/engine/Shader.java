package vc.engine;

import vc.engine.util.FileUtils;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
	private static final String SHADERS_FILE_PATH = "res/shaders/";
	private static final String SHADERS_EXT = ".shader";
	private static final String SHADERS_LEGACY_SUFFIX = "_legacy";
	private static final String SHADERS_VERTEX = "vert";
	private static final String SHADERS_FRAGMENT = "frag";

	private final int id;

	private Shader(int type, boolean legacy) {
		id = glCreateShader(type);
		if (!compile(load(type, legacy))) Log.error("Failed to compile shader.", new RuntimeException());
	}

	public static Shader create(int type, boolean legacy) {
		return new Shader(type, legacy);
	}

	private String load(int type, boolean legacy) {
		String name;
		switch (type) {
			case GL_VERTEX_SHADER:
				name = SHADERS_VERTEX;
				break;
			case GL_FRAGMENT_SHADER:
				name = SHADERS_FRAGMENT;
				break;
			default:
				return null;
		}

		String path = SHADERS_FILE_PATH + name;
		if (legacy) path += SHADERS_LEGACY_SUFFIX;
		path += SHADERS_EXT;
		return FileUtils.read(path);
	}

	private boolean compile(String source) {
		if (source == null) return false;
		glShaderSource(id, source);
		glCompileShader(id);
		return glGetShaderi(id, GL_COMPILE_STATUS) == GL_TRUE;
	}

	public void delete() {
		glDeleteShader(id);
	}

	public int getID() {
		return id;
	}
}
