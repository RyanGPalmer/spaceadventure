package vc.engine;

import vc.engine.util.PerformanceUtils;

import java.util.ArrayList;
import java.util.Collection;

public class GameObjectManager {
	private static final int LONG_TICK_MILLISECONDS = 20;
	private static final int LONG_OVERALL_TICK_MILLISECONDS = 100;

	private final Collection<GameObject> objects = new ArrayList<>();

	public boolean register(GameObject gameObject) {
		return objects.add(gameObject);
	}

	public boolean deregister(GameObject gameObject) {
		return objects.remove(gameObject);
	}

	public void tickAll() {
		PerformanceUtils.monitorDuration(() -> {
			tickEarly();
			tickNormal();
			tickLate();
		}, LONG_OVERALL_TICK_MILLISECONDS, "Long overall tick duration detected.");
	}

	public void destroyAll() {
		Log.info("Destroyed " + objects.size() + " game objects.");
		for (GameObject go : new ArrayList<>(objects)) go.destroy();
	}

	public void tickEarly() {
		for (GameObject go : new ArrayList<>(objects))
			PerformanceUtils.monitorDuration(go::earlyTick, LONG_TICK_MILLISECONDS, "Long early tick detected from '" + go.getClass().getSimpleName() + "'");
	}

	public void tickNormal() {
		for (GameObject go : new ArrayList<>(objects))
			PerformanceUtils.monitorDuration(go::tick, LONG_TICK_MILLISECONDS, "Long tick detected from '" + go.getClass().getSimpleName() + "'");
	}

	public void tickLate() {
		for (GameObject go : new ArrayList<>(objects))
			PerformanceUtils.monitorDuration(go::lateTick, LONG_TICK_MILLISECONDS, "Long late tick detected from '" + go.getClass().getSimpleName() + "'");
	}
}