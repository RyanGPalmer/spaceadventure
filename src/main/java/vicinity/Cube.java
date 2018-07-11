package vicinity;

import vicinity.opengl.rendering.GLCube;

public class Cube extends GameObject {
	@Override
	protected void init() {
		setModel(new GLCube());
	}

	@Override
	public void tick(double delta) {

	}
}
