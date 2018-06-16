package vicinity.opengl.buffers;

import vicinity.opengl.GLBindable;
import vicinity.opengl.GLObject;

import static org.lwjgl.opengl.GL15.*;

public abstract class GLBufferObject extends GLObject implements GLBindable {
	private final int type;

	GLBufferObject(int type) {
		super(glGenBuffers());
		this.type = type;
	}

	public final void delete() {
		glDeleteBuffers(id);
	}

	public final void bind() {
		glBindBuffer(type, id);
	}

	public final void unbind() {
		glBindBuffer(type, 0);
	}

	final void bufferStaticFloat(float[] data) {
		bind();
		glBufferData(type, data, GL_STATIC_DRAW);
		unbind();
	}

	final void bufferStaticInt(int[] data) {
		bind();
		glBufferData(type, data, GL_STATIC_DRAW);
		unbind();
	}
}
