package vicinity;

import vicinity.util.FileUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glClearBufferfv;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Renderer {
	private static final float[] BLACK = {0.0f, 0.0f, 0.0f, 1.0f};
	private static final float[] WHITE = {1.0f, 1.0f, 1.0f, 1.0f};
	private static final float[] RED = {1.0f, 0.0f, 0.0f, 1.0f};
	private static final float[] GREEN = {0.0f, 1.0f, 0.0f, 1.0f};
	private static final float[] BLUE = {0.0f, 0.0f, 1.0f, 1.0f};

	private final OpenGL gl;
	private ShaderProgram sp;

	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	private ElementBufferObject ebo;

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

	public Renderer(OpenGL gl) {
		this.gl = gl;
	}

	public boolean init() {
		String vertShaderSrc = FileUtils.read(Shader.DEFAULT_VERTEX_SHADER_PATH);
		String geoShaderSrc = FileUtils.read(Shader.DEFAULT_GEOMETRY_SHADER_PATH);
		String fragShaderSrc = FileUtils.read(Shader.DEFAULT_FRAGMENT_SHADER_PATH);

		if (vertShaderSrc == null || geoShaderSrc == null || fragShaderSrc == null) {
			Log.error("Failed to load one or more shader source files.");
			return false;
		}

		try {
			sp = new ShaderProgram();
			Shader vertShader = new Shader(GL_VERTEX_SHADER, vertShaderSrc);
			Shader geomShader = new Shader(GL_GEOMETRY_SHADER, geoShaderSrc);
			Shader fragShader = new Shader(GL_FRAGMENT_SHADER, fragShaderSrc);
			sp.attachShader(vertShader);
			//sp.attachShader(geomShader);
			sp.attachShader(fragShader);
			sp.linkAndDiscardShaders();
		} catch (ShaderException e) {
			Log.error("Shader creation failed.", e);
			return false;
		}

		vao = new VertexArrayObject();
		vao.bind();

		vbo = new VertexBufferObject();
		vbo.bind();
		vbo.bufferStatic(vertices);

		ebo = new ElementBufferObject();
		ebo.bind();
		ebo.bufferStatic(indices);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
		glEnableVertexAttribArray(0);

		vao.unbind();
		vbo.unbind();

		Log.info("Renderer initialized.");
		return true;
	}

	public void render() {
		glClearBufferfv(GL_COLOR, 0, BLACK);
		sp.use();
		vao.bind();
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		gl.swapAndPoll();
	}

	public void close() {
		if (sp != null) sp.delete();
		if (vao != null) vao.delete();
		if (vbo != null) vbo.delete();
		if (ebo != null) ebo.delete();
		Log.info("Renderer closed.");
	}
}
