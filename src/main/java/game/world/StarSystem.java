package game.world;

public class StarSystem extends ProceduralObject {
	private static final int PREFIX_LENGTH = 2;
	private static final int NUMBER_LENGTH = 7;
	private static final int MIN_PLANETS = 3;
	private static final int MAX_PLANETS = 10;

	private final Planet[] planets;
	private final Coordinates coords;
	private final String name;
	public final int id;

	public StarSystem(Seed seed, int id) {
		super(seed);
		this.id = id;
		name = generateName();
		planets = generatePlanets();
		coords = Coordinates.generate(seed);
	}

	private Planet[] generatePlanets() {
		int planetCount = seed.nextInt(MIN_PLANETS, MAX_PLANETS);
		Planet[] planets = new Planet[planetCount];
		for (int i = 0; i < planetCount; i++) planets[i] = new Planet(seed);
		return planets;
	}

	private String generateName() {
		String name = "";
		for (int i = 0; i < PREFIX_LENGTH; i++) name += seed.nextLetter().toUpperCase();
		name += "-";
		for (int i = 0; i < NUMBER_LENGTH; i++) name += seed.nextInt();
		if (seed.nextBool())
			name += "-" + (seed.nextBool() ? seed.nextLetter().toUpperCase() : seed.nextCallsign());
		return name;
	}

	public int getPlanetCount() {
		return planets.length;
	}

	public Coordinates getCoords() {
		return coords;
	}

	public String getName() {
		return name;
	}
}
