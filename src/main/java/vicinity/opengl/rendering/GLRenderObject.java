package vicinity.opengl.rendering;

import vicinity.math.Matrix;
import vicinity.math.Matrix4;

public class GLRenderObject {
	private final float[] vertices;
	private Matrix4 translation;
	private Matrix4 rotation;
	private Matrix4 scale;

	public GLRenderObject(float[] vertices) {
		this.vertices = vertices;
		translation = new Matrix4();
		rotation = new Matrix4();
		scale = new Matrix4();
	}

	public void translate(float x, float y, float z) {
		translation.multiply(Matrix4.translate(x, y, z));
	}

	public void setTranslation(float x, float y, float z) {
		translation = Matrix4.translate(x, y, z);
	}

	public void rotate(float angle, float x, float y, float z) {
		rotation.multiply(Matrix4.rotate(angle, x, y, z));
	}

	public void setRotation(float angle, float x, float y, float z) {
		rotation = Matrix4.rotate(angle, x, y, z);
	}

	public void scale(float x, float y, float z) {
		scale.multiply(Matrix4.scale(x, y, z));
	}

	public void setScale(float x, float y, float z) {
		scale = Matrix4.scale(x, y, z);
	}

	public Matrix getTransformMatrix() {
		return Matrix.multiply(translation, rotation).multiply(scale);
	}

	public float[] getVertices() {
		return vertices;
	}

	public void register() {
		GLRenderer.current().registerObject(this);
	}

	public void deregister() {
		GLRenderer.current().deregisterObject(this);
	}
}