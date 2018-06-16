package vicinity.opengl.shaders;

import vicinity.opengl.GLObject;

import static org.lwjgl.opengl.GL20.*;

public final class GLShaderProgram extends GLObject {
	private final GLShader[] shaders = new GLShader[3]; // Room for a vertex, geometry, and fragment shader

	public GLShaderProgram() {
		super(glCreateProgram());
	}

	public void attachShader(GLShader shader) {
		glAttachShader(id, shader.getID());
		shaders[shader.getType()] = shader;
	}

	public void linkAndDiscardShaders() {
		glLinkProgram(id);
		for (GLShader s : shaders) if (s != null) s.delete();
	}

	public int getLocation(String attribute) {
		return glGetAttribLocation(id, attribute);
	}

	public void use() {
		glUseProgram(id);
	}

	public void delete() {
		glDeleteProgram(id);
	}
}
