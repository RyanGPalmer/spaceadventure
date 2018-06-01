package vc.engine;

import vc.engine.math.Matrix4;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Renderer {

	private final GLContext gl;

	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	private ShaderProgram sp;
	private int posLoc, colLoc;

	float angle = 0f;
	float[] pyramidData = {
			-0.5f, 0, 0.5f, 1, 0, 0,
			0, 0.5f, 0f, 0, 1, 0,
			0.5f, 0, 0.5f, 0, 0, 1
	};

	public Renderer(GLContext gl) {
		this.gl = gl;
	}

	public void init() {
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		vao = VertexArrayObject.create();
		vbo = VertexBufferObject.create();

		Shader v = Shader.create(GL_VERTEX_SHADER, gl.isLegacy());
		Shader f = Shader.create(GL_FRAGMENT_SHADER, gl.isLegacy());
		sp = ShaderProgram.create(gl.isLegacy(), v, f);
		if (!gl.isLegacy()) sp.bindFragDataLocation(0, "fragColor");
		sp.link();
		sp.use();

		vbo.upload(pyramidData);

		posLoc = sp.getAttributeLocation("position");
		colLoc = sp.getAttributeLocation("color");
		sp.enableVertexAttribute(posLoc);
		sp.enableVertexAttribute(colLoc);
		sp.pointVertexAttribute(posLoc, 3, 6 * GL_FLOAT, 0);
		sp.pointVertexAttribute(colLoc, 3, 6 * GL_FLOAT, 3 * GL_FLOAT);

		setUniforms();
		Log.info("Renderer initialized.");
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
		renderPyramid();
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

	public void renderPyramid() {
		sp.enableVertexAttribute(posLoc);
		sp.enableVertexAttribute(colLoc);
		glDrawArrays(GL_TRIANGLES, 0, 18);
		sp.disableVertexAttribute(posLoc);
		sp.disableVertexAttribute(colLoc);
	}
}
