import java.util.ArrayList;
import java.util.List;

import vc.engine.math.Vector;
import vc.game.world.Coordinates;
import vc.game.world.environment.Environment;
import vc.game.world.Planet;
import vc.game.world.Seed;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MiscTests {
	@Test
	public void testCoordinates() {
		Coordinates c1 = new Coordinates(2387456, 3456827, 9374856);
		Coordinates c2 = new Coordinates(1387456, 3456827, 9374856);
		assertEquals(1000000, Coordinates.getDistance(c1, c2), 0.0001);
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
			assertEquals(nums[i], seed.nextInt());
			assertEquals(floats[i], seed.nextFloat(), 0.00001);
			assertEquals(bools[i], seed.nextBool());
			assertEquals(rolls[i], seed.nextRoll(rollProbability));
			assertEquals(letters[i], seed.nextLetter());
			assertEquals(callsigns[i], seed.nextCallsign());
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

		assertEquals(Environment.EDEN_PROBABILITY, probability, 0.001);
	}

	@Test
	public void vectorMath() {
		Vector v = new Vector(2, 8);
		Vector w = new Vector(-3, 4);
		double result = Vector.dot(v, w);
		assertEquals(26, result, 0.00001);

		Vector a = new Vector(1, 2, 20);
		Vector b = new Vector(3, -2, -7);
		Vector c = new Vector(-5, 12, 0);
		assertTrue(Vector.checkCompatibility(a, b, c));
		assertEquals(-63, Vector.dot(a, b, c), 0.00001);
		assertEquals(23, Vector.dot(a), 0.00001);
		Vector q = Vector.add(a, b, c);
		Vector u = new Vector(-1, 12, 13);
		assertTrue(Vector.equals(u, q));
		assertTrue(Vector.equals(new Vector(-1, 12, 13), Vector.add(a, b, c)));
		assertEquals(u.toString(), q.toString());
		q = Vector.subtract(c, b, a);
		u = new Vector(-9, 12, -13);
		assertTrue(Vector.equals(q, u));
		assertTrue(Vector.equals(new Vector(-9, 12, -13), Vector.subtract(c, b, a)));

		a = new Vector(1, 20);
		b = new Vector(3, -2, -7);
		assertFalse(Vector.checkCompatibility(a, b));
		assertFalse(Vector.checkCompatibility(a, null));
		assertFalse(Vector.checkCompatibility());
		assertFalse(Vector.checkCompatibility(b, b, b, b, a));

		a = new Vector(2, 2, 2, 4, 4, 6, 8);
		assertEquals(12, a.getLength(), 0.00001);
		b = new Vector(4, 4, 4, 8, 8, 12, 16);
		assertTrue(Vector.equals(b, Vector.scale(a, 2)));
		a.scale(2);
		assertEquals(a.toString(), b.toString());
		assertTrue(Vector.equals(a, b));

		a = new Vector(1, -7, 1);
		b = new Vector(5, 2, 4);
		c = new Vector(-30, 1, 37);
		q = Vector.cross(a, b);
		q = Vector.cross(a, b);
		assertTrue(Vector.equals(c, Vector.cross(a, b)));
		// Since Q is orthogonal to A and B, the dot product of Q and A or B should be 0
		assertEquals(0, Vector.dot(a, q), 0.0000001);
		assertEquals(0, Vector.dot(b, q), 0.0000001);

		a = new Vector(0, 0, 287);
		b = new Vector(0, 0, 1);
		c = Vector.getNormalized(a);
		assertEquals(b.toString(), c.toString());
		assertTrue(Vector.equals(b, c));
	}
}
