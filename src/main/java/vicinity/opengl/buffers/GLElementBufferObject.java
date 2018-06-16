package vicinity.opengl.buffers;

import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

public class GLElementBufferObject extends GLBufferObject {
	public GLElementBufferObject() {
		super(GL_ELEMENT_ARRAY_BUFFER);
	}

	public void bufferStatic(int[] data) {
		bufferStaticInt(data);
	}
}