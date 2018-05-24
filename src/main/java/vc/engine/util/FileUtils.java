package vc.engine.util;

import vc.engine.Log;

import java.io.*;

public class FileUtils {
	public static String read(String path) {
		String contents = "";
		try (FileReader fr = new FileReader(new File(path)); BufferedReader br = new BufferedReader(fr)) {
			String line;
			while ((line = br.readLine()) != null) contents += line + '\n';
		} catch (FileNotFoundException e) {
			Log.error("File not found: " + path);
		} catch (IOException e) {
			Log.error("Couldn't read file: " + path, e);
		} finally {
			return contents;
		}
	}
}
