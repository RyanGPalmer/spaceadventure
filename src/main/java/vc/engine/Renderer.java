package vc.engine;

import org.lwjgl.system.MemoryStack;
import vc.engine.math.Matrix4;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Renderer {

	private final GLContext gl;

	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	private ShaderProgram sp;
	float angle = 0f;

	public Renderer(GLContext gl) {
		this.gl = gl;
	}

	public void init() {
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		vao = VertexArrayObject.create();

		// Create VBO
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer vertices = stack.mallocFloat(3 * 6);
			vertices.put(-0.6f).put(-0.4f).put(0f).put(1f).put(0f).put(0f);
			vertices.put(0.6f).put(-0.4f).put(0f).put(0f).put(1f).put(0f);
			vertices.put(0f).put(0.721f).put(0f).put(0f).put(0f).put(1f);
			vertices.flip();

			vbo = VertexBufferObject.create();
			vbo.upload(vertices);
		}

		Shader v = Shader.create(GL_VERTEX_SHADER, gl.isLegacy());
		Shader f = Shader.create(GL_FRAGMENT_SHADER, gl.isLegacy());
		sp = ShaderProgram.create(gl.isLegacy(), v, f);
		if (!gl.isLegacy()) sp.bindFragDataLocation(0, "fragColor");
		sp.link();
		sp.use();
		setVertexAttributes();
		setUniforms();
		Log.info("Renderer initialized.");
	}

	private void setVertexAttributes() {
		sp.addAttribute("position");
		sp.addAttribute("color", 3);
	}

	private void setUniforms() {
		sp.setUniform("model", new Matrix4());
		sp.setUniform("view", new Matrix4());
		float ratio = (float) gl.getScreenX() / (float) gl.getScreenY();
		sp.setUniform("projection", Matrix4.orthographic(-ratio, ratio, -1f, 1f, -1f, 1f));
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		if (gl.isLegacy()) renderLegacy();
		else renderNormal();
		gl.swapAndPoll();
	}

	private void renderNormal() {
		vao.bind();
		sp.use();

		Matrix4 model = Matrix4.rotate(angle += 0.2f, -1f, 0f, 1f);
		sp.setUniform("model", model);

		glDrawArrays(GL_TRIANGLES, 0, 3);
	}

	private void renderLegacy() {
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(-1, -1);
		glTexCoord2f(0, 1);
		glVertex2f(-1, 1);
		glTexCoord2f(1, 1);
		glVertex2f(1, 1);
		glTexCoord2f(1, 0);
		glVertex2f(1, -1);
		glEnd();
	}

	public void close() {
		vao.delete();
		vbo.delete();
		sp.delete();
		Log.info("Renderer closed.");
	}
}
