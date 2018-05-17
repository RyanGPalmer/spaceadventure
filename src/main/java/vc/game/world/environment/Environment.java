package vc.game.world.environment;

import java.util.List;

import vc.game.utilities.FloatRange;
import vc.game.world.ProceduralObject;
import vc.game.world.Seed;

public class Environment extends ProceduralObject {
	private static final FloatRange MASS_RANGE = new FloatRange(0.1f, 800); // Pluto is 0.02% earth's mass and Jupiter can fit about 1,300 earths inside it
	private static final FloatRange SOLAR_DISTANCE_RANGE = new FloatRange(0.3f, 40); // Mercury is about 1/3 earth's distance, and pluto is about 40 times farther
	private static final FloatRange EDEN_MASS_RANGE = new FloatRange(0.9f, 1.1f);
	private static final FloatRange EDEN_OXYGEN_RANGE = new FloatRange(0.9f, 1.1f);
	private static final FloatRange EDEN_SOLAR_DISTANCE_RANGE = new FloatRange(0.8f, 1.2f);
	private static final float AVG_EARTH_TEMPERATURE_C = 14.6f;
	public static final float EDEN_PROBABILITY = 0.01f;
	private static final int MAX_SPECIES = 10;
	private static final int MIN_SPECIES = 1;

	public final Atmosphere atmosphere;
	public final float mass;
	public final float solarDistance;
	public final List<Species> species;

	public Environment(Seed seed) {
		super(seed);
		if (seed.nextRoll(EDEN_PROBABILITY)) {
			this.atmosphere = new Atmosphere(seed, 0, EDEN_OXYGEN_RANGE);
			this.mass = seed.nextFloat(EDEN_MASS_RANGE);
			this.solarDistance = seed.nextFloat(EDEN_SOLAR_DISTANCE_RANGE);
		} else {
			this.atmosphere = new Atmosphere(seed);
			this.mass = seed.nextFloat(MASS_RANGE);
			this.solarDistance = seed.nextFloat(SOLAR_DISTANCE_RANGE);
		}
		species = Species.generateMultiple(seed.nextInt(MIN_SPECIES, MAX_SPECIES), seed, this);
	}

	public boolean isEden() {
		return EDEN_MASS_RANGE.inRange(getMass()) && EDEN_OXYGEN_RANGE.inRange(getOxygen()) && EDEN_SOLAR_DISTANCE_RANGE.inRange(solarDistance);
	}

	public float getMass() {
		return mass;
	}

	public float getOxygen() {
		return atmosphere.isBreathable() ? atmosphere.getDensity() : 0;
	}
}
