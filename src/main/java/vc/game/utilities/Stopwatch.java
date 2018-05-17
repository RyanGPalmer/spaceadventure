package vc.game.utilities;

import java.text.DecimalFormat;

public class Stopwatch {
	private final long startTime;

	public Stopwatch() {
		startTime = System.currentTimeMillis();
	}

	public String getTime() {
		float time = getTimeFloat();
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		return df.format(time);
	}

	public float getTimeFloat() {
		return ((float) (System.currentTimeMillis() - startTime)) / 1000f;
	}
}
