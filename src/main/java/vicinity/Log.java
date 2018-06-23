package vicinity;

import vicinity.util.TimeUtils;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class Log {
	private static final String LOG_FILE_NAME = "runtime.log";
	private static final String DIV = " - ";
	private static final String INDENT = "   ";
	private static final String LOG_ERROR = "ERROR - ";
	private static final String LOG_WARN = "Warning - ";
	private static final String LOG_UNCAUGHT = "UNCAUGHT EXCEPTION - ";
	private static final String LOG_INTERRUPT_MESSAGE = "Thread interrupted while waiting.";
	private static final List<String> LOG_BUFFER = new ArrayList<>();
	private static final int LOG_WRITE_INTERVAL = 1000;
	private static final Object LOG_MUTEX = new Object();
	private static final Thread LOG_WORKER = new Thread(() -> runLogWorkerActivities());
	private static final String LOG_WORKER_NAME = "LogWorker";
	private static boolean logWorkerActive = false;

	private Log() {
	}

	public static void start() {
		logWorkerActive = true;
		LOG_WORKER.setName(LOG_WORKER_NAME);
//		LOG_WORKER.setUncaughtExceptionHandler(Game.UNCAUGHT_EXCEPTION_HANDLER);
		LOG_WORKER.start();
		while (!LOG_WORKER.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ignore) {
			}
		}
	}

	public static void stop() {
		if (!logWorkerActive) return;
		logWorkerActive = false;
		LOG_WORKER.interrupt();
		while (LOG_WORKER.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ignore) {
			}
		}
		checkLogBufferAndWriteNewEntries();
	}

	private static void runLogWorkerActivities() {
		while (logWorkerActive) {
			try {
				Thread.sleep(LOG_WRITE_INTERVAL);
			} catch (InterruptedException e) {
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
		String thread = Thread.currentThread().getName();
		addToLogBuffer(TimeUtils.getTimestamp() + DIV + thread + DIV + message);
	}

	public static void info(String message) {
		log(message);
	}

	public static void error(String message) {
		log(LOG_ERROR + message);
	}

	public static void error(String message, Throwable e) {
		error(message + '\n' + e.toString() + getStackTrace(e));
	}

	public static void warn(String message) {
		log(LOG_WARN + message);
	}

	public static void uncaughtException(Throwable e) {
		error(LOG_UNCAUGHT + e.toString() + getStackTrace(e));
	}

	public static void interrupt() {
		warn(LOG_INTERRUPT_MESSAGE);
	}

	private static void addToLogBuffer(String text) {
		text = text.replace("\n", '\n' + INDENT);
		synchronized (LOG_MUTEX) {
			LOG_BUFFER.add(text);
		}
	}

	private static void writeToLogFile(String text) {
		try {
			FileWriter fw = new FileWriter(LOG_FILE_NAME, true);
			fw.write(text);
			fw.close();
		} catch (Exception e) {
			error("Failed to write logs to file.", e);
		}
	}

	public static String getStackTrace(Throwable e) {
		String stackTrace = "";
		for (StackTraceElement st : e.getStackTrace())
			stackTrace += '\n' + st.getClassName() + '.' + st.getMethodName() + '(' + st.getFileName() + ':' + st.getLineNumber() + ')';
		return stackTrace;
	}
}
