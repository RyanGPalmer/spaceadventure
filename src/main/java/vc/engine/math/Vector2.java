package vc.engine.math;

/**
 * Represents a 2-dimensional Vector. GLSL equivalent to vec2.
 *
 * @author Ryan Palmer
 */
public class Vector2 extends Vector {
	public Vector2(float x, float y) {
		super(x, y);
	}

	public float getX() {
		return get(0);
	}

	public float getY() {
		return get(1);
	}
}