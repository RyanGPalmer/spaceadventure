package vicinity.util;

import java.util.Scanner;

public final class TextUtils {
	private static final int SOURCE_SNIPPET_PADDING = 5;

	private TextUtils() {
	}

	public static String padTextLeft(String text, int width, char padChar) {
		int amount = width - text.length();
		if (amount <= 0) return text;
		String pad = repeatChar(padChar, amount);
		return pad + text;
	}

	public static String formatSourceCodeSnippet(String source) {
		source = source.replace("\t", "    ");
		String result = "";
		Scanner sc = new Scanner(source);
		int width = 0;
		int count = 1;
		while (sc.hasNextLine()) {
			String lineNumber = "" + count++;
			String line = padTextLeft(lineNumber, SOURCE_SNIPPET_PADDING, ' ') + " | " + sc.nextLine() + '\n';
			if (line.length() > width) width = line.length();
			result += line;
		}
		String border = repeatChar('=', width);
		return border + '\n' + result + border;
	}

	private static String repeatChar(char c, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) sb.append(c);
		return sb.toString();
	}
}
