package vc.game;

import vc.engine.GameObject;

public class TestObject extends GameObject {
	private final int num;

	public TestObject(int num) {
		this.num = num;
	}

	@Override
	protected void init() {
	}

	@Override
	public void tick(final double delta) {
		for (int i = 0; i < num; i++) {
			int j = 2 + 2;
		}
	}
}
