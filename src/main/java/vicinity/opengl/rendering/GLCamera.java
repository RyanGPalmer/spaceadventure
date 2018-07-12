package vicinity.opengl.rendering;

import vicinity.math.Matrix;
import vicinity.math.Matrix4;

public class GLCamera {
	private final Matrix4 view;

	public GLCamera() {
		view = new Matrix4();
	}

	public void translate(float x, float y, float z) {
		view.multiply(Matrix4.translate(x, y, z));
	}

	public void rotate(float angle, float x, float y, float z) {
		view.multiply(Matrix4.rotate(angle, x, y, z));
	}

	public Matrix getViewMatrix() {
		return new Matrix(view);
	}
}
