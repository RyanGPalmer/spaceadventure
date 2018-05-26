package vc.engine;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

public class VertexBufferObject {
	private final int id;

	private VertexBufferObject() {
		id = glGenBuffers();
		bind(GL_ARRAY_BUFFER);
	}

	public static VertexBufferObject create() {
		return new VertexBufferObject();
	}

	private void bind(int target) {
		glBindBuffer(target, id);
	}

	public void upload(FloatBuffer data) {
		upload(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	private void upload(int target, FloatBuffer data, int usage) {
		glBufferData(target, data, usage);
	}

	public void delete() {
		glDeleteBuffers(id);
	}
}
