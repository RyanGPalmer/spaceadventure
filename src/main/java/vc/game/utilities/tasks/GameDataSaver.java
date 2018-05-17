package vc.game.utilities.tasks;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import vc.engine.Log;
import vc.game.utilities.Stopwatch;

public class GameDataSaver extends Task<Boolean> {
	private static final String DATA_FILE_NAME = "game.dat";

	private final long seed;

	public GameDataSaver(long seed) {
		this.seed = seed;
	}

	@Override
	protected Boolean runTask() {
		setMessage("Saving game data");
		try {
			Stopwatch sw = new Stopwatch();
			FileOutputStream file = new FileOutputStream(DATA_FILE_NAME);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(seed);
			out.close();
			file.close();
			Log.info("Game data saved. (" + sw.getTime() + "s)");
			return true;
		} catch (Exception e) {
			Log.error("Failed to save game data. Worker message: " + getMessage());
			Log.exception(e);
			return false;
		}
	}
}
