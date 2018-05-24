package vc.engine.util;

import vc.engine.Log;

public class TickTimer {
	private static final double NANOSECONDS = 1000000000.0;
	private static final int TPS_HISTORY_LENGTH = 30;
	private static final double LOW_TICK_RATE_FACTOR = 0.5;

	private final double desiredTickRate;
	private int tps = 0, fps = 0, ticks = 0, frames = 0;
	private double delta = 0, last = 0, seconds = 0;
	private int[] tpsHistory = new int[TPS_HISTORY_LENGTH];
	private int currentHistoryIndex = 0;

	public TickTimer(final double desiredTickRate) {
		this.desiredTickRate = desiredTickRate;
	}

	public void init() {
		last = getTimeSeconds();
		for (int i = 0; i < tpsHistory.length; i++) tpsHistory[i] = (int) desiredTickRate;
	}

	public void update() {
		double now = getTimeSeconds();
		delta += (now - last) * desiredTickRate;
		seconds += now - last;
		last = now;
		frames++;

		if (seconds >= 1.0) {
			tps = ticks;
			fps = frames;
			seconds--;
			ticks = 0;
			frames = 0;
			addTpsHistory(tps);
			System.out.println("TPS: " + tps + " FPS: " + fps + " AVG: " + getAverageTPS());
			if (tps <= desiredTickRate * LOW_TICK_RATE_FACTOR) Log.warn("Low tick rate detected: " + tps);
		}
	}

	public double onTick() {
		double prevDelta = delta;
		ticks++;
		delta--;
		return prevDelta;
	}

	public boolean shouldTick() {
		return delta >= 1.0;
	}

	private double getTimeSeconds() {
		return System.nanoTime() / NANOSECONDS;
	}

	private void addTpsHistory(int tps) {
		if (currentHistoryIndex >= tpsHistory.length) currentHistoryIndex = 0;
		tpsHistory[currentHistoryIndex++] = tps;
	}

	public int getAverageTPS() {
		int total = 0;
		for (int tps : tpsHistory) total += tps;
		return Math.round((float) total / (float) tpsHistory.length);
	}

	public int getTPS() {
		return tps != 0 ? tps : ticks;
	}

	public int getFPS() {
		return fps != 0 ? fps : frames;
	}

	public double getDesiredTickRate() {
		return desiredTickRate;
	}
}
