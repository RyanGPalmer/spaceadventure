package vicinity.opengl;

import vicinity.Log;
import vicinity.opengl.buffers.GLElementBufferObject;
import vicinity.opengl.buffers.GLVertexBufferObject;
import vicinity.util.FileUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glClearBufferfv;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class GLRenderer {
	private static final float[] BLACK = {0.0f, 0.0f, 0.0f, 1.0f};
	private static final float[] WHITE = {1.0f, 1.0f, 1.0f, 1.0f};
	private static final float[] RED = {1.0f, 0.0f, 0.0f, 1.0f};
	private static final float[] GREEN = {0.0f, 1.0f, 0.0f, 1.0f};
	private static final float[] BLUE = {0.0f, 0.0f, 1.0f, 1.0f};

	private final OpenGL gl;
	private GLShaderProgram sp;

	private GLVertexArrayObject vao;
	private GLVertexBufferObject vbo;
	private GLElementBufferObject ebo;

	private static final float[] vertices = {
			0.6f, 0.6f, 0.0f,    // top right
			0.5f, -0.5f, 0.0f,    // bottom right
			-0.5f, -0.5f, 0.0f,    // bottom left
			-0.5f, 0.5f, 0.0f    // top left
	};
	private static final int[] indices = {
			0, 2, 3,    // first triangle
			1, 2, 3        // second triangle
	};

	public GLRenderer(OpenGL gl) {
		this.gl = gl;
	}

	public boolean init() {
		String vertShaderSrc = FileUtils.read(GLShader.DEFAULT_VERTEX_SHADER_PATH);
		String geoShaderSrc = FileUtils.read(GLShader.DEFAULT_GEOMETRY_SHADER_PATH);
		String fragShaderSrc = FileUtils.read(GLShader.DEFAULT_FRAGMENT_SHADER_PATH);

		if (vertShaderSrc == null || geoShaderSrc == null || fragShaderSrc == null) {
			Log.error("Failed to load one or more shader source files.");
			return false;
		}

		try {
			sp = new GLShaderProgram();
			GLShader vertShader = new GLShader(GL_VERTEX_SHADER, vertShaderSrc);
			GLShader geomShader = new GLShader(GL_GEOMETRY_SHADER, geoShaderSrc);
			GLShader fragShader = new GLShader(GL_FRAGMENT_SHADER, fragShaderSrc);
			sp.attachShader(vertShader);
			//sp.attachShader(geomShader);
			sp.attachShader(fragShader);
			sp.linkAndDiscardShaders();
			sp.use();
		} catch (GLShaderException e) {
			Log.error("Shader creation failed.", e);
			return false;
		}

		vao = new GLVertexArrayObject();
		vbo = new GLVertexBufferObject();
		ebo = new GLElementBufferObject();
		vao.bind();
		vbo.bind();
		ebo.bind();

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
		glEnableVertexAttribArray(0);

		Log.info("Renderer initialized.");
		return true;
	}

	public void render() {
		glClearBufferfv(GL_COLOR, 0, BLACK);
		buffer();
		//vao.bind();
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		gl.swapAndPoll();
	}

	public void buffer() {
		for (int i = 0; i < vertices.length; i++) vertices[i] += 0.001f;
		vbo.bufferStatic(vertices);
		ebo.bufferStatic(indices);
	}

	public void close() {
		if (sp != null) sp.delete();
		if (vao != null) vao.delete();
		if (vbo != null) vbo.delete();
		if (ebo != null) ebo.delete();
		Log.info("Renderer closed.");
	}
}
