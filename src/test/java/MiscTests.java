import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import vc.game.world.Coordinates;
import vc.game.world.environment.Environment;
import vc.game.world.Planet;
import vc.game.world.Seed;

public class MiscTests {
	@Test
	public void testCoordinates() {
		Coordinates c1 = new Coordinates(2387456, 3456827, 9374856);
		Coordinates c2 = new Coordinates(1387456, 3456827, 9374856);
		Assert.assertEquals(1000000, Coordinates.getDistance(c1, c2), 0.0001d);
	}

	@Test
	public void testSeedConsistency() {
		int runs = 100;
		float rollProbability = 0.77f;
		int[] nums = new int[runs];
		float[] floats = new float[runs];
		boolean[] bools = new boolean[runs];
		boolean[] rolls = new boolean[runs];
		String[] letters = new String[runs];
		String[] callsigns = new String[runs];

		Seed seed = Seed.generate();
		for (int i = 0; i < runs; i++) {
			nums[i] = seed.nextInt();
			floats[i] = seed.nextFloat();
			bools[i] = seed.nextBool();
			rolls[i] = seed.nextRoll(rollProbability);
			letters[i] = seed.nextLetter();
			callsigns[i] = seed.nextCallsign();
		}

		seed = new Seed(seed.seed);
		for (int i = 0; i < runs; i++) {
			Assert.assertEquals(nums[i], seed.nextInt());
			Assert.assertEquals(floats[i], seed.nextFloat(), 0.00001f);
			Assert.assertEquals(bools[i], seed.nextBool());
			Assert.assertEquals(rolls[i], seed.nextRoll(rollProbability));
			Assert.assertEquals(letters[i], seed.nextLetter());
			Assert.assertEquals(callsigns[i], seed.nextCallsign());
		}
	}

	@Test
	public void confirmProbabilityOfEdenPlanet() {
		Seed seed = Seed.generate();
		List<Integer> triesList = new ArrayList<>();
		int tries = 0;
		int edenCount = 0;
		while (edenCount < 10000) {
			Planet p = new Planet(seed);
			if (p.environment.isEden()) {
				triesList.add(tries);
				tries = 0;
				edenCount++;
			}
			tries++;
		}

		long totalTries = 0;
		for (int i : triesList) totalTries += i;
		int avgTries = (int) (totalTries / triesList.size());
		double probability = 1d / avgTries;

		Assert.assertEquals(Environment.EDEN_PROBABILITY, probability, 0.001);
	}
}
