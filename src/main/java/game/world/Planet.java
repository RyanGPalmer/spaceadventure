package game.world;

import java.util.ArrayList;
import java.util.List;

import game.Infoable;
import game.utilities.Util;
import game.world.environment.Environment;
import game.world.infrastructure.Settlement;

public class Planet extends ProceduralObject implements Infoable {
	private static final String NAME = "Name: ";
	private static final String ATMOSPHERE_TYPE = "Atmosphere Type: ";
	private static final String ATMOSPHERE_DENSITY = "Atmosphere Density: ";
	private static final String MASS = "Mass: ";
	private static final String SOLAR_DISTANCE = "Distance From Star: ";
	private static final String SETTLEMENTS = "Settlements: ";

	private static final int PREFIX_LENGTH = 2;
	private static final int NUMBER_LENGTH = 7;
	private static final int BASE_REWARD = 100;
	private static final float EARTH_SOLAR_DISTANCE_MILLION_KM = 149.6f;

	public final String name;
	public final Environment environment;
	public final List<Settlement> settlements = new ArrayList<>();

	public boolean isConquered = false;

	public Planet(Seed seed) {
		super(seed);
		this.name = generateName();
		this.environment = new Environment(seed);
	}

	private String generateName() {
		String name = "";
		for (int i = 0; i < PREFIX_LENGTH; i++) name += seed.nextLetter().toUpperCase();
		name += "-";
		for (int i = 0; i < NUMBER_LENGTH; i++) name += seed.nextInt(0, 9);
		if (seed.nextBool())
			name += "-" + (seed.nextBool() ? seed.nextLetter().toUpperCase() : seed.nextCallsign());
		return name;
	}

	public int getReward() {
		return BASE_REWARD * getRewardFactor();
	}

	@Override
	public String getInfo() {
		return isConquered ? getManagementInfo() : getConquestInfo();
	}

	private String getConquestInfo() {
		String info = NAME + name + (environment.isEden() ? " (Eden Planet)\n" : '\n');
		info += ATMOSPHERE_TYPE + Util.capitalizeAll(environment.atmosphere.getType()) + '\n';
		info += ATMOSPHERE_DENSITY + Util.asPercent(environment.atmosphere.getDensity()) + '\n';
		info += MASS + Util.asPercent(environment.mass) + " Earth mass\n";
		info += SOLAR_DISTANCE + (int) (environment.solarDistance * EARTH_SOLAR_DISTANCE_MILLION_KM) + "m km";
		return info;
	}

	private String getManagementInfo() {
		String info = getConquestInfo();
		info += '\n' + SETTLEMENTS + settlements.size();
		for (Settlement s : settlements) info += "\n" + s.getInfo();
		return info;
	}

	public Settlement addSettlement(String name) {
		Settlement s = new Settlement(name);
		settlements.add(s);
		return s;
	}

	public int getRewardFactor() {
		// TODO Make this meaningful
		float d = 0.81f;
		if (d > 0.95) return 50;
		if (d > 0.90) return 20;
		if (d > 0.80) return 10;
		if (d > 0.70) return 8;
		if (d > 0.60) return 6;
		if (d > 0.50) return 4;
		if (d > 0.40) return 3;
		if (d > 0.30) return 2;
		else return 1;
	}
}
