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

	public void tick(final double delta) {
		tickEarly(delta);
		tickNormal(delta);
		tickLate(delta);
	}

	public void destroyAll() {
		Log.info("Destroyed " + objects.size() + " game objects.");
		for (GameObject go : new ArrayList<>(objects)) go.destroy();
	}

	public void tickEarly(final double delta) {
		for (GameObject go : new ArrayList<>(objects))
			PerformanceUtils.monitorDuration(() -> go.earlyTick(delta), LONG_TICK_MILLISECONDS, "Long EARLY tick duration detected from '" + go.getClass().getSimpleName() + "'");
	}

	public void tickNormal(final double delta) {
		for (GameObject go : new ArrayList<>(objects))
			PerformanceUtils.monitorDuration(() -> go.tick(delta), LONG_TICK_MILLISECONDS, "Long tick duration detected from '" + go.getClass().getSimpleName() + "'");
	}

	public void tickLate(final double delta) {
		for (GameObject go : new ArrayList<>(objects))
			PerformanceUtils.monitorDuration(() -> go.lateTick(delta), LONG_TICK_MILLISECONDS, "Long LATE tick duration detected from '" + go.getClass().getSimpleName() + "'");
	}
}