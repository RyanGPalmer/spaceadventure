package vicinity.opengl.buffers;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

public class GLVertexBufferObject extends GLBufferObject {
	public GLVertexBufferObject() {
		super(GL_ARRAY_BUFFER);
	}

	public void bufferStatic(float[] data) {
		bufferStaticFloat(data);
	}
}