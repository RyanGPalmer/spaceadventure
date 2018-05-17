package vc.game.ux;

import vc.engine.Log;

public abstract class GamePanel extends Frame {
	protected static final int DEFAULT_TICK_RATE = 1000 / 30;
	protected static final int FRAME_WIDTH = 640;
	private static final int FRAME_HEIGHT = 480;
	protected static final int PADDING = 25;
	protected static final int BUTTON_WIDTH = 120;
	protected static final int BUTTON_HEIGHT = 40;
	private static final int EXIT_BUTTON_WIDTH = 20;
	protected static final int EXIT_BUTTON_HEIGHT = 20;
	private static final String EXIT = "X";
	private static final String BACK = "<";

//	private final Worker updateWorker = new Worker(() -> update0());

//	private JButton exitButton;

	protected GamePanel(Frame parent) {
		this(parent, FRAME_WIDTH, FRAME_HEIGHT);
	}

	protected GamePanel(Frame parent, int width, int height) {
		super(parent);
//		Thread.currentThread().setUncaughtExceptionHandler(Game.UNCAUGHT_EXCEPTION_HANDLER);
		Thread.currentThread().setName(getClass().getSimpleName());
		if (exit) return;
		draw0(width, height);
//		updateWorker.setName(getClass().getName() + "Update");
//		updateWorker.start();
	}

	private final void draw0(int width, int height) {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setLayout(null);
//		setSize(width, height);
//		setLocationRelativeTo(null);
//		setResizable(false);
//		setUndecorated(true);
//
//		String text = parent == null ? EXIT : BACK;
//		exitButton = new JButton(text);
//		exitButton.setBounds(PADDING, PADDING, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
//		exitButton.addActionListener(a -> exit());
//		add(exitButton);
//
//		draw();
//		setVisible(true);
	}

	protected abstract void draw();

	public final void update0() {
		while (!exit) {
			update();
			try {
				Thread.sleep(DEFAULT_TICK_RATE);
			} catch (InterruptedException e) {
				Log.warn(getClass().getName() + " interrupted during update.");
			}
		}
	}

	protected abstract void update();
}
