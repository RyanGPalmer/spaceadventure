package vc.game.world.environment;

import java.util.ArrayList;
import java.util.List;

import vc.game.world.ProceduralObject;
import vc.game.world.Seed;

public class Species extends ProceduralObject {
	public Species(Seed seed) {
		super(seed);
	}

	public static List<Species> generateMultiple(int count, Seed seed, Environment environment) {
		List<Species> species = new ArrayList<>();
		for (int i = 0; i < count; i++) species.add(new Species(seed));
		return species;
	}
}
