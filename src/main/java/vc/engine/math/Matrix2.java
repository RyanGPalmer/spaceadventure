package vc.engine.math;

/**
 * This class represents a 2x2-Matrix. GLSL equivalent to mat2.
 *
 * @author Ryan Palmer
 */
public class Matrix2 extends Matrix {
	public Matrix2() {
		super(2);
	}

	public Matrix2(Vector2 col1, Vector2 col2) {
		super(col1, col2);
	}
}