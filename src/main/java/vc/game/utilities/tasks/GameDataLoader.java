package vc.game.utilities.tasks;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import vc.engine.Log;
import vc.engine.util.Stopwatch;

public class GameDataLoader extends Task<Long> {
	private static final String DATA_FILE_NAME = "game.dat";

	@Override
	protected Long runTask() {
		setMessage("Loading game data");
		Stopwatch sw = new Stopwatch();
		Long seed = null;

		try {
			FileInputStream file = null;
			try {
				file = new FileInputStream(DATA_FILE_NAME);
			} catch (FileNotFoundException e) {
				Log.info("Data file not found. A new one will be created.");
				if (file != null) file.close();
				return null;
			}

			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(file);
			} catch (EOFException e) {
				Log.warn("Data file is empty. No data was loaded.");
				if (in != null) in.close();
				file.close();
				return null;
			} catch (StreamCorruptedException e) {
				Log.warn("Data file is corrupt. No data was loaded.");
				if (in != null) in.close();
				file.close();
				return null;
			}

			try {
				seed = (Long) in.readObject();
			} catch (ClassCastException e) {
				Log.warn("Data file is corrupt. No data was loaded.");
				in.close();
				file.close();
				return null;
			}

			in.close();
			file.close();
		} catch (Exception e) {
			Log.warn("Something went wrong while loading data. Nothing was loaded. Worker message: " + getMessage());
			return null;
		}

		Log.info("Successfully loaded game data. (" + sw.getTime() + "s)");
		return seed;
	}
}
