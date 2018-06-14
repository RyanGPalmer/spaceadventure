package game.utilities.tasks;

import vicinity.Log;

public class TaskWorker {
	private final Thread t;
	private final Task task;
	private boolean threadStarted = false;

	public TaskWorker(Task task) {
		this(task, true);
	}

	public TaskWorker(Task task, boolean startImmediately) {
		this.task = task;
		t = new Thread(task);
		t.setName(task.getClass().getSimpleName());
		if (startImmediately) start();
	}

	public void start() {
		if (!threadStarted) {
			t.start();
			threadStarted = true;
			while (!task.isStarted()) {
			}
			Log.info("Task started: " + task.getClass().getSimpleName());
		}
	}

	public boolean isDone() {
		return task.isDone();
	}

	public String getMessage() {
		return task.getMessage();
	}

	public Object getResult() {
		return task.getResult();
	}

	public String getNextMessage() {
		return task.getNextMessage();
	}
}
