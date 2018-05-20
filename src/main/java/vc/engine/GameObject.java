package vc.engine;

public abstract class GameObject {
	protected GameObject() {
		init();
		Game.gameObjectManager.register(this);
	}

	protected abstract void init();

	public void earlyTick() {
	}

	public abstract void tick();

	public void lateTick() {
	}

	public final void destroy() {
		Game.gameObjectManager.deregister(this);
	}
}
