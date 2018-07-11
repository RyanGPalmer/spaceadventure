package game;

import vicinity.Cube;
import vicinity.Game;

import java.util.ArrayList;
import java.util.List;

public final class SpaceAdventure extends Game {
	private static final String TITLE = "Space Adventure";
	private static final String TEXTURE_PATH = "./res/test.jpg";

	private List<Cube> cubes = new ArrayList<>();
	private long startTime;

	protected SpaceAdventure() {
		super(TITLE);
	}

	public static void main(String... args) {
		new SpaceAdventure().start();
	}

	@Override
	protected final void awake() {
		startTime = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			Cube cube = new Cube();
			cube.setRotation(90, 1, 0, 0);
			cubes.add(cube);
		}
	}

	@Override
	protected final void tick(final double delta) {
		super.tick(delta);
		long elapsed = System.currentTimeMillis() - startTime;
		float f = elapsed * 0.003f;
		int size = cubes.size();
		for (int i = 0; i < size; i++) {
			Cube cube = cubes.get(i);
			float s = (float) Math.sin(f);
			float c = (float) Math.cos(f);
			cube.setPosition((0.4f - (float) i / size) * 3f, (0.4f - (float) i / size) * 3f, -4 - (float) i / size);
			cube.setRotation(90, s, c, c * s);
		}
	}
}
