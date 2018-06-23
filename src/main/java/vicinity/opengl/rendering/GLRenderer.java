package vicinity.opengl.rendering;

import vicinity.Log;
import vicinity.math.Vector3;
import vicinity.opengl.buffers.GLElementBufferObject;
import vicinity.opengl.buffers.GLVertexBufferObject;
import vicinity.opengl.glfw.GLFWWindow;
import vicinity.opengl.shaders.GLShader;
import vicinity.opengl.shaders.GLShaderException;
import vicinity.opengl.shaders.GLShaderProgram;
import vicinity.util.FileUtils;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glClearBufferfv;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class GLRenderer {
	private static final float[] BLACK = {0.0f, 0.0f, 0.0f, 1.0f};
	private static final Vector3 WHITE = new Vector3(1.0f, 1.0f, 1.0f);
	private static final Vector3 RED = new Vector3(1.0f, 0.0f, 0.0f);
	private static final Vector3 GREEN = new Vector3(0.0f, 1.0f, 0.0f);
	private static final Vector3 BLUE = new Vector3(0.0f, 0.0f, 1.0f);

	private final GLFWWindow window;

	private GLShaderProgram sp;
	private GLVertexArrayObject vao;
	private GLElementBufferObject ebo;
	private GLVertexBufferObject vbo;

	private static final int[] indices = {
			0, 1, 3,    // first triangle
			1, 2, 3        // second triangle
	};

	private static final Vector3[] vertices = {
			new Vector3(0.5f, 0.5f, 0.0f),    // top right
			RED,
			new Vector3(0.5f, -0.5f, 0.0f),    // bottom right
			GREEN,
			new Vector3(-0.5f, -0.5f, 0.0f),    // bottom left
			BLUE,
			new Vector3(-0.5f, 0.5f, 0.0f),    // top left
			WHITE
	};

	public GLRenderer(GLFWWindow window) {
		this.window = window;
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
		ebo = new GLElementBufferObject();
		vao.bind();
		ebo.bind();

		vbo = new GLVertexBufferObject();
		vbo.point(0, 0, 6);
		vbo.point(1, 3, 6);

		vao.unbind();

		Log.info("Renderer initialized.");
		return true;
	}

	public void render() {
		glClearBufferfv(GL_COLOR, 0, BLACK);
		buffer();
		vao.drawElements();
		window.swapBuffers();
	}

	public void buffer() {
		float[] data = new float[vertices.length * 3];
		int pointer = 0;
		for (Vector3 v : vertices) for (float f : v.toArray()) data[pointer++] = f;
		vbo.bufferStatic(data);
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
