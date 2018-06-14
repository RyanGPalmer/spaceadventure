package game.world;

import vicinity.Log;
import vicinity.util.Stopwatch;

public class Galaxy extends ProceduralObject {
	private static final int MIN_DISTANCE_BETWEEN_STARS = 4;
	private static final int STAR_SYSTEM_COUNT = 10000;

	public final StarSystem[] starSystems;
	public StarSystem currentStar;

	public Galaxy(Seed seed) {
		super(seed);
		Log.info("Generating galaxy from seed: " + seed.seed);
		Stopwatch sw = new Stopwatch();
		starSystems = generateStarSystems();
		Log.info("Galaxy generation completed in " + sw.getTime() + " seconds.");
		currentStar = starSystems[0];
		goToStar(seed.nextInt(0, STAR_SYSTEM_COUNT));
	}

	private StarSystem[] generateStarSystems() {
		StarSystem[] starSystems = new StarSystem[STAR_SYSTEM_COUNT];
		for (int i = 0; i < STAR_SYSTEM_COUNT; i++) {
			StarSystem s = new StarSystem(seed, i);
			while (isTooClose(starSystems, s)) s = new StarSystem(seed, i);
			starSystems[i] = s;
		}
		return starSystems;
	}

	private static boolean isTooClose(StarSystem[] starSystems, StarSystem s) {
		for (StarSystem s1 : starSystems)
			if (s1 != null && Coordinates.getDistance(s.getCoords(), s1.getCoords()) < MIN_DISTANCE_BETWEEN_STARS)
				return true;
		return false;
	}

	public int getPlanetCount() {
		int total = 0;
		for (StarSystem s : starSystems) total += s.getPlanetCount();
		return total;
	}

	public void goToStar(int id) {
		if (id >= 0 && id < starSystems.length) currentStar = starSystems[id];
		else Log.error("Tried to go to an invalid StarSystem.");
		Log.info("Set current star system: " + currentStar.getName());
	}
}
