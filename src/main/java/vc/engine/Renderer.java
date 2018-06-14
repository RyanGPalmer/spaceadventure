package vc.engine;

import vc.engine.util.FileUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
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
	private static final float[] vertices = {
			0.25f, -0.25f, 0.5f,
			-0.25f, -0.25f, 0.5f,
			0.25f, 0.25f, 0.5f};

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
			Shader geoShader = new Shader(GL_GEOMETRY_SHADER, geoShaderSrc);
			Shader fragShader = new Shader(GL_FRAGMENT_SHADER, fragShaderSrc);
			sp.attachShader(vertShader);
			//sp.attachShader(geoShader);
			sp.attachShader(fragShader);
			sp.linkAndDiscardShaders();
		} catch (ShaderException e) {
			Log.error("Shader creation failed.", e);
			return false;
		}

		vao = new VertexArrayObject();
		vao.setOffset(0, 0, 0, 0);
		vao.setColor(WHITE);

		vbo = new VertexBufferObject();
		vbo.bind();

		vbo.buffer(vertices);

		Log.info("Renderer initialized.");
		return true;
	}

	public void render() {
		glClearBufferfv(GL_COLOR, 0, BLACK);
		sp.use();
		vao.bind();
		glDrawArrays(GL_TRIANGLES, 0, 3);
		gl.swapAndPoll();
	}

	public void close() {
		if (sp != null) sp.delete();
		if (vao != null) vao.delete();
		if (vbo != null) vbo.delete();
		Log.info("Renderer closed.");
	}
}
