package vicinity;

import static org.lwjgl.opengl.GL15.*;

public class ElementBufferObject {
	private final int id;

	public ElementBufferObject() {
		id = glGenBuffers();
	}

	public void bind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
	}

	public void bufferStatic(int[] data) {
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}

	public void unbind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void delete() {
		unbind();
		glDeleteBuffers(id);
	}
}
