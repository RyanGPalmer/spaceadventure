package game;

import vicinity.Game;

public final class SpaceAdventure extends Game {
	private static final String TITLE = "Space Adventure";
	private static final String TEXTURE_PATH = "./res/test.jpg";

	protected SpaceAdventure() {
		super(TITLE);
	}

	public static void main(String... args) {
		new SpaceAdventure().start();
	}

	@Override
	protected final void awake() {
		for (int i = 0; i < 10000; i++) new TestObject(i + 1);
	}
}
