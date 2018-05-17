package vc.game.world;

import java.io.Serializable;
import java.util.Random;

import vc.game.utilities.Util;
import vc.game.utilities.FloatRange;

public class Seed implements Serializable {
	private final Random random;
	public final long seed;

	public Seed(long seed) {
		this.seed = seed;
		random = new Random(seed);
	}

	public static Seed generate() {
		return new Seed(new Random().nextLong());
	}

	public int nextInt(int min, int max) {
		return min + random.nextInt(max - min);
	}

	public int nextInt() {
		return nextInt(0, 10);
	}

	public float nextFloat(FloatRange r) {
		return nextInt((int) (r.min * 1000000), (int) (r.max * 1000000) + 1) / 1000000f;
	}

	public float nextFloat() {
		return nextFloat(new FloatRange(0, 1));
	}

	public boolean nextBool() {
		return random.nextBoolean();
	}

	public boolean nextRoll(float probability) {
		if (probability >= 1) return true;
		if (probability <= 0) return false;
		float roll = nextFloat();
		return roll <= probability;
	}

	public String nextLetter() {
		return "" + (char) nextInt('a', 'z' + 1);
	}

	public String nextCallsign() {
		return Util.CALLSIGNS[nextInt(0, Util.CALLSIGNS.length)];
	}
}
