package vc.game.utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.*;

import vc.engine.Log;
import vc.game.world.Coordinates;
import vc.game.world.Galaxy;
import vc.game.world.StarSystem;

public class Util {
	private static final char CHAR_MIN = 'a';
	private static final char CHAR_MAX = 'z';
	private static final String HIGH = "High";
	private static final String LOW = "Low";

	private static final String[] INTENSITY_ADJECTIVES = {
			"Normal",
			"Somewhat",
			"Very",
			"Extremely"
	};

	public static final String[] CALLSIGNS = {
			"Alpha",
			"Beta",
			"Theta",
			"Omega",
			"Delta",
			"Epsilon"
	};

	public static int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public static float randomFloat(FloatRange range) {
		return randomFloat(range.min, range.max);
	}

	public static float randomFloat(float min, float max) {
		return randomInt((int) (min * 1000000), (int) (max * 1000000)) / 1000000f;
	}

	public static String randomLetter() {
		return randomLetter(CHAR_MIN, CHAR_MAX);
	}

	public static String randomLetter(char min, char max) {
		min = ("" + min).toLowerCase().charAt(0);
		max = ("" + max).toLowerCase().charAt(0);
		return "" + (char) randomInt(min, max);
	}

	public static boolean randomBool() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	public static String asPercent(float value) {
		return ((int) (value * 100)) + "%";
	}

	public static float getValueFromIntensity(float intensity, FloatRange range) {
		if (intensity >= 1) return range.max;
		if (intensity <= 0) return range.min;
		return range.min + (intensity * (range.max - range.min));
	}

	public static float getIntensity(float value, FloatRange range) {
		if (value >= range.max) return 1;
		if (value <= range.min) return 0;
		return (value - range.min) / (range.max - range.min);
	}

	public static float getNormalizedIntensity(float value, float min, float max, float mean) {
		boolean above = value > mean;
		float deviation = Math.abs(value - mean);
		return above ? deviation / (max - mean) : deviation / (mean - min);
	}

	// Expects a value between 0 and 1
	public static String getIntensityString(float value, boolean high) {
		String intensity = "";
		String highLow = high ? HIGH : LOW;
		if (value > 0.75f) intensity += INTENSITY_ADJECTIVES[3];
		else if (value > 0.50f) intensity += INTENSITY_ADJECTIVES[2];
		else if (value > 0.25f) intensity += INTENSITY_ADJECTIVES[1];
		else intensity += INTENSITY_ADJECTIVES[0];
		return intensity.equals(INTENSITY_ADJECTIVES[0]) ? intensity : intensity + " " + highLow;
	}

	public static String getIntensityString(float value) {
		return getIntensityString(value, true);
	}

	public static void message(String title, String text, boolean good) {
		JOptionPane.showMessageDialog(null, text, title, good ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
	}

	public static boolean confirm(String title, String text) {
		return JOptionPane.showConfirmDialog(null, text, title, JOptionPane.YES_NO_OPTION) == 0;
	}

	public static String capitalize(String text) {
		if (text == null || text.length() < 1) return text;
		String result = ("" + text.charAt(0)).toUpperCase();
		if (text.length() > 1) result += text.substring(1, text.length()).toLowerCase();
		return result;
	}

	// Like capitalize() except will capitalize each word separated by a single space
	public static String capitalizeAll(String text) {
		String[] splitText = text.split(" ");
		String result = "";
		for (String s : splitText) result += capitalize(s) + ' ';
		return result.substring(0, result.length() - 1);
	}

	// Rolls for true/false value based on probability. 1 is 100% probability of true.
	public static boolean roll(float probability) {
		if (probability >= 1) return true;
		if (probability <= 0) return false;
		float roll = randomFloat(0, 1);
		return roll <= probability;
	}

	public static String getCurrentSecondMillis() {
		String currentTimeMillis = "" + System.currentTimeMillis();
		if (currentTimeMillis.length() <= 3) return currentTimeMillis;
		return currentTimeMillis.substring(currentTimeMillis.length() - 3, currentTimeMillis.length());
	}

	public static void mapGalaxy(Galaxy g) {
		int minX = g.starSystems[0].getCoords().x;
		int minY = g.starSystems[0].getCoords().y;
		int maxX = g.starSystems[0].getCoords().x;
		int maxY = g.starSystems[0].getCoords().y;

		for (StarSystem s : g.starSystems) {
			Coordinates c = s.getCoords();
			if (c.x > maxX) maxX = c.x;
			else if (c.x < minX) minX = c.x;
			if (c.y > maxY) maxY = c.y;
			else if (c.y < minY) minY = c.y;
		}

		int width = maxX - minX + 1;
		int height = maxY - minY + 1;

		char[][] map = new char[width][height];
		for (char[] cx : map) for (int i = 0; i < height; i++) cx[i] = ' ';
		for (StarSystem s : g.starSystems) {
			Coordinates c = s.getCoords();
			char letter = s.getName().substring(0, 1).toCharArray()[0];
			map[c.x - minX][c.y - minY] = letter;
		}

		StringBuilder sb = new StringBuilder();
		for (char[] cx : map) {
			for (char cy : cx) sb.append(cy);
			sb.append('\n');
		}

		try {
			FileWriter fw = new FileWriter("map.txt");
			fw.write(sb.toString());
			fw.close();
		} catch (IOException e) {
			Log.error("Failed to generate galaxy map.");
		}
	}
}

