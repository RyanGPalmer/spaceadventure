package game.utilities.tasks;

import vicinity.util.DataSerializer;

public class GameDataSaverTask extends Task<Boolean> {
	private static final String DATA_FILE_NAME = "game.dat";

	private final long seed;

	public GameDataSaverTask(long seed) {
		this.seed = seed;
	}

	@Override
	protected Boolean runTask() {
		setMessage("Saving game data");
		return new DataSerializer<>(DATA_FILE_NAME).save(seed);
	}
}