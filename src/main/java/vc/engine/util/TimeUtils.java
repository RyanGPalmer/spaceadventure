package vc.engine.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeUtils {
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private TimeUtils() {
	}

	public static String getTimestamp() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
		return dtf.format(LocalDateTime.now()) + '.' + getCurrentMillis();
	}

	public static String getCurrentMillis() {
		String currentTimeMillis = "" + System.currentTimeMillis();
		if (currentTimeMillis.length() <= 3) return currentTimeMillis;
		return TextUtils.padTextLeft(currentTimeMillis.substring(currentTimeMillis.length() - 3, currentTimeMillis.length()), 3, '0');
	}
}
