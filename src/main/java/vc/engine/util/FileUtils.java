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
			contents = null;
		} catch (IOException e) {
			Log.error("Couldn't read file: " + path, e);
			contents = null;
		} finally {
			return contents;
		}
	}

	public static String buildFilePath(String filename, String extension, String... path) {
		return buildFolderPath(path) + filename + '.' + extension;
	}

	public static String buildFolderPath(String... path) {
		StringBuilder sb = new StringBuilder();
		for (String p : path) sb.append(p).append('/');
		return sb.toString();
	}
}
