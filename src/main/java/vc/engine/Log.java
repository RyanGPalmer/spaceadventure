package vc.engine;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import vc.game.utilities.Util;

public final class Log {
	private static final String LOG_FILE_NAME = "log.txt";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DIV = " - ";
	private static final String LOG_ERROR = "ERROR: ";
	private static final String LOG_WARN = "Warning: ";
	private static final List<String> LOG_BUFFER = new ArrayList<>();
	private static final int LOG_WRITE_INTERVAL = 1000;
	private static final Object LOG_MUTEX = new Object();
	private static final Thread LOG_WORKER = new Thread(() -> runLogWorkerActivities());
	private static final String LOG_WORKER_NAME = "LogWorker";
	private static boolean logWorkerActive = false;

	private Log() {
	}

	public static void startLogWorker() {
		logWorkerActive = true;
		LOG_WORKER.setName(LOG_WORKER_NAME);
//		LOG_WORKER.setUncaughtExceptionHandler(Game.UNCAUGHT_EXCEPTION_HANDLER);
		LOG_WORKER.start();
	}

	public static void stopLogWorker() {
		logWorkerActive = false;
	}

	private static void runLogWorkerActivities() {
		while (logWorkerActive) {
			try {
				Thread.sleep(LOG_WRITE_INTERVAL);
			} catch (InterruptedException e) {
				Log.interrupt();
			}
			checkLogBufferAndWriteNewEntries();
		}
	}

	private static void checkLogBufferAndWriteNewEntries() {
		synchronized (LOG_MUTEX) {
			if (LOG_BUFFER.size() > 0) {
				String textToWrite = "";
				for (String s : LOG_BUFFER) textToWrite += s + '\n';
				writeToLogFile(textToWrite);
				LOG_BUFFER.clear();
			}
		}
	}

	private static void log(String message) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
		String dateTime = dtf.format(LocalDateTime.now()) + '.' + Util.getCurrentSecondMillis();
		String thread = Thread.currentThread().getName();
		addLineToLogBuffer(dateTime + DIV + thread + DIV + message);
	}

	public static void info(String message) {
		log(message);
	}

	public static void error(String message) {
		log(LOG_ERROR + message);
	}

	public static void warn(String message) {
		log(LOG_WARN + message);
	}

	public static void uncaughtException(Throwable e) {
		error("UNHANDLED: " + e.toString() + Util.getStackTrace(e));
	}

	public static void exception(Throwable e) {
		error(e.toString() + Util.getStackTrace(e));
	}

	public static void interrupt() {
		warn("Thread interrupted while sleeping.");
	}

	private static void addLineToLogBuffer(String line) {
		synchronized (LOG_MUTEX) {
			LOG_BUFFER.add(line);
		}
	}

	private static void writeToLogFile(String text) {
		try {
			FileWriter fw = new FileWriter(LOG_FILE_NAME, true);
			fw.write(text);
			fw.close();
		} catch (Exception e) {
			Util.message("ERROR", "Encountered " + e.getClass().getName() + " while writing to log file.", false);
		}
	}
}
