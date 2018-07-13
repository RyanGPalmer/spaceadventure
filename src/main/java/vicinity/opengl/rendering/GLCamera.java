package vicinity.opengl.rendering;

import vicinity.math.Matrix;
import vicinity.math.Matrix4;

public class GLCamera {
	private static final float[] BLACK = {0.0f, 0.0f, 0.0f, 1.0f};
	private static final boolean DEFAULT_ORTHOGRAPHIC = false;
	private static final int DEFAULT_FOV = 50;

	private Matrix4 translation;
	private Matrix4 rotation;
	private Matrix4 projection;
	private final float[] background;
	private float fov;
	private boolean orthographic;

	public GLCamera() {
		fov = DEFAULT_FOV;
		orthographic = DEFAULT_ORTHOGRAPHIC;
		background = BLACK;
		translation = new Matrix4();
		rotation = new Matrix4();
		updateProjectionMatrix();
	}

	public void translate(float x, float y, float z) {
		translation.multiply(Matrix4.translate(x, y, z));
	}

	public void rotate(float angle, float x, float y, float z) {
		rotation.multiply(Matrix4.rotate(angle, x, y, z));
	}

	public Matrix4 getProjectionMatrix() {
		return new Matrix4(projection);
	}

	private void updateProjectionMatrix() {
		projection = orthographic ? new Matrix4() : Matrix4.perspective(fov, GLRenderer.current().getWindow().getAspectRatio(), 0.1f, 1000);
	}

	public void setFOV(float fov) {
		this.fov = fov;
		updateProjectionMatrix();
	}

	public void setOrthographic(boolean orthographic) {
		this.orthographic = orthographic;
		updateProjectionMatrix();
	}

	public float[] getBackground() {
		return background;
	}

	public Matrix getViewMatrix() {
		return Matrix.multiply(translation, rotation);
	}
}
