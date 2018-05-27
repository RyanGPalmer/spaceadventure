package vc.engine;

import vc.engine.math.Vector3;
import vc.engine.math.Vector4;

import java.nio.FloatBuffer;

/**
 * This class represents a 4x4-Matrix. GLSL equivalent to mat4.
 *
 * @author Heiko Brumme
 */
public class Matrix4f {

	private float m00, m01, m02, m03;
	private float m10, m11, m12, m13;
	private float m20, m21, m22, m23;
	private float m30, m31, m32, m33;

	/**
	 * Creates a 4x4 identity matrix.
	 */
	public Matrix4f() {
		setIdentity();
	}

	/**
	 * Creates a 4x4 matrix with specified columns.
	 *
	 * @param col1 Vector with values of the first column
	 * @param col2 Vector with values of the second column
	 * @param col3 Vector with values of the third column
	 * @param col4 Vector with values of the fourth column
	 */
	public Matrix4f(Vector4 col1, Vector4 col2, Vector4 col3, Vector4 col4) {
		m00 = col1.getX();
		m10 = col1.getY();
		m10 = col1.getY();
		m20 = col1.getZ();
		m30 = col1.getW();

		m01 = col2.getX();
		m11 = col2.getY();
		m21 = col2.getZ();
		m31 = col2.getW();

		m02 = col3.getX();
		m12 = col3.getY();
		m22 = col3.getZ();
		m32 = col3.getW();

		m03 = col4.getX();
		m13 = col4.getY();
		m23 = col4.getZ();
		m33 = col4.getW();
	}

	/**
	 * Sets this matrix to the identity matrix.
	 */
	public final void setIdentity() {
		m00 = 1f;
		m11 = 1f;
		m22 = 1f;
		m33 = 1f;

		m01 = 0f;
		m02 = 0f;
		m03 = 0f;
		m10 = 0f;
		m12 = 0f;
		m13 = 0f;
		m20 = 0f;
		m21 = 0f;
		m23 = 0f;
		m30 = 0f;
		m31 = 0f;
		m32 = 0f;
	}

	/**
	 * Adds this matrix to another matrix.
	 *
	 * @param other The other matrix
	 * @return Sum of this + other
	 */
	public Matrix4f add(Matrix4f other) {
		Matrix4f result = new Matrix4f();

		result.m00 = this.m00 + other.m00;
		result.m10 = this.m10 + other.m10;
		result.m20 = this.m20 + other.m20;
		result.m30 = this.m30 + other.m30;

		result.m01 = this.m01 + other.m01;
		result.m11 = this.m11 + other.m11;
		result.m21 = this.m21 + other.m21;
		result.m31 = this.m31 + other.m31;

		result.m02 = this.m02 + other.m02;
		result.m12 = this.m12 + other.m12;
		result.m22 = this.m22 + other.m22;
		result.m32 = this.m32 + other.m32;

		result.m03 = this.m03 + other.m03;
		result.m13 = this.m13 + other.m13;
		result.m23 = this.m23 + other.m23;
		result.m33 = this.m33 + other.m33;

		return result;
	}

	/**
	 * Negates this matrix.
	 *
	 * @return Negated matrix
	 */
	public Matrix4f negate() {
		return multiply(-1f);
	}

	/**
	 * Subtracts this matrix from another matrix.
	 *
	 * @param other The other matrix
	 * @return Difference of this - other
	 */
	public Matrix4f subtract(Matrix4f other) {
		return this.add(other.negate());
	}

	/**
	 * Multiplies this matrix with a scalar.
	 *
	 * @param scalar The scalar
	 * @return Scalar product of this * scalar
	 */
	public Matrix4f multiply(float scalar) {
		Matrix4f result = new Matrix4f();

		result.m00 = this.m00 * scalar;
		result.m10 = this.m10 * scalar;
		result.m20 = this.m20 * scalar;
		result.m30 = this.m30 * scalar;

		result.m01 = this.m01 * scalar;
		result.m11 = this.m11 * scalar;
		result.m21 = this.m21 * scalar;
		result.m31 = this.m31 * scalar;

		result.m02 = this.m02 * scalar;
		result.m12 = this.m12 * scalar;
		result.m22 = this.m22 * scalar;
		result.m32 = this.m32 * scalar;

		result.m03 = this.m03 * scalar;
		result.m13 = this.m13 * scalar;
		result.m23 = this.m23 * scalar;
		result.m33 = this.m33 * scalar;

		return result;
	}

	/**
	 * Multiplies this matrix to a vector.
	 *
	 * @param vector The vector
	 * @return Vector product of this * other
	 */
	public Vector4 multiply(Vector4 vector) {
		float x = this.m00 * vector.getX() + this.m01 * vector.getY() + this.m02 * vector.getZ() + this.m03 * vector.getW();
		float y = this.m10 * vector.getX() + this.m11 * vector.getY() + this.m12 * vector.getZ() + this.m13 * vector.getW();
		float z = this.m20 * vector.getX() + this.m21 * vector.getY() + this.m22 * vector.getZ() + this.m23 * vector.getW();
		float w = this.m30 * vector.getX() + this.m31 * vector.getY() + this.m32 * vector.getZ() + this.m33 * vector.getW();
		return new Vector4(x, y, z, w);
	}

	/**
	 * Multiplies this matrix to another matrix.
	 *
	 * @param other The other matrix
	 * @return Matrix product of this * other
	 */
	public Matrix4f multiply(Matrix4f other) {
		Matrix4f result = new Matrix4f();

		result.m00 = this.m00 * other.m00 + this.m01 * other.m10 + this.m02 * other.m20 + this.m03 * other.m30;
		result.m10 = this.m10 * other.m00 + this.m11 * other.m10 + this.m12 * other.m20 + this.m13 * other.m30;
		result.m20 = this.m20 * other.m00 + this.m21 * other.m10 + this.m22 * other.m20 + this.m23 * other.m30;
		result.m30 = this.m30 * other.m00 + this.m31 * other.m10 + this.m32 * other.m20 + this.m33 * other.m30;

		result.m01 = this.m00 * other.m01 + this.m01 * other.m11 + this.m02 * other.m21 + this.m03 * other.m31;
		result.m11 = this.m10 * other.m01 + this.m11 * other.m11 + this.m12 * other.m21 + this.m13 * other.m31;
		result.m21 = this.m20 * other.m01 + this.m21 * other.m11 + this.m22 * other.m21 + this.m23 * other.m31;
		result.m31 = this.m30 * other.m01 + this.m31 * other.m11 + this.m32 * other.m21 + this.m33 * other.m31;

		result.m02 = this.m00 * other.m02 + this.m01 * other.m12 + this.m02 * other.m22 + this.m03 * other.m32;
		result.m12 = this.m10 * other.m02 + this.m11 * other.m12 + this.m12 * other.m22 + this.m13 * other.m32;
		result.m22 = this.m20 * other.m02 + this.m21 * other.m12 + this.m22 * other.m22 + this.m23 * other.m32;
		result.m32 = this.m30 * other.m02 + this.m31 * other.m12 + this.m32 * other.m22 + this.m33 * other.m32;

		result.m03 = this.m00 * other.m03 + this.m01 * other.m13 + this.m02 * other.m23 + this.m03 * other.m33;
		result.m13 = this.m10 * other.m03 + this.m11 * other.m13 + this.m12 * other.m23 + this.m13 * other.m33;
		result.m23 = this.m20 * other.m03 + this.m21 * other.m13 + this.m22 * other.m23 + this.m23 * other.m33;
		result.m33 = this.m30 * other.m03 + this.m31 * other.m13 + this.m32 * other.m23 + this.m33 * other.m33;

		return result;
	}

	/**
	 * Transposes this matrix.
	 *
	 * @return Transposed matrix
	 */
	public Matrix4f transpose() {
		Matrix4f result = new Matrix4f();

		result.m00 = this.m00;
		result.m10 = this.m01;
		result.m20 = this.m02;
		result.m30 = this.m03;

		result.m01 = this.m10;
		result.m11 = this.m11;
		result.m21 = this.m12;
		result.m31 = this.m13;

		result.m02 = this.m20;
		result.m12 = this.m21;
		result.m22 = this.m22;
		result.m32 = this.m23;

		result.m03 = this.m30;
		result.m13 = this.m31;
		result.m23 = this.m32;
		result.m33 = this.m33;

		return result;
	}

	/**
	 * Stores the matrix in a given Buffer.
	 *
	 * @param buffer The buffer to store the matrix data
	 */
	public void toBuffer(FloatBuffer buffer) {
		buffer.put(m00).put(m10).put(m20).put(m30);
		buffer.put(m01).put(m11).put(m21).put(m31);
		buffer.put(m02).put(m12).put(m22).put(m32);
		buffer.put(m03).put(m13).put(m23).put(m33);
		buffer.flip();
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
	 */
	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f ortho = new Matrix4f();

		float tx = -(right + left) / (right - left);
		float ty = -(top + bottom) / (top - bottom);
		float tz = -(far + near) / (far - near);

		ortho.m00 = 2f / (right - left);
		ortho.m11 = 2f / (top - bottom);
		ortho.m22 = -2f / (far - near);
		ortho.m03 = tx;
		ortho.m13 = ty;
		ortho.m23 = tz;

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
	 */
	public static Matrix4f frustum(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f frustum = new Matrix4f();

		float a = (right + left) / (right - left);
		float b = (top + bottom) / (top - bottom);
		float c = -(far + near) / (far - near);
		float d = -(2f * far * near) / (far - near);

		frustum.m00 = (2f * near) / (right - left);
		frustum.m11 = (2f * near) / (top - bottom);
		frustum.m02 = a;
		frustum.m12 = b;
		frustum.m22 = c;
		frustum.m32 = -1f;
		frustum.m23 = d;
		frustum.m33 = 0f;

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
	 */
	public static Matrix4f perspective(float fovy, float aspect, float near, float far) {
		Matrix4f perspective = new Matrix4f();

		float f = (float) (1f / Math.tan(Math.toRadians(fovy) / 2f));

		perspective.m00 = f / aspect;
		perspective.m11 = f;
		perspective.m22 = (far + near) / (near - far);
		perspective.m32 = -1f;
		perspective.m23 = (2f * far * near) / (near - far);
		perspective.m33 = 0f;

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
	 */
	public static Matrix4f translate(float x, float y, float z) {
		Matrix4f translation = new Matrix4f();

		translation.m03 = x;
		translation.m13 = y;
		translation.m23 = z;

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
	 */
	public static Matrix4f rotate(float angle, float x, float y, float z) {
		Matrix4f rotation = new Matrix4f();

		float c = (float) Math.cos(Math.toRadians(angle));
		float s = (float) Math.sin(Math.toRadians(angle));
		Vector3 vec = new Vector3(x, y, z);
		if (vec.getLength() != 1f) {
			vec.normalize();
			x = vec.getX();
			y = vec.getY();
			z = vec.getZ();
		}

		rotation.m00 = x * x * (1f - c) + c;
		rotation.m10 = y * x * (1f - c) + z * s;
		rotation.m20 = x * z * (1f - c) - y * s;
		rotation.m01 = x * y * (1f - c) - z * s;
		rotation.m11 = y * y * (1f - c) + c;
		rotation.m21 = y * z * (1f - c) + x * s;
		rotation.m02 = x * z * (1f - c) + y * s;
		rotation.m12 = y * z * (1f - c) - x * s;
		rotation.m22 = z * z * (1f - c) + c;

		return rotation;
	}

	/**
	 * Creates a scaling matrix. Similar to <code>glScale(x, y, z)</code>.
	 *
	 * @param x Scale factor along the x coordinate
	 * @param y Scale factor along the y coordinate
	 * @param z Scale factor along the z coordinate
	 * @return Scaling matrix
	 */
	public static Matrix4f scale(float x, float y, float z) {
		Matrix4f scaling = new Matrix4f();

		scaling.m00 = x;
		scaling.m11 = y;
		scaling.m22 = z;

		return scaling;
	}

}