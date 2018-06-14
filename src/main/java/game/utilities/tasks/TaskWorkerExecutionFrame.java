package game.utilities.tasks;

import vicinity.Log;
import vicinity.util.Stopwatch;
import game.ux.Frame;

public class TaskWorkerExecutionFrame extends Frame {
	private static final int WIDTH = 500;
	private static final int HEIGHT = 100;
	private static final int MIN_RUNTIME = 1;

	private final TaskWorker tw;
//	private final JLabel text;

	public TaskWorkerExecutionFrame(Frame parent, Task task) {
		super(parent);
		Stopwatch sw = new Stopwatch();
		tw = new TaskWorker(task, false);
//		text = new JLabel();
		draw();
		run();
		while (sw.getTimeFloat() < MIN_RUNTIME) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Log.interrupt();
			}
		}
		exit();
	}

	private void draw() {
//		setSize(WIDTH, HEIGHT);
//		setUndecorated(true);
//		setLocationRelativeTo(null);
//		text.setSize(WIDTH, HEIGHT);
//		text.setHorizontalAlignment(JLabel.CENTER);
//		add(text);
//		setVisible(true);
	}

	private void run() {
		tw.start();
//		while (!tw.isDone()) text.setText(tw.getNextMessage());
	}

	public Object getResult() {
		return tw.getResult();
	}
}
