package vicinity.opengl;

import static org.lwjgl.opengl.GL20.*;

public final class GLShaderProgram {
	private final int id;
	private final int[] shaders = new int[3]; // Room for a vertex, geometry, and fragment shader

	public GLShaderProgram() {
		id = glCreateProgram();
	}

	public void attachShader(GLShader shader) {
		int shaderID = shader.getID();
		glAttachShader(id, shaderID);
		shaders[shader.getType()] = shaderID;
	}

	public void linkAndDiscardShaders() {
		glLinkProgram(id);
		for (int s : shaders) glDeleteShader(s);
	}

	public void use() {
		glUseProgram(id);
	}

	public void delete() {
		glDeleteProgram(id);
	}
}
