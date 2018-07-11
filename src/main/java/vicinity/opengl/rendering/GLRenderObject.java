package vicinity.opengl.rendering;

import vicinity.math.Matrix4;
import vicinity.math.Vector3;
import vicinity.math.Vector4;

public class GLRenderObject {
	private final float[] vertices;
	private Vector3 position;
	private Vector4 rotation;
	private Vector3 scale;

	public GLRenderObject(float[] vertices) {
		this.vertices = vertices;
		position = new Vector3(0, 0, 0);
		rotation = new Vector4(0, 0, 0, 0);
		scale = new Vector3(1, 1, 1);
	}

	public void translate(float x, float y, float z) {
		position.add(new Vector3(x, y, z));
	}

	public void setPosition(float x, float y, float z) {
		position = new Vector3(x, y, z);
	}

	public void setRotation(float angle, float x, float y, float z) {
		rotation = new Vector4(x, y, z, angle);
	}

	public void scale(float scale) {
		this.scale.scale(scale);
	}

	public float[] getModelView() {
		Matrix4 translation = Matrix4.translate(position.getX(), position.getY(), position.getZ());
		Matrix4 rotation = Matrix4.rotate(this.rotation.getW(), this.rotation.getX(), this.rotation.getY(), this.rotation.getZ());
		return translation.multiply(rotation).scale(scale.getLength()).toArray();
	}

	public float[] getVertices() {
		return vertices;
	}

	public void register() {
		GLRenderer.current().registerObject(this);
	}

	public void deregister() {
		GLRenderer.current().deregisterObject(this);
	}
}
