package vicinity;

public abstract class GameObject {
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

	public final void destroy() {
		Game.GAME_OBJECT_MANAGER.deregister(this);
	}
}
