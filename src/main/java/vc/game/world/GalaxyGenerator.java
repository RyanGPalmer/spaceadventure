package vc.game.world;

import vc.game.utilities.tasks.Task;

public class GalaxyGenerator extends Task<Galaxy> {
	private final Seed seed;

	public GalaxyGenerator(Seed seed) {
		this.seed = seed;
	}

	@Override
	protected Galaxy runTask() {
		setMessage("Generating galaxy from seed: " + seed.seed);
		Galaxy galaxy = new Galaxy(seed);
		return galaxy;
	}
}
