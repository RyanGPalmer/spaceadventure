package vc.game.world.environment;

import vc.game.utilities.FloatRange;
import vc.game.utilities.Type;
import vc.game.utilities.TypeOption;
import vc.game.world.ProceduralObject;
import vc.game.world.Seed;

public class Atmosphere extends ProceduralObject {
	private static final FloatRange DENSITY_RANGE = new FloatRange(0, 3);

	private static final TypeOption[] TYPES = {
			new TypeOption("oxygen", 0),
			new TypeOption("carbon dioxide", 1),
			new TypeOption("nitrogen", 2),
			new TypeOption("ozone", 3),
			new TypeOption("argon", 4),
			new TypeOption("hydrogen", 5),
			new TypeOption("helium", 6),
			new TypeOption("methane", 7),
	};

	private final Type type = new Type(TYPES);
	private final float density;

	public Atmosphere(Seed seed) {
		this(seed, seed.nextInt(0, TYPES.length - 1), DENSITY_RANGE);
	}

	public Atmosphere(Seed seed, int type, FloatRange densityRange) {
		super(seed);
		this.type.set(type);
		this.density = seed.nextFloat(densityRange);
	}

	public boolean isBreathable() {
		return type.equals(0);
	}

	public String getType() {
		return type.getName();
	}

	public int getTypeCode() {
		return type.getCode();
	}

	public float getDensity() {
		return density;
	}
}
