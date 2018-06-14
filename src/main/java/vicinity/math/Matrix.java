package vicinity.math;

import vicinity.Log;

import java.nio.FloatBuffer;

/**
 * Generic matrix capable of representing any n-dimensional matrix.
 *
 * @author Ryan Palmer
 */
public class Matrix {
	private float[][] matrix;

	public Matrix(int size) {
		matrix = new float[size][size];
		setIdentity();
	}

	public Matrix(Matrix m) {
		int size = m.getSize();
		matrix = new float[size][size];
		for (int r = 0; r < size; r++) for (int c = 0; c < size; c++) matrix[r][c] = m.matrix[r][c];
	}

	public Matrix(Vector... vectors) {
		if (!Vector.checkCompatibility(vectors)) {
			Log.error("Tried to construct matrix from incompatible vectors.", new RuntimeException());
			matrix = new float[3][3];
			setIdentity();
		} else if (vectors.length != vectors[0].getSize()) {
			Log.error("Tried to construct " + vectors.length + "-dimensional matrix with " + vectors[0].getSize() + "-dimensional vectors.", new RuntimeException());
			matrix = new float[3][3];
			setIdentity();
		} else {
			int size = vectors.length;
			matrix = new float[size][size];
			for (int i = 0; i < size; i++) matrix[i] = vectors[i].toArray();
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
			for (int c = 0; c < size; c++) row += m.matrix[r][c] * v.get(c);
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
		float[][] matrix = matrices[0].matrix;
		for (int i = 1; i < matrices.length; i++)
			for (int r = 0; r < matrix.length; r++)
				for (int c = 0; c < matrix.length; c++) if (matrix[r][c] != matrices[i].matrix[r][c]) return false;
		return true;
	}

	public Matrix add(Matrix m) {
		if (!checkCompatibility(this, m)) return this;
		int size = getSize();
		for (int r = 0; r < size; r++) for (int c = 0; c < size; c++) matrix[r][c] += m.matrix[r][c];
		return this;
	}

	public Matrix subtract(Matrix m) {
		Matrix m2 = new Matrix(m);
		return add(m2.negate());
	}

	public Matrix scale(float scalar) {
		for (int r = 0; r < getSize(); r++) for (int c = 0; c < getSize(); c++) matrix[r][c] *= scalar;
		return this;
	}

	public Matrix multiply(Matrix m) {
		if (!checkCompatibility(this, m)) return this;
		int size = getSize();
		float[][] temp = new float[size][size];
		for (int r = 0; r < size; r++) {
			Vector row = new Vector(matrix[r]);
			for (int c = 0; c < size; c++) {
				float[] colVals = new float[size];
				for (int i = 0; i < size; i++) colVals[i] = m.matrix[i][c];
				Vector col = new Vector(colVals);
				temp[r][c] = Vector.dot(row, col);
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
		for (int r = 0; r < size; r++) for (int c = 0; c < size; c++) matrix[r][c] = temp.matrix[c][r];
		return this;
	}

	public Matrix setIdentity() {
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getSize(); j++) {
				if (i == j) matrix[i][j] = 1;
				else matrix[i][j] = 0;
			}
		}
		return this;
	}

	public int getSize() {
		return matrix.length;
	}

	public float get(int r, int c) {
		return matrix[r][c];
	}

	public void set(int r, int c, float val) {
		matrix[r][c] = val;
	}

	public void toBuffer(FloatBuffer buffer) {
		int size = getSize();
		for (int c = 0; c < size; c++) for (int r = 0; r < size; r++) buffer.put(matrix[r][c]);
		buffer.flip();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int r = 0; r < getSize(); r++) {
			String row = "[";
			for (int c = 0; c < getSize(); c++) row += matrix[c][r] + ", ";
			sb.append(row.substring(0, row.length() - 2) + "]\n");
		}
		return sb.substring(0, sb.length() - 1);
	}
}
