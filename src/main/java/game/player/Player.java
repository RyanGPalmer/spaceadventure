package game.player;

public class Player {
	private static final int REGULATOR_SINGLE_USE = 10;
	private static final int COMPENSATOR_SINGLE_USE = 5;
	private static final int REGULATOR_MAX_FILL = 100;
	private static final int COMPENSATOR_MAX_FILL = 100;

	private int regulator;
	private int compensator;
	private int money;

	public void useRegulator() {
		if (regulator >= REGULATOR_SINGLE_USE) regulator -= REGULATOR_SINGLE_USE;
		else regulator = 0;
	}

	public void fillRegulator() {
		regulator = REGULATOR_MAX_FILL;
	}

	public float getRegulatorPercentage() {
		return (float) regulator / REGULATOR_MAX_FILL;
	}

	public void useCompensator() {
		if (compensator >= COMPENSATOR_SINGLE_USE) compensator -= COMPENSATOR_SINGLE_USE;
		else compensator = 0;
	}

	public void fillCompensator() {
		compensator = COMPENSATOR_MAX_FILL;
	}

	public float getCompensatorPercentage() {
		return (float) compensator / COMPENSATOR_MAX_FILL;
	}

	public void addMoney(int amount) {
		money += amount;
	}

	public int getBalance() {
		return money;
	}

	public boolean spendMoney(int amount) {
		if (getBalance() >= amount) {
			subtractMoney(amount);
			return true;
		} else return false;
	}

	public void subtractMoney(int amount) {
		if (amount > money) amount = money;
		money -= amount;
	}
}
