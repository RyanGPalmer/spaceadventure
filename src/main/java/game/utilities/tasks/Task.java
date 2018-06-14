package game.utilities.tasks;

import vicinity.Log;

public abstract class Task<T> implements Runnable {
	private final Object messageMutex = new Object();
	private volatile String message;
	private final Object resultMutex = new Object();
	private volatile T result;
	private volatile boolean done = false;
	private volatile boolean started = false;

	@Override
	public final void run() {
		synchronized (resultMutex) {
			started = true;
			result = runTask();
			done();
		}
	}

	protected abstract T runTask();

	protected void setMessage(String message) {
		synchronized (messageMutex) {
			this.message = message;
			messageMutex.notifyAll();
		}
	}

	private void done() {
		synchronized (messageMutex) {
			done = true;
			messageMutex.notifyAll();
		}
	}

	public String getNextMessage() {
		synchronized (messageMutex) {
			try {
				messageMutex.wait();
			} catch (InterruptedException e) {
				Log.warn("Interrupted while waiting for message from task: " + getClass().getName());
			}
			return message;
		}
	}

	public String getMessage() {
		return message;
	}

	public boolean isDone() {
		return done;
	}

	public boolean isStarted() {
		return started;
	}

	public T getResult() {
		synchronized (resultMutex) {
			return result;
		}
	}
}
