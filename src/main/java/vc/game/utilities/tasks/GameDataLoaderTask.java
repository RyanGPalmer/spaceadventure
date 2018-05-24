package vc.game.utilities.tasks;

import vc.engine.util.DataSerializer;

public class GameDataLoaderTask extends Task<Long> {
	private static final String DATA_FILE_NAME = "game.dat";

	@Override
	protected Long runTask() {
		setMessage("Loading game data");
		return new DataSerializer<Long>(DATA_FILE_NAME).load();
	}
}