package vc.engine;

import static org.lwjgl.opengl.GL15.*;

public class VertexBufferObject {
	private static final int FLOAT_SIZE_BYTES = 4;

	private final int id;

	public VertexBufferObject() {
		id = glGenBuffers();
	}

	public void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, id);
	}

	public void buffer(float[] vertices) {
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
	}

	public void delete() {
		glDeleteBuffers(id);
	}

	public int getID() {
		return id;
	}
}
