package vicinity.math;

import vicinity.Log;

/**
 * This class represents a 4x4-Matrix. GLSL equivalent to mat4.
 *
 * @author Ryan Palmer
 */
public class Matrix4 {
	private float[] values;

	public Matrix4() {
		values = new float[16];
		setIdentity();
	}

	public Matrix4(float[] values) {
		this.values = new float[16];
		if (values.length == 16) {
			System.arraycopy(values, 0, this.values, 0, 16);
			transpose(); // Convert to column-major
		} else {
			Log.error("Tried to initialize a Matrix4 (4 x 4) with " + values.length + " values.", new Exception());
			setIdentity();
		}
	}

	public Matrix4(Matrix4 m) {
		values = new float[16];
		System.arraycopy(m.values, 0, values, 0, 16);
	}

	public static Matrix4 add(Matrix4... matrices) {
		Matrix4 result = new Matrix4(matrices[0]);
		for (int i = 1; i < matrices.length; i++) result.add(matrices[i]);
		return result;
	}

	public static Matrix4 scale(Matrix4 m, float scalar) {
		Matrix4 result = new Matrix4(m);
		return result.scale(scalar);
	}

	public static Matrix4 multiply(Matrix4 m, Matrix4 n) {
		Matrix4 result = new Matrix4(m);
		return result.multiply(n);
	}

	public static Matrix4 transpose(Matrix4 m) {
		Matrix4 result = new Matrix4(m);
		return result.transpose();
	}

	public Matrix4 add(Matrix4 m) {
		for (int r = 0; r < 4; r++) for (int c = 0; c < 4; c++) values[c * 4 + r] += m.values[c * 4 + r];
		return this;
	}

	public Matrix4 subtract(Matrix4 m) {
		Matrix4 m2 = new Matrix4(m);
		return add(m2.negate());
	}

	public Matrix4 scale(float scalar) {
		for (int i = 0; i < values.length; i++) values[i] *= scalar;
		return this;
	}

	public Matrix4 multiply(Matrix4 m) {
		float[] temp = new float[16];
		for (int r = 0; r < 4; r++) {
			float[] row = new float[4];
			for (int i = 0; i < 4; i++) row[i] = values[i * 4 + r];
			for (int c = 0; c < 4; c++) {
				float[] col = new float[4];
				for (int i = 0; i < 4; i++) col[i] = m.values[c * 4 + i];
				temp[c * 4 + r] = Vector4.dot(new Vector4(row), new Vector4(col));
			}
		}
		values = temp;
		return this;
	}

	public Matrix4 negate() {
		return scale(-1);
	}

	public Matrix4 transpose() {
		Matrix4 temp = new Matrix4(this);
		for (int r = 0; r < 4; r++) for (int c = 0; c < 4; c++) values[r * 4 + c] = temp.values[c * 4 + r];
		return this;
	}

	public Matrix4 setIdentity() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == j) values[j * 4 + i] = 1;
				else values[j * 4 + i] = 0;
			}
		}
		return this;
	}

	public float get(int r, int c) {
		return values[c * 4 + r];
	}

	public void set(int r, int c, float val) {
		values[c * 4 + r] = val;
	}

	public float[] toArray() {
		return values;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int r = 0; r < 4; r++) {
			String row = "[";
			for (int c = 0; c < 4; c++) row += values[c * 4 + r] + ", ";
			sb.append(row.substring(0, row.length() - 2) + "]\n");
		}
		return sb.substring(0, sb.length() - 1);
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
		if (x == 0 && y == 0 && z == 0) return rotation;

		float c = (float) Math.cos(Math.toRadians(angle));
		float s = (float) Math.sin(Math.toRadians(angle));
		Vector3 vec = new Vector3(new float[]{x, y, z});
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

	public static Matrix4 lookAt(Vector3 position, Vector3 target) {
		Matrix4 orientation = new Matrix4();
		Matrix4 offset = new Matrix4();
		Vector3 direction = Vector3.getNormalized(Vector3.subtract(target, position));
		Vector3 right = Vector3.getNormalized(Vector3.cross(Vector.UP, direction));
		Vector3 up = Vector3.cross(direction, right);

		orientation.set(0, 0, right.getX());
		orientation.set(0, 1, right.getY());
		orientation.set(0, 2, right.getZ());
		orientation.set(1, 0, up.getX());
		orientation.set(1, 1, up.getY());
		orientation.set(1, 2, up.getZ());
		orientation.set(2, 0, direction.getX());
		orientation.set(2, 1, direction.getY());
		orientation.set(2, 2, direction.getZ());

		offset.set(0, 3, position.getX());
		offset.set(1, 3, position.getY());
		offset.set(2, 3, position.getZ());

		return Matrix4.multiply(orientation, offset);
	}
}