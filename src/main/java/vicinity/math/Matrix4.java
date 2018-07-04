package vicinity.math;

/**
 * This class represents a 4x4-Matrix. GLSL equivalent to mat4.
 *
 * @author Ryan Palmer
 */
public class Matrix4 extends Matrix {
	public Matrix4() {
		super(4);
	}

	public Matrix4(Vector4 col1, Vector4 col2, Vector4 col3, Vector4 col4) {
		super(col1, col2, col3, col4);
	}

	/**
	 * Creates a orthographic projection matrix. Similar to
	 * <code>glOrtho(left, right, bottom, top, near, far)</code>.
	 *
	 * @param left   Coordinate for the left vertical clipping pane
	 * @param right  Coordinate for the right vertical clipping pane
	 * @param bottom Coordinate for the bottom horizontal clipping pane
	 * @param top    Coordinate for the bottom horizontal clipping pane
	 * @param near   Coordinate for the near depth clipping pane
	 * @param far    Coordinate for the far depth clipping pane
	 * @return Orthographic matrix
	 * @author Heiko Brumme
	 */
	public static Matrix4 orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4 ortho = new Matrix4();

		float tx = -(right + left) / (right - left);
		float ty = -(top + bottom) / (top - bottom);
		float tz = -(far + near) / (far - near);

		ortho.set(0, 0, 2f / (right - left));
		ortho.set(1, 1, 2f / (top - bottom));
		ortho.set(2, 2, -2f / (far - near));
		ortho.set(0, 3, tx);
		ortho.set(1, 3, ty);
		ortho.set(2, 3, tz);

		return ortho;
	}

	/**
	 * Creates a perspective projection matrix. Similar to
	 * <code>glFrustum(left, right, bottom, top, near, far)</code>.
	 *
	 * @param left   Coordinate for the left vertical clipping pane
	 * @param right  Coordinate for the right vertical clipping pane
	 * @param bottom Coordinate for the bottom horizontal clipping pane
	 * @param top    Coordinate for the bottom horizontal clipping pane
	 * @param near   Coordinate for the near depth clipping pane, must be
	 *               positive
	 * @param far    Coordinate for the far depth clipping pane, must be
	 *               positive
	 * @return Perspective matrix
	 * @author Heiko Brumme
	 */
	public static Matrix4 frustum(float left, float right, float bottom, float top, float near, float far) {
		Matrix4 frustum = new Matrix4();

		float a = (right + left) / (right - left);
		float b = (top + bottom) / (top - bottom);
		float c = -(far + near) / (far - near);
		float d = -(2f * far * near) / (far - near);

		frustum.set(0, 0, (2f * near) / (right - left));
		frustum.set(1, 1, (2f * near) / (top - bottom));
		frustum.set(0, 2, a);
		frustum.set(1, 2, b);
		frustum.set(2, 2, c);
		frustum.set(3, 2, -1);
		frustum.set(2, 3, d);
		frustum.set(3, 3, 0);

		return frustum;
	}

	/**
	 * Creates a perspective projection matrix. Similar to
	 * <code>gluPerspective(fovy, aspec, zNear, zFar)</code>.
	 *
	 * @param fovy   Field of view angle in degrees
	 * @param aspect The aspect ratio is the ratio of width to height
	 * @param near   Distance from the viewer to the near clipping plane, must
	 *               be positive
	 * @param far    Distance from the viewer to the far clipping plane, must be
	 *               positive
	 * @return Perspective matrix
	 * @author Heiko Brumme
	 */
	public static Matrix4 perspective(float fovy, float aspect, float near, float far) {
		Matrix4 perspective = new Matrix4();

		float f = (float) (1f / Math.tan(Math.toRadians(fovy) / 2f));

		perspective.set(0, 0, f / aspect);
		perspective.set(1, 1, f);
		perspective.set(2, 2, (far + near) / (near - far));
		perspective.set(3, 2, -1);
		perspective.set(2, 3, (2f * far * near) / (near - far));
		perspective.set(3, 3, 0);

		return perspective;
	}

	/**
	 * Creates a translation matrix. Similar to
	 * <code>glTranslate(x, y, z)</code>.
	 *
	 * @param x x coordinate of translation vector
	 * @param y y coordinate of translation vector
	 * @param z z coordinate of translation vector
	 * @return Translation matrix
	 * @author Heiko Brumme
	 */
	public static Matrix4 translate(float x, float y, float z) {
		Matrix4 translation = new Matrix4();

		translation.set(0, 3, x);
		translation.set(1, 3, y);
		translation.set(2, 3, z);

		return translation;
	}

	/**
	 * Creates a rotation matrix. Similar to
	 * <code>glRotate(angle, x, y, z)</code>.
	 *
	 * @param angle Angle of rotation in degrees
	 * @param x     x coordinate of the rotation vector
	 * @param y     y coordinate of the rotation vector
	 * @param z     z coordinate of the rotation vector
	 * @return Rotation matrix
	 * @author Heiko Brumme
	 */
	public static Matrix4 rotate(float angle, float x, float y, float z) {
		Matrix4 rotation = new Matrix4();

		float c = (float) Math.cos(Math.toRadians(angle));
		float s = (float) Math.sin(Math.toRadians(angle));
		Vector3 vec = new Vector3(x, y, z);
		if (vec.getLength() != 1f) {
			vec.normalize();
			x = vec.getX();
			y = vec.getY();
			z = vec.getZ();
		}

		rotation.set(0, 0, x * x * (1f - c) + c);
		rotation.set(1, 0, y * x * (1f - c) + z * s);
		rotation.set(2, 0, x * z * (1f - c) - y * s);
		rotation.set(0, 1, x * y * (1f - c) - z * s);
		rotation.set(1, 1, y * y * (1f - c) + c);
		rotation.set(2, 1, y * z * (1f - c) + x * s);
		rotation.set(0, 2, x * z * (1f - c) + y * s);
		rotation.set(1, 2, y * z * (1f - c) - x * s);
		rotation.set(2, 2, z * z * (1f - c) + c);

		return rotation;
	}

	/**
	 * Creates a scaling matrix. Similar to <code>glScale(x, y, z)</code>.
	 *
	 * @param x Scale factor along the x coordinate
	 * @param y Scale factor along the y coordinate
	 * @param z Scale factor along the z coordinate
	 * @return Scaling matrix
	 * @author Heiko Brumme
	 */
	public static Matrix4 scale(float x, float y, float z) {
		Matrix4 scaling = new Matrix4();

		scaling.set(0, 0, x);
		scaling.set(1, 1, y);
		scaling.set(2, 2, z);

		return scaling;
	}
}