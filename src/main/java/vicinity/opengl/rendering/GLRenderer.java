package vicinity.opengl.rendering;

import vicinity.Log;
import vicinity.math.Matrix4;
import vicinity.opengl.buffers.GLVertexBufferObject;
import vicinity.opengl.GLWindow;
import vicinity.opengl.shaders.GLShader;
import vicinity.opengl.shaders.GLShaderException;
import vicinity.opengl.shaders.GLShaderProgram;
import vicinity.util.FileUtils;

import java.util.ArrayList;
import java.util.Collection;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL;
import static org.lwjgl.opengl.GL30.glClearBufferfi;
import static org.lwjgl.opengl.GL30.glClearBufferfv;

public class GLRenderer {

	private static GLRenderer current;
	private final GLWindow window;

	private GLShaderProgram sp;
	private GLVertexArrayObject vao;
	private GLVertexBufferObject vbo;
	private Collection<GLRenderObject> objects = new ArrayList<>();
	private GLCamera camera;

	public GLRenderer(GLWindow window) {
		this.window = window;
	}

	public void makeCurrent() {
		GLRenderer.current = this;
	}

	public boolean init() {
		if (!initShaders()) return false;
		makeCurrent();
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glPolygonMode(GL_FRONT, GL_FILL);
		vao = new GLVertexArrayObject();
		vao.bind();

		vbo = new GLVertexBufferObject();
		vbo.point(0, 0, 0);
		vbo.bind();

		camera = new GLCamera();
		vao.unbind();
		Log.info("Renderer initialized.");
		return true;
	}

	private boolean initShaders() {
		String vertShaderSrc = FileUtils.read(GLShader.DEFAULT_VERTEX_SHADER_PATH);
		String fragShaderSrc = FileUtils.read(GLShader.DEFAULT_FRAGMENT_SHADER_PATH);

		if (vertShaderSrc == null || fragShaderSrc == null) {
			Log.error("Failed to load one or more shader source files.");
			return false;
		}

		try {
			sp = new GLShaderProgram();
			GLShader vertShader = new GLShader(GL_VERTEX_SHADER, vertShaderSrc);
			GLShader fragShader = new GLShader(GL_FRAGMENT_SHADER, fragShaderSrc);
			sp.attachShader(vertShader);
			sp.attachShader(fragShader);
			sp.linkAndDiscardShaders();
			sp.use();
			return true;
		} catch (GLShaderException e) {
			Log.error("Shader creation failed.", e);
			return false;
		}
	}

	public void render() {
		testStuff2();
		window.swapBuffers();
	}

	private void testStuff2() {
		glClearBufferfv(GL_COLOR, 0, camera.getBackground());
		glClearBufferfi(GL_DEPTH_STENCIL, 0, 1.0f, 0);
		int pjLoc = sp.getUniformLocation("pj_matrix");
		glUniformMatrix4fv(pjLoc, false, camera.getProjectionMatrix().toArray());
		for (GLRenderObject obj : objects) {
			vbo.bufferStatic(obj.getVertices());
			int mvLoc = sp.getUniformLocation("mv_matrix");
			float[] modelViewMatrix = camera.transform.getLookAtMatrix().multiply(obj.transform.getTransformationMatrix()).toArray();
			//float[] modelViewMatrix = obj.transform.getTransformationMatrix().toArray();
			glUniformMatrix4fv(mvLoc, false, modelViewMatrix);
			vao.drawArrays();
		}
	}

	public boolean registerObject(GLRenderObject object) {
		return objects.add(object);
	}

	public boolean deregisterObject(GLRenderObject object) {
		return objects.remove(object);
	}

	public static GLRenderer current() {
		return current;
	}

	public void setCamera(GLCamera camera) {
		this.camera = camera;
	}

	public GLWindow getWindow() {
		return window;
	}

	public void close() {
		if (sp != null) sp.delete();
		if (vao != null) vao.delete();
		if (vbo != null) vbo.delete();
		Log.info("Renderer closed.");
	}
}
