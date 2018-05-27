package vc.engine.math;

/**
 * Represents a 3-dimensional Vector. GLSL equivalent to vec3.
 *
 * @author Ryan Palmer
 */
public class Vector3 extends Vector {
	public Vector3(float x, float y, float z) {
		super(x, y, z);
	}

	public float getX() {
		return get(0);
	}

	public float getY() {
		return get(1);
	}

	public float getZ() {
		return get(2);
	}
}