package game;

import vicinity.Cube;
import vicinity.Game;
import vicinity.opengl.GLInput;
import vicinity.opengl.GLInputListener;
import vicinity.opengl.GLWindow;
import vicinity.opengl.rendering.GLCamera;
import vicinity.opengl.rendering.GLRenderer;

import java.util.ArrayList;
import java.util.List;

public final class SpaceAdventure extends Game implements GLInputListener {
	private static final String TITLE = "Space Adventure";
	private static final String TEXTURE_PATH = "./res/test.jpg";

	private List<Cube> cubes = new ArrayList<>();
	private final int cubeCount = 15;
	private final float spread = 6.2f;
	private final float speed = 0.7f;
	private long startTime;

	private GLCamera camera = new GLCamera();

	private int xMovement = 0;
	private int yMovement = 0;
	private int zMovement = 0;
	private float movementSpeed = 0.1f;

	protected SpaceAdventure() {
		super(TITLE);
	}

	public static void main(String... args) {
		new SpaceAdventure().start();
	}

	@Override
	protected final void awake() {
		GLInput.addListener(this);
		startTime = System.currentTimeMillis();
		GLRenderer.current().setCamera(camera);
		for (int i = 0; i < cubeCount; i++) {
			Cube cube = new Cube();
			cubes.add(cube);
		}
	}

	@Override
	protected final void tick(final double delta) {
		camera.translate(movementSpeed * xMovement, movementSpeed * yMovement, movementSpeed * zMovement);
		super.tick(delta);
		long elapsed = System.currentTimeMillis() - startTime;
		float f = elapsed * (0.001f * speed);
		int size = cubes.size();
		for (int i = 0; i < size; i++) {
			Cube cube = cubes.get(i);
			float s = (float) Math.sin(f + (float) i / (cubeCount / spread));
			float c = (float) Math.cos(f + (float) i / (cubeCount / spread));
			cube.setPosition(s, c, -4 + (i % 2 == 0 ? c : s));
			cube.rotate(1f, c, s, 0);
			cube.scale(1 + s * 0.005f, 1 + c * 0.005f, 1);
		}
	}

	@Override
	public final void handleInput(GLWindow window, int key, int action) {
		if (key == GLInput.KEY_ESC && action == GLInput.RELEASE) window.close();
		if (key == GLInput.KEY_W && action == GLInput.PRESS) zMovement = 1;
		if (key == GLInput.KEY_W && action == GLInput.RELEASE) zMovement = 0;
		if (key == GLInput.KEY_S && action == GLInput.PRESS) zMovement = -1;
		if (key == GLInput.KEY_S && action == GLInput.RELEASE) zMovement = 0;
		if (key == GLInput.KEY_A && action == GLInput.PRESS) xMovement = 1;
		if (key == GLInput.KEY_A && action == GLInput.RELEASE) xMovement = 0;
		if (key == GLInput.KEY_D && action == GLInput.PRESS) xMovement = -1;
		if (key == GLInput.KEY_D && action == GLInput.RELEASE) xMovement = 0;
		if (key == GLInput.KEY_SPACE && action == GLInput.PRESS) yMovement = -1;
		if (key == GLInput.KEY_SPACE && action == GLInput.RELEASE) yMovement = 0;
		if (key == GLInput.KEY_LEFT_CTRL && action == GLInput.PRESS) yMovement = 1;
		if (key == GLInput.KEY_LEFT_CTRL && action == GLInput.RELEASE) yMovement = 0;
	}
}
