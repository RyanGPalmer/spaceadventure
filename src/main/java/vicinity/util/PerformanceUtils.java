package vicinity.util;

import vicinity.Log;

public class PerformanceUtils {
	private PerformanceUtils() {
	}

	public static void monitorDuration(final Runnable r, final int durationQuota, final String message) {
		long start = System.currentTimeMillis();
		r.run();
		int delta = (int) (System.currentTimeMillis() - start);
		if (delta > durationQuota)
			Log.warn(message + " (" + delta + "ms)");
	}
}
