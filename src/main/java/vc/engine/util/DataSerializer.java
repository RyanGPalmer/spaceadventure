package vc.engine.util;

import vc.engine.Log;

import java.io.*;

public class DataSerializer<T> {
	private final String path;

	public DataSerializer(final String path) {
		this.path = path;
	}

	public boolean save(T data) {
		try (FileOutputStream file = new FileOutputStream(path); ObjectOutputStream out = new ObjectOutputStream(file)) {
			out.writeObject(data);
			return true;
		} catch (Exception e) {
			Log.error("Failed to save data. (" + path + ")", e);
			return false;
		}
	}

	public T load() {
		T data;
		try (FileInputStream file = new FileInputStream(path); ObjectInputStream in = new ObjectInputStream(file)) {
			data = (T) in.readObject();
		} catch (FileNotFoundException e) {
			return null;
		} catch (Exception e) {
			Log.error("Something went wrong while loading data. (" + path + ")", e);
			return null;
		}

		return data;
	}
}
