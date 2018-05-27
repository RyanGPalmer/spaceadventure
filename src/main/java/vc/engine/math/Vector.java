package vc.engine.math;

import vc.engine.Log;

import java.nio.FloatBuffer;

/**
 * Generic Vector class representing any n-dimensional Vector.
 *
 * @author Ryan Palmer
 */
public class Vector {
	private float[] dimensions;

	public Vector(float... dimensions) {
		this.dimensions = dimensions;
	}

	public Vector(Vector v) {
		dimensions = new float[v.getSize()];
		for (int i = 0; i < v.getSize(); i++) dimensions[i] = v.get(i);
	}

	public static float dot(Vector... vectors) {
		if (!checkCompatibility(vectors)) return 0;
		float result = 0;
		int size = vectors[0].getSize();
		for (int i = 0; i < size; i++) {
			float dimensionalResult = vectors[0].get(i);
			for (int j = 1; j < vectors.length; j++) dimensionalResult *= vectors[j].get(i);
			result += dimensionalResult;
		}
		return result;
	}

	public static Vector cross(Vector v, Vector w) {
		if (v.getSize() != 3) { // Must be 3-dimensional
			Log.error("Tried to get the cross product of a non-3-dimensional vector.", new RuntimeException());
			return null;
		} else if (!checkCompatibility(v, w)) return null;
		float[] dimensions = new float[3];
		dimensions[0] = v.get(1) * w.get(2) - v.get(2) * w.get(1);
		dimensions[1] = v.get(2) * w.get(0) - v.get(0) * w.get(2);
		dimensions[2] = v.get(0) * w.get(1) - v.get(1) * w.get(0);
		return new Vector(dimensions);
	}

	public static Vector add(Vector... vectors) {
		if (!checkCompatibility(vectors)) return null;
		Vector result = new Vector(vectors[0]);
		for (int i = 1; i < vectors.length; i++) result.add(vectors[i]);
		return result;
	}

	public static Vector subtract(Vector... vectors) {
		if (!checkCompatibility(vectors)) return null;
		Vector result = new Vector(vectors[0]);
		for (int i = 1; i < vectors.length; i++) result.subtract(vectors[i]);
		return result;
	}

	public static Vector scale(Vector v, float scalar) {
		Vector result = new Vector(v);
		result.scale(scalar);
		return result;
	}

	public static Vector getNormalized(Vector v) {
		Vector result = new Vector(v);
		result.normalize();
		return result;
	}

	public static boolean checkCompatibility(Vector... vectors) {
		if (vectors.length == 0) {
			Log.error("Tried to perform vector operation with no vectors.", new RuntimeException());
			return false;
		} else {
			int size = vectors[0].getSize();
			for (Vector v : vectors) {
				if (v == null) {
					Log.error("Tried to operate on a null vector.", new RuntimeException());
					return false;
				} else if (v.getSize() != size) {
					Log.error("Tried to operate on incompatible vectors.", new RuntimeException());
					return false;
				}
			}
			return true;
		}
	}

	public static boolean equals(Vector... vectors) {
		if (!checkCompatibility(vectors)) return false;
		float[] dimensions = vectors[0].getDimensions();
		for (int i = 1; i < vectors.length; i++)
			for (int j = 0; j < dimensions.length; j++) if (dimensions[j] != vectors[i].get(j)) return false;
		return true;
	}

	public Vector add(Vector v) {
		if (!checkCompatibility(this, v)) return this;
		for (int i = 0; i < getSize(); i++) dimensions[i] += v.get(i);
		return this;
	}

	public Vector subtract(Vector v) {
		if (!checkCompatibility(this, v)) return this;
		Vector v2 = new Vector(v);
		return add(v2.negate());
	}

	public Vector scale(float scalar) {
		for (int i = 0; i < getSize(); i++) dimensions[i] *= scalar;
		return this;
	}

	public Vector normalize() {
		return scale(1f / getLength());
	}

	public Vector lerp(Vector v, float alpha) {
		scale(1f - alpha);
		Vector v2 = new Vector(v);
		return add(v2.scale(alpha));
	}

	public Vector negate() {
		return scale(-1);
	}

	public float getLength() {
		float sum = 0;
		for (int i = 0; i < getSize(); i++) sum += get(i) * get(i);
		return (float) Math.sqrt(sum);
	}

	public int getSize() {
		return dimensions.length;
	}

	public float get(int dimension) {
		if (dimension < 0 || dimension >= getSize()) {
			Log.error("Tried to get dimension " + (dimension + 1) + " from a " + getSize() + "-dimensional vector.", new RuntimeException());
			return 0;
		} else return dimensions[dimension];
	}

	public float[] getDimensions() {
		return dimensions;
	}

	public void toBuffer(FloatBuffer buffer) {
		for (float d : dimensions) buffer.put(d);
		buffer.flip();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (float d : dimensions) sb.append(d + ", ");
		return sb.substring(0, sb.length() - 2) + "]";
	}
}
