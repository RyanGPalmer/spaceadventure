package vicinity.opengl;

public abstract class GLObject {
	protected final int id;

	protected GLObject(int id) {
		this.id = id;
	}

	public abstract void bind();

	public abstract void unbind();

	public abstract void delete();
}
