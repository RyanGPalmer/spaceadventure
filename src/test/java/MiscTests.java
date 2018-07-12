import java.util.ArrayList;
import java.util.List;

import vicinity.math.Matrix;
import vicinity.math.Vector;
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
		assertVectorEquals(u, q);
		assertVectorEquals(new Vector(-1, 12, 13), Vector.add(a, b, c));
		assertEquals(u.toString(), q.toString());
		q = Vector.subtract(c, b, a);
		u = new Vector(-9, 12, -13);
		assertVectorEquals(q, u);
		assertVectorEquals(new Vector(-9, 12, -13), Vector.subtract(c, b, a));

		a = new Vector(1, 20);
		b = new Vector(3, -2, -7);
		assertFalse(Vector.checkCompatibility(a, b));
		assertFalse(Vector.checkCompatibility(a, null));
		assertFalse(Vector.checkCompatibility());
		assertFalse(Vector.checkCompatibility(b, b, b, b, a));

		a = new Vector(2, 2, 2, 4, 4, 6, 8);
		assertEquals(12, a.getLength(), 0.00001);
		b = new Vector(4, 4, 4, 8, 8, 12, 16);
		assertVectorEquals(b, Vector.scale(a, 2));
		a.scale(2);
		assertEquals(a.toString(), b.toString());
		assertVectorEquals(a, b);

		a = new Vector(1, -7, 1);
		b = new Vector(5, 2, 4);
		c = new Vector(-30, 1, 37);
		q = Vector.cross(a, b);
		q = Vector.cross(a, b);
		assertVectorEquals(c, Vector.cross(a, b));
		// Since Q is orthogonal to A and B, the dot product of Q and A or B should be 0
		assertEquals(0, Vector.dot(a, q), 0.0000001);
		assertEquals(0, Vector.dot(b, q), 0.0000001);

		a = new Vector(0, 0, 287);
		b = new Vector(0, 0, 1);
		c = Vector.getNormalized(a);
		assertEquals(b.toString(), c.toString());
		assertVectorEquals(b, c);
	}

	private void assertVectorEquals(Vector expected, Vector actual) {
		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected.getSize(), actual.getSize());
		int size = expected.getSize();
		for (int i = 0; i < size; i++) assertEquals(expected.get(i), actual.get(i));
	}

	@Test
	public void matrixMath() {
		Vector x = new Vector(1, 2, 3, 1);
		Vector y = new Vector(4, 5, 6, 1);
		Vector z = new Vector(7, 8, 9, 1);
		Vector w = new Vector(6, 6, 6, 1);
		Matrix m = new Matrix(x, y, z, w);
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

		Vector h = new Vector(2, 4, 6, 2);
		Vector i = new Vector(8, 10, 12, 2);
		Vector j = new Vector(14, 16, 18, 2);
		Vector k = new Vector(12, 12, 12, 2);
		Matrix result = new Matrix(h, i, j, k);
		assertMatrixEquals(result, Matrix.scaleAll(m, 2));

		h = new Vector(1, 4, 7, 6);
		i = new Vector(2, 5, 8, 6);
		j = new Vector(3, 6, 9, 6);
		k = new Vector(1, 1, 1, 1);
		result = new Matrix(h, i, j, k);
		assertMatrixEquals(result, Matrix.transpose(m));

		Vector a = new Vector(7, 2, 9, 2);
		Vector b = new Vector(-6, 0, 8, 2);
		Vector c = new Vector(0, -1, -1, 0);
		Vector d = new Vector(-2, 4, 3, 5);
		Matrix n = new Matrix(a, b, c, d);

		h = new Vector(-7, 3, 25, 11);
		i = new Vector(-4, 6, 73, 23);
		j = new Vector(-1, 9, 121, 35);
		k = new Vector(4, 10, 99, 29);
		result = new Matrix(h, i, j, k);
		Matrix mult = Matrix.multiply(m, n);
		assertMatrixEquals(result, mult);

		i = new Vector(204, 687, 1170, 997);
		assertVectorEquals(i, Matrix.multiply(result, a));

		h = new Vector(1, 0, 0, 0, 0);
		i = new Vector(0, 1, 0, 0, 0);
		j = new Vector(0, 0, 1, 0, 0);
		k = new Vector(0, 0, 0, 1, 0);
		a = new Vector(0, 0, 0, 0, 1);
		result = new Matrix(h, i, j, k, a);
		assertMatrixEquals(result, new Matrix(5));

		h = new Vector(8, 4, 12, 3);
		i = new Vector(-2, 5, 14, 3);
		j = new Vector(7, 7, 8, 1);
		k = new Vector(4, 10, 9, 6);
		result = new Matrix(h, i, j, k);
		assertMatrixEquals(result, Matrix.add(m, n));
	}

	private void assertMatrixEquals(Matrix expected, Matrix actual) {
		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected.getSize(), actual.getSize());
		int size = expected.getSize();
		for (int r = 0; r < size; r++)
			for (int c = 0; c < size; c++) assertEquals(expected.get(r, c), actual.get(r, c));
	}
}
