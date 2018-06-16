package vicinity.opengl.buffers;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class GLVertexBufferObject extends GLBufferObject {
	public GLVertexBufferObject() {
		super(GL_ARRAY_BUFFER);
	}

	public void bufferStatic(float[] data) {
		bufferStaticFloat(data);
	}

	public void point(int location, int start, int stride) {
		bind();
		glVertexAttribPointer(location, 3, GL_FLOAT, false, stride * 4, start * 4);
		glEnableVertexAttribArray(location);
		unbind();
	}
}