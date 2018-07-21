import java.util.ArrayList;
import java.util.List;

import vicinity.math.Matrix;
import vicinity.math.Matrix4;
import vicinity.math.Vector3;
import game.world.Coordinates;
import game.world.environment.Environment;
import game.world.Planet;
import game.world.Seed;

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
		Vector3 a = new Vector3(1, 2, 20);
		Vector3 b = new Vector3(3, -2, -7);
		Vector3 c = new Vector3(-5, 12, 0);
		assertEquals(-63, Vector3.dot(a, b, c), 0.00001);
		assertEquals(23, Vector3.dot(a), 0.00001);
		Vector3 q = Vector3.add(a, b, c);
		Vector3 u = new Vector3(-1, 12, 13);
		assertVector3Equals(u, q);
		assertVector3Equals(new Vector3(-1, 12, 13), Vector3.add(a, b, c));
		assertEquals(u.toString(), q.toString());
		q = Vector3.subtract(c, b, a);
		u = new Vector3(-9, 12, -13);
		assertVector3Equals(q, u);
		assertVector3Equals(new Vector3(-9, 12, -13), Vector3.subtract(c, b, a));

		a = new Vector3(1, -7, 1);
		b = new Vector3(5, 2, 4);
		c = new Vector3(-30, 1, 37);
		q = Vector3.cross(a, b);
		assertVector3Equals(c, Vector3.cross(a, b));
		// Since Q is orthogonal to A and B, the dot product of Q and A or B should be 0
		assertEquals(0, Vector3.dot(a, q), 0.0000001);
		assertEquals(0, Vector3.dot(b, q), 0.0000001);

		a = new Vector3(0, 0, 287);
		b = new Vector3(0, 0, 1);
		c = Vector3.getNormalized(a);
		assertEquals(b.toString(), c.toString());
		assertVector3Equals(b, c);
	}

	private void assertVector3Equals(Vector3 expected, Vector3 actual) {
		assertNotNull(expected);
		assertNotNull(actual);
		for (int i = 0; i < 3; i++) assertEquals(expected.get(i), actual.get(i));
	}

	@Test
	public void matrixMath() {
		Matrix4 m = new Matrix4(new float[]{
				1, 2, 3, 1,
				4, 5, 6, 1,
				7, 8, 9, 1,
				6, 6, 6, 1});
		assertEquals(1, m.get(0, 0));
		assertEquals(2, m.get(0, 1));
		assertEquals(3, m.get(0, 2));
		assertEquals(1, m.get(0, 3));
		assertEquals(4, m.get(1, 0));
		assertEquals(5, m.get(1, 1));
		assertEquals(6, m.get(1, 2));
		assertEquals(1, m.get(1, 3));
		assertEquals(7, m.get(2, 0));
		assertEquals(8, m.get(2, 1));
		assertEquals(9, m.get(2, 2));
		assertEquals(1, m.get(2, 3));
		assertEquals(6, m.get(3, 0));
		assertEquals(6, m.get(3, 1));
		assertEquals(6, m.get(3, 2));
		assertEquals(1, m.get(3, 3));

		Matrix4 result = new Matrix4(new float[]{
				2, 4, 6, 2,
				8, 10, 12, 2,
				14, 16, 18, 2,
				12, 12, 12, 2});
		assertMatrixEquals(result, Matrix4.scale(m, 2));

		result = new Matrix4(new float[]{
				1, 4, 7, 6,
				2, 5, 8, 6,
				3, 6, 9, 6,
				1, 1, 1, 1});
		assertMatrixEquals(result, Matrix4.transpose(m));

		Matrix4 n = new Matrix4(new float[]{
				7, 2, 9, 2,
				-6, 0, 8, 2,
				0, -1, -1, 0,
				-2, 4, 3, 5});

		result = new Matrix4(new float[]{
				-7, 3, 25, 11,
				-4, 6, 73, 23,
				-1, 9, 121, 35,
				4, 10, 99, 29});
		Matrix4 mult = Matrix4.multiply(m, n);
		assertMatrixEquals(result, mult);

		result = new Matrix4(new float[]{
				8, 4, 12, 3,
				-2, 5, 14, 3,
				7, 7, 8, 1,
				4, 10, 9, 6});
		assertMatrixEquals(result, Matrix4.add(m, n));
	}

	private void assertMatrixEquals(Matrix4 expected, Matrix4 actual) {
		assertNotNull(expected);
		assertNotNull(actual);
		for (int r = 0; r < 4; r++)
			for (int c = 0; c < 4; c++) assertEquals(expected.get(r, c), actual.get(r, c));
	}
}
