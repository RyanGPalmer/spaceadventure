package vc.engine.util;

import vc.engine.Log;

import java.io.*;

public class DataSerializer<T> {
	private final String path;

	public DataSerializer(final String path) {
		this.path = path;
	}

	public boolean save(T data) {
		try {
			FileOutputStream file = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(data);
			out.close();
			file.close();
			return true;
		} catch (Exception e) {
			Log.error("Failed to save data. (" + path + ")");
			Log.exception(e);
			return false;
		}
	}

	public T load() {
		T data;
		FileInputStream file;
		ObjectInputStream in;

		try {
			file = new FileInputStream(path);
			in = new ObjectInputStream(file);
			data = (T) in.readObject();
			in.close();
			file.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (Exception e) {
			Log.warn("Something went wrong while loading data. (" + path + ")");
			Log.exception(e);
			return null;
		}

		return data;
	}
}
