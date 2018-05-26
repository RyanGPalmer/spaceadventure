package vc.engine;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArrayObject {
	private final int id;

	private VertexArrayObject() {
		id = glGenVertexArrays();
		bind();
	}

	public static VertexArrayObject create() {
		return new VertexArrayObject();
	}

	public void bind() {
		glBindVertexArray(id);
	}

	public void delete() {
		glDeleteVertexArrays(id);
	}

	public int getID() {
		return id;
	}
}
