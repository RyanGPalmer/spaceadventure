package vicinity.math;

public class VTransform {
	private Vector3 position;
	private Vector3 target;

	public VTransform() {
		position = new Vector3();
		target = new Vector3();
	}

	public void translate(float x, float y, float z) {
		position.add(new Vector3(x, y, z));
	}

	public void setPosition(float x, float y, float z) {
		position = new Vector3(x, y, z);
	}

	public void setTarget(float x, float y, float z) {
		target = new Vector3(x, y, z);
	}

	public Matrix4 getLookAtMatrix() {
		return Matrix4.lookAt(position, target);
	}
}
