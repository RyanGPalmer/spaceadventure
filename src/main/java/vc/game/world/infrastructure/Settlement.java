package vc.game.world.infrastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vc.game.Infoable;

public class Settlement implements Infoable, Serializable {
	private static final String FACILITIES = "Facilities: ";

	public final List<Facility> facilities = new ArrayList<>();
	private final String name;

	public Settlement(String name) {
		this.name = name;
	}

	public Facility addFacility(Facility facility) {
		facilities.add(facility);
		return facility;
	}

	public String getInfo() {
		String info = name + '\n';
		info += FACILITIES + facilities.size();
		for (Facility f : facilities) info += '\n' + f.getInfo();
		return info;
	}
}
