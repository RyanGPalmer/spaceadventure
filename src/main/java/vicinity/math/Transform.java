package vicinity.math;

public class Transform {
	private Matrix4 translation;
	private Matrix4 rotation;
	private Matrix4 scale;

	public Transform() {
		translation = new Matrix4();
		rotation = new Matrix4();
		scale = new Matrix4();
	}

	public void translate(float x, float y, float z) {
		translation.multiply(Matrix4.translate(x, y, z));
	}

	public void rotate(float angle, float x, float y, float z) {
		rotation.multiply(Matrix4.rotate(angle, x, y, z));
	}

	public void scale(float x, float y, float z) {
		scale.multiply(Matrix4.scale(x, y, z));
	}

	public void setTranslation(float x, float y, float z) {
		translation = Matrix4.translate(x, y, z);
	}

	public void setRotation(float angle, float x, float y, float z) {
		rotation = Matrix4.rotate(angle, x, y, z);
	}

	public void setScale(float x, float y, float z) {
		scale = Matrix4.scale(x, y, z);
	}

	public Matrix4 getTranslation() {
		return translation;
	}

	public Matrix4 getRotation() {
		return rotation;
	}

	public Matrix4 getScale() {
		return scale;
	}

	public Matrix4 getTransformationMatrix() {
		return Matrix4.multiply(translation, rotation).multiply(scale);
	}

	public Matrix4 getViewMatrix() {
		return Matrix4.multiply(rotation, translation);
	}
}
