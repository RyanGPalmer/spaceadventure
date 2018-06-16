package vicinity.opengl.buffers;

import vicinity.opengl.GLObject;

import static org.lwjgl.opengl.GL15.*;

public abstract class GLBufferObject extends GLObject {
	private final int type;

	protected GLBufferObject(int type) {
		super(glGenBuffers());
		this.type = type;
	}

	public final void bind() {
		glBindBuffer(type, id);
	}

	protected final void bufferStaticFloat(float[] data) {
		glBufferData(type, data, GL_STATIC_DRAW);
	}

	protected final void bufferStaticInt(int[] data) {
		glBufferData(type, data, GL_STATIC_DRAW);
	}

	public final void unbind() {
		glBindBuffer(type, 0);
	}

	public final void delete() {
		glDeleteBuffers(id);
	}
}
