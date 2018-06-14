package vicinity.math;

/**
 * Represents a 4-dimensional Vector. GLSL equivalent to vec4.
 *
 * @author Ryan Palmer
 */
public class Vector4 extends Vector {
	public Vector4(float x, float y, float z, float w) {
		super(x, y, z, w);
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

	public float getW() {
		return get(3);
	}
}