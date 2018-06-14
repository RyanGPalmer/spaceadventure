package game.world;

public class Coordinates {
	private static final int MAX_COORDS = 1000;

	public final int x;
	public final int y;
	public final int z;

	public Coordinates(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static Coordinates generate(Seed seed) {
		return new Coordinates(seed.nextInt(0, MAX_COORDS), seed.nextInt(0, MAX_COORDS), seed.nextInt(0, MAX_COORDS));
	}

	// The distance between two points P1(x1, y1, z1) and P2(x2, y2, z2)
	// d(P1, P2) = sqrt((x2 - x1)^2 + (y2 - y1)^2 + (z2 - z1)^2)
	public static double getDistance(Coordinates a, Coordinates b) {
		return Math.sqrt(Math.pow((b.x - a.x), 2) + Math.pow((b.y - a.y), 2) + Math.pow((b.z - a.z), 2));
	}
}
