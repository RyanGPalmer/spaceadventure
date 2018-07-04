package vicinity.opengl.rendering;

import vicinity.Log;
import vicinity.math.Matrix;
import vicinity.math.Matrix4;
import vicinity.math.Vector3;
import vicinity.opengl.buffers.GLVertexBufferObject;
import vicinity.opengl.glfw.GLFWWindow;
import vicinity.opengl.shaders.GLShader;
import vicinity.opengl.shaders.GLShaderException;
import vicinity.opengl.shaders.GLShaderProgram;
import vicinity.util.FileUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL;
import static org.lwjgl.opengl.GL30.glClearBufferfi;
import static org.lwjgl.opengl.GL30.glClearBufferfv;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class GLRenderer {
	private static final float[] BLACK = {0.0f, 0.0f, 0.0f, 1.0f};
	private static final Vector3 WHITE = new Vector3(1.0f, 1.0f, 1.0f);
	private static final Vector3 RED = new Vector3(1.0f, 0.0f, 0.0f);
	private static final Vector3 GREEN = new Vector3(0.0f, 1.0f, 0.0f);
	private static final Vector3 BLUE = new Vector3(0.0f, 0.0f, 1.0f);

	private final GLFWWindow window;
	private final long startTime;

	private GLShaderProgram sp;
	private GLVertexArrayObject vao;
	private GLVertexBufferObject vbo;

	private static final float[] vertices = {
			-0.25f, -0.25f, -0.25f,
			-0.25f, -0.25f, 0.25f,
			-0.25f, 0.25f, 0.25f,

			0.25f, 0.25f, -0.25f,
			-0.25f, -0.25f, -0.25f,
			-0.25f, 0.25f, -0.25f,

			0.25f, -0.25f, 0.25f,
			-0.25f, -0.25f, -0.25f,
			0.25f, -0.25f, -0.25f,

			0.25f, 0.25f, -0.25f,
			0.25f, -0.25f, -0.25f,
			-0.25f, -0.25f, -0.25f,

			-0.25f, -0.25f, -0.25f,
			-0.25f, 0.25f, 0.25f,
			-0.25f, 0.25f, -0.25f,

			0.25f, -0.25f, 0.25f,
			-0.25f, -0.25f, 0.25f,
			-0.25f, -0.25f, -0.25f,

			-0.25f, 0.25f, 0.25f,
			-0.25f, -0.25f, 0.25f,
			0.25f, -0.25f, 0.25f,

			0.25f, 0.25f, 0.25f,
			0.25f, -0.25f, -0.25f,
			0.25f, 0.25f, -0.25f,

			0.25f, -0.25f, -0.25f,
			0.25f, 0.25f, 0.25f,
			0.25f, -0.25f, 0.25f,

			0.25f, 0.25f, 0.25f,
			0.25f, 0.25f, -0.25f,
			-0.25f, 0.25f, -0.25f,

			0.25f, 0.25f, 0.25f,
			-0.25f, 0.25f, -0.25f,
			-0.25f, 0.25f, 0.25f,

			0.25f, 0.25f, 0.25f,
			-0.25f, 0.25f, 0.25f,
			0.25f, -0.25f, 0.25f
	};

	public GLRenderer(GLFWWindow window) {
		this.window = window;
		startTime = System.currentTimeMillis();
	}

	public boolean init() {
		if (!initShaders()) return false;
		glEnable(GL_DEPTH_TEST);
		vao = new GLVertexArrayObject();
		vao.bind();

		vbo = new GLVertexBufferObject();
		vbo.point(0, 0, 0);
		vbo.bind();
		vbo.bufferStatic(vertices);

		Log.info("Renderer initialized.");
		vao.unbind();
		return true;
	}

	private boolean initShaders() {
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
			return true;
		} catch (GLShaderException e) {
			Log.error("Shader creation failed.", e);
			return false;
		}
	}

	private boolean initProjectionMatrix() {
		Matrix4 projection = Matrix4.perspective(50, (float) window.getHeight() / window.getHeight(), 0.1f, 1000);
		int pjLoc = sp.getUniformLocation("pj_matrix");
		glUniformMatrix4fv(pjLoc, false, projection.toArray());
		return true;
	}

	public void render() {
		testStuff();
		window.swapBuffers();
	}

	private void testStuff() {
		float currentTime = getElapsedTimeSeconds();
		glClearBufferfv(GL_COLOR, 0, BLACK);
		glClearBufferfi(GL_DEPTH_STENCIL, 0, 1.0f, 0);
		initProjectionMatrix();

		for (int i = 0; i < 1000; i++) {
			float f = i + currentTime * 0.3f;

			Matrix4 translation = (Matrix4)
					Matrix4.translate(0, 0, -4).multiply(
							Matrix4.translate(
									(float) Math.sin(2.1 * f) * 0.5f,
									(float) Math.cos(1.7 * f) * 0.5f,
									(float) Math.sin(1.3 * f) * (float) Math.cos(1.5 * f) * 2.0f));

			Matrix4 rotation = (Matrix4)
					Matrix4.rotate(currentTime * 45, 0, 1, 0).multiply(
							Matrix4.rotate(currentTime * 81, 1, 0, 1));

			Matrix4 modelView = (Matrix4) translation.multiply(rotation);

			int mvLoc = sp.getUniformLocation("mv_matrix");
			glUniformMatrix4fv(mvLoc, false, modelView.toArray());
			vao.drawArrays();
		}
	}

	private float getElapsedTimeSeconds() {
		long elapsedMillis = System.currentTimeMillis() - startTime;
		return (float) elapsedMillis / 1000f;
	}

	public void close() {
		if (sp != null) sp.delete();
		if (vao != null) vao.delete();
		if (vbo != null) vbo.delete();
		Log.info("Renderer closed.");
	}
}
