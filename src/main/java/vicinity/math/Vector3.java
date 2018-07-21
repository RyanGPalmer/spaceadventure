package vicinity.math;

import vicinity.Log;

/**
 * Represents a 3-dimensional Vector. GLSL equivalent to vec3.
 *
 * @author Ryan Palmer
 */
public class Vector3 {
	private float[] values;

	public Vector3() {
		values = new float[3];
	}

	public Vector3(float[] values) {
		this.values = new float[3];
		if (values.length == 3) for (int i = 0; i < 3; i++) this.values[i] = values[i];
		else Log.error("Tried to initialize a Vector3 with " + values.length + " values.");
	}

	public Vector3(float x, float y, float z) {
		this(new float[]{x, y, z});
	}

	public Vector3(Vector3 other) {
		values = new float[3];
		values[0] = other.getX();
		values[1] = other.getY();
		values[2] = other.getZ();
	}

	public static float dot(Vector3... vectors) {
		float result = 0;
		for (int i = 0; i < 3; i++) {
			float dimensionalResult = vectors[0].get(i);
			for (int j = 1; j < vectors.length; j++) dimensionalResult *= vectors[j].get(i);
			result += dimensionalResult;
		}
		return result;
	}

	public static Vector3 cross(Vector3 v, Vector3 w) {
		float[] dimensions = new float[3];
		dimensions[0] = v.get(1) * w.get(2) - v.get(2) * w.get(1);
		dimensions[1] = v.get(2) * w.get(0) - v.get(0) * w.get(2);
		dimensions[2] = v.get(0) * w.get(1) - v.get(1) * w.get(0);
		return new Vector3(dimensions);
	}

	public static Vector3 add(Vector3... vectors) {
		Vector3 result = new Vector3(vectors[0]);
		for (int i = 1; i < vectors.length; i++) result.add(vectors[i]);
		return result;
	}

	public static Vector3 subtract(Vector3... vectors) {
		Vector3 result = new Vector3(vectors[0]);
		for (int i = 1; i < vectors.length; i++) result.subtract(vectors[i]);
		return result;
	}

	public static Vector3 scale(Vector3 v, float scalar) {
		Vector3 result = new Vector3(v);
		result.scale(scalar);
		return result;
	}

	public static Vector3 getNormalized(Vector3 v) {
		Vector3 result = new Vector3(v);
		result.normalize();
		return result;
	}

	public Vector3 add(Vector3 v) {
		values[0] += v.get(0);
		values[1] += v.get(1);
		values[2] += v.get(2);
		return this;
	}

	public Vector3 subtract(Vector3 v) {
		Vector3 v2 = new Vector3(v);
		return add(v2.negate());
	}

	public Vector3 scale(float scalar) {
		values[0] *= scalar;
		values[1] *= scalar;
		values[2] *= scalar;
		return this;
	}

	public Vector3 normalize() {
		return scale(1f / getLength());
	}

	public Vector3 lerp(Vector3 v, float alpha) {
		scale(1f - alpha);
		Vector3 v2 = new Vector3(v);
		return add(v2.scale(alpha));
	}

	public Vector3 negate() {
		return scale(-1);
	}

	public float getLength() {
		float sum = 0;
		for (int i = 0; i < 3; i++) sum += get(i) * get(i);
		return (float) Math.sqrt(sum);
	}

	public float[] toArray() {
		final float[] array = new float[3];
		array[0] = values[0];
		array[1] = values[1];
		array[2] = values[2];
		return array;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (float d : values) sb.append(d + ", ");
		return sb.substring(0, sb.length() - 2) + "]";
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

	public float get(int index) {
		try {
			return values[index];
		} catch (IndexOutOfBoundsException e) {
			Log.error("Tried to access invalid index (" + index + ") of a Vector3.", e);
			return 0;
		}
	}

	public static Vector3 direction(Vector3 origin, Vector3 target) {
		return Vector3.subtract(target, origin);
	}
}