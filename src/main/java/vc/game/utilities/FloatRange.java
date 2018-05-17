package vc.game.utilities;

public class FloatRange {
	public final float min;
	public final float max;

	public FloatRange(float min, float max) {
		this.min = min;
		this.max = max;
	}

	public boolean inRange(float value) {
		return value < max && value >= min;
	}
}
