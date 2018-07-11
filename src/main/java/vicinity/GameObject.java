package vicinity;

import vicinity.opengl.rendering.GLRenderObject;

public abstract class GameObject {
	private GLRenderObject model;

	protected GameObject() {
		init();
		Game.GAME_OBJECT_MANAGER.register(this);
	}

	protected abstract void init();

	public void earlyTick(final double delta) {
	}

	public abstract void tick(final double delta);

	public void lateTick(final double delta) {
	}

	public void translate(float x, float y, float z) {
		model.translate(x, y, z);
	}

	public void setPosition(float x, float y, float z) {
		model.setPosition(x, y, z);
	}

	public void setRotation(float angle, float x, float y, float z) {
		model.setRotation(angle, x, y, z);
	}

	public void scale(float scale) {
		model.scale(scale);
	}

	public void setModel(GLRenderObject model) {
		if (this.model != null) this.model.deregister();
		this.model = model;
		this.model.register();
	}

	public final void destroy() {
		Game.GAME_OBJECT_MANAGER.deregister(this);
	}
}
