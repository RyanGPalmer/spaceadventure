package vicinity.opengl;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * A vertex array object (also known as VAO) can be bound just like a vertex
 * buffer object and any subsequent vertex attribute calls from that point on
 * will be stored inside the VAO. This has the advantage that when configuring
 * vertex attribute pointers you only have to make those calls once and whenever
 * we want to draw the object, we can just bind the corresponding VAO.
 */
public class GLVertexArrayObject extends GLObject {
	public GLVertexArrayObject() {
		super(glGenVertexArrays());
	}

	public void bind() {
		glBindVertexArray(id);
	}

	public void unbind() {
		glBindVertexArray(0);
	}

	public void delete() {
		glDeleteVertexArrays(id);
	}
}
