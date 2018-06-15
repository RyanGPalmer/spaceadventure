package vicinity;

import static org.lwjgl.opengl.GL15.*;

public class VertexBufferObject {
	private final int id;

	public VertexBufferObject() {
		id = glGenBuffers();
	}

	public void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, id);
	}

	public void bufferStatic(float[] data) {
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public void unbind() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void delete() {
		unbind();
		glDeleteBuffers(id);
	}
}
