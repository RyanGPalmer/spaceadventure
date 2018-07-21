package vicinity.math;

import vicinity.Log;

public class Vector4 {
	private float[] values;

	public Vector4() {
		values = new float[4];
	}

	public Vector4(float... values) {
		this.values = new float[4];
		if (values.length == 4) for (int i = 0; i < 4; i++) this.values[i] = values[i];
		else Log.error("Tried to initialize a Vector3 with " + values.length + " values.");
	}

	public static float dot(Vector4... vectors) {
		float result = 0;
		for (int i = 0; i < 4; i++) {
			float dimensionalResult = vectors[0].get(i);
			for (int j = 1; j < vectors.length; j++) dimensionalResult *= vectors[j].get(i);
			result += dimensionalResult;
		}
		return result;
	}

	public float get(int index) {
		try {
			return values[index];
		} catch (IndexOutOfBoundsException e) {
			Log.error("Tried to access invalid index (" + index + ") of a Vector4.", e);
			return 0;
		}
	}
}
