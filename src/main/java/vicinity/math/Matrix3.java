package vicinity.math;

/**
 * This class represents a 3x3-Matrix. GLSL equivalent to mat3.
 *
 * @author Ryan Palmer
 */
public class Matrix3 extends Matrix {
	public Matrix3() {
		super(3);
	}

	public Matrix3(Vector3 col1, Vector3 col2, Vector3 col3) {
		super(col1, col2, col3);
	}
}