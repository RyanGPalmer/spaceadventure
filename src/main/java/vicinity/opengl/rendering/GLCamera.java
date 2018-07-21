package vicinity.opengl.rendering;

import vicinity.math.Matrix4;
import vicinity.math.VTransform;

public class GLCamera {
	private static final float[] BLACK = {0.0f, 0.0f, 0.0f, 1.0f};
	private static final boolean DEFAULT_ORTHOGRAPHIC = false;
	private static final int DEFAULT_FOV = 50;

	public final VTransform transform;

	private Matrix4 projection;
	private final float[] background;
	private float fov;
	private boolean orthographic;

	public GLCamera() {
		transform = new VTransform();
		fov = DEFAULT_FOV;
		orthographic = DEFAULT_ORTHOGRAPHIC;
		background = BLACK;
		updateProjectionMatrix();
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
}
