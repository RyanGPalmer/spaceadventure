package vicinity.opengl.rendering;

import vicinity.math.Transform;

public class GLRenderObject {
	private final float[] vertices;

	public final Transform transform;

	public GLRenderObject(float[] vertices) {
		this.vertices = vertices;
		transform = new Transform();
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