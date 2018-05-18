package vc.engine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;

public final class GameSettingsLoader extends JFrame {
	private static final String DATA_FILE_NAME = "settings.dat";

	private GameSettingsLoader() {
	}

	public static void save(GameSettings settings) {
		try {
			FileOutputStream file = new FileOutputStream(DATA_FILE_NAME);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(settings);
			out.close();
			file.close();
		} catch (Exception e) {
			Log.error("Failed to save settings.");
			Log.exception(e);
		}
	}

	public static GameSettings load() {
		GameSettings gs;
		FileInputStream file;
		ObjectInputStream in;

		try {
			file = new FileInputStream(DATA_FILE_NAME);
			in = new ObjectInputStream(file);
			gs = (GameSettings) in.readObject();
			if (in != null) in.close();
			if (file != null) file.close();
		} catch (Exception e) {
			Log.warn("Something went wrong while loading settings. Nothing was loaded.");
			return null;
		}

		return gs;
	}
}
