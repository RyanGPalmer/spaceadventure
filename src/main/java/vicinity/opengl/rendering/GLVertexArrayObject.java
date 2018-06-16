package vicinity.opengl.rendering;

import vicinity.opengl.GLBindable;
import vicinity.opengl.GLObject;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * A vertex array object (also known as VAO) can be bound just like a vertex
 * buffer object and any subsequent vertex attribute calls from that point on
 * will be stored inside the VAO. This has the advantage that when configuring
 * vertex attribute pointers you only have to make those calls once and whenever
 * we want to draw the object, we can just bind the corresponding VAO.
 * <p>
 * A vertex array object stores the following:
 * - Calls to glEnableVertexAttribArray or glDisableVertexAttribArray
 * - Vertex attribute configurations via glVertexAttribPointer
 * - Vertex buffer objects associated with vertex attributes by calls to glVertexAttribPointer
 */
public class GLVertexArrayObject extends GLObject implements GLBindable {
	public GLVertexArrayObject() {
		super(glGenVertexArrays());
	}

	public void delete() {
		glDeleteVertexArrays(id);
	}

	public void bind() {
		glBindVertexArray(id);
	}

	public void unbind() {
		glBindVertexArray(0);
	}

	public void drawElements() {
		bind();
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		unbind();
	}
}
