package vc.game;

import vc.engine.Game;
import vc.engine.Texture2D;

public final class SpaceAdventure extends Game {
	private static final String TITLE = "Space Adventure";
	private static final String TEXTURE_PATH = "./res/test.jpg";

	private Texture2D texture;

	protected SpaceAdventure() {
		super(TITLE);
	}

	public static void main(String... args) {
		new SpaceAdventure().start();
	}

	@Override
	protected final void awake() {
		texture = Texture2D.load(TEXTURE_PATH);
		for (int i = 0; i < 10; i++) new TestObject(i + 1);
	}

	@Override
	protected final void tick() {
	}

	@Override
	protected final void beforeExit() {
	}
}
