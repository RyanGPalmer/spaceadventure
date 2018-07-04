package vicinity.math;

import vicinity.Log;

import java.nio.FloatBuffer;

/**
 * Generic matrix capable of representing any n-dimensional matrix.
 * <p>
 * OpenGL uses column-major ordering, so this class uses the same.
 *
 * @author Ryan Palmer
 */
public class Matrix {
	private float[] matrix;

	public Matrix(int size) {
		matrix = new float[size * size];
		setIdentity();
	}

	public Matrix(Matrix m) {
		int size = m.getSize();
		matrix = new float[size * size];
		for (int i = 0; i < size * size; i++) matrix[i] = m.matrix[i];
	}

	public Matrix(Vector... vectors) {
		if (!Vector.checkCompatibility(vectors)) {
			Log.error("Tried to construct matrix from incompatible vectors.", new RuntimeException());
			matrix = new float[4 * 4];
			setIdentity();
		} else if (vectors.length != vectors[0].getSize()) {
			Log.error("Tried to construct " + vectors.length + "-dimensional matrix with " + vectors[0].getSize() + "-dimensional vectors.", new RuntimeException());
			matrix = new float[4 * 4];
			setIdentity();
		} else {
			int size = vectors.length;
			matrix = new float[size * size];
			for (int r = 0; r < size; r++)
				for (int c = 0; c < size; c++) matrix[c * size + r] = vectors[r].toArray()[c];
		}
	}

	public static Matrix add(Matrix... matrices) {
		if (!checkCompatibility(matrices)) return null;
		Matrix result = new Matrix(matrices[0]);
		for (int i = 1; i < matrices.length; i++) result.add(matrices[i]);
		return result;
	}

	public static Matrix scale(Matrix m, float scalar) {
		Matrix result = new Matrix(m);
		return result.scale(scalar);
	}

	public static Matrix multiply(Matrix m, Matrix n) {
		Matrix result = new Matrix(m);
		return result.multiply(n);
	}

	public static Vector multiply(Matrix m, Vector v) {
		if (!checkCompatibility(m, v)) return null;
		int size = m.getSize();
		float[] dimensions = new float[size];
		for (int r = 0; r < size; r++) {
			float row = 0;
			for (int c = 0; c < size; c++) row += m.matrix[c * size + r] * v.get(c);
			dimensions[r] = row;
		}
		return new Vector(dimensions);
	}

	public static Matrix transpose(Matrix m) {
		Matrix result = new Matrix(m);
		return result.transpose();
	}

	public static boolean checkCompatibility(Matrix... matrices) {
		if (matrices.length == 0) {
			Log.error("Tried to perform matrix operation with no matrices.", new RuntimeException());
			return false;
		} else {
			int size = matrices[0].getSize();
			for (Matrix m : matrices) {
				if (m == null) {
					Log.error("Tried to operate on a null matrix.", new RuntimeException());
					return false;
				} else if (m.getSize() != size) {
					Log.error("Tried to operate on incompatible matrices.", new RuntimeException());
					return false;
				}
			}
			return true;
		}
	}

	public static boolean checkCompatibility(Matrix m, Vector v) {
		if (m == null) {
			Log.error("Tried to operate on a null matrix.", new RuntimeException());
			return false;
		} else if (v == null) {
			Log.error("Tried to operate on a matrix with a null vector.", new RuntimeException());
			return false;
		} else if (m.getSize() != v.getSize()) {
			Log.error("Tried to operate on a " + m.getSize() + "-dimensional matrix with a " + v.getSize() + "-dimensional vector.", new RuntimeException());
			return false;
		} else return true;
	}

	public static boolean equals(Matrix... matrices) {
		if (!checkCompatibility(matrices)) return false;
		float[] matrix = matrices[0].matrix;
		for (int i = 1; i < matrices.length; i++)
			for (int j = 0; j < matrix.length; j++) if (matrix[j] != matrices[i].matrix[j]) return false;
		return true;
	}

	public Matrix add(Matrix m) {
		if (!checkCompatibility(this, m)) return this;
		int size = getSize();
		for (int r = 0; r < size; r++) for (int c = 0; c < size; c++) matrix[c * size + r] += m.matrix[c * size + r];
		return this;
	}

	public Matrix subtract(Matrix m) {
		Matrix m2 = new Matrix(m);
		return add(m2.negate());
	}

	public Matrix scale(float scalar) {
		for (int i = 0; i < matrix.length; i++) matrix[i] *= scalar;
		return this;
	}

	public Matrix multiply(Matrix m) {
		if (!checkCompatibility(this, m)) return this;
		int size = getSize();
		float[] temp = new float[size * size];
		for (int r = 0; r < size; r++) {
			float[] row = new float[size];
			for (int i = 0; i < size; i++) row[i] = matrix[i * size + r];
			for (int c = 0; c < size; c++) {
				float[] col = new float[size];
				for (int i = 0; i < size; i++) col[i] = m.matrix[c * size + i];
				temp[c * size + r] = Vector.dot(new Vector(row), new Vector(col));
			}
		}
		matrix = temp;
		return this;
	}

	public Matrix negate() {
		return scale(-1);
	}

	public Matrix transpose() {
		Matrix temp = new Matrix(this);
		int size = getSize();
		for (int r = 0; r < size; r++) for (int c = 0; c < size; c++) matrix[r * size + c] = temp.matrix[c * size + r];
		return this;
	}

	public Matrix setIdentity() {
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getSize(); j++) {
				if (i == j) matrix[j * getSize() + i] = 1;
				else matrix[j * getSize() + i] = 0;
			}
		}
		return this;
	}

	public int getSize() {
		return (int) Math.sqrt(matrix.length);
	}

	public float get(int r, int c) {
		return matrix[c * getSize() + r];
	}

	public void set(int r, int c, float val) {
		matrix[c * getSize() + r] = val;
	}

	public void toBuffer(FloatBuffer buffer) {
		int size = getSize();
		for (int c = 0; c < size; c++) for (int r = 0; r < size; r++) buffer.put(matrix[c * size + r]);
		buffer.flip();
	}

	public float[] toArray() {
		return matrix;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int r = 0; r < getSize(); r++) {
			String row = "[";
			for (int c = 0; c < getSize(); c++) row += matrix[c * getSize() + r] + ", ";
			sb.append(row.substring(0, row.length() - 2) + "]\n");
		}
		return sb.substring(0, sb.length() - 1);
	}
}
