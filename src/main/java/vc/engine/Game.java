package vc.engine;

import javafx.application.Application;
import javafx.stage.Stage;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Game extends Application {
	private static final String MAIN_THREAD = "System";

	private static String title = "Game";

	protected static void launchGame(String... args) {
		Thread.currentThread().setName(MAIN_THREAD);
		Log.startLogWorker();
		launch(args);
		Log.stopLogWorker();
	}

	@Override
	public final void start(Stage primaryStage) {
		public class Main {
			public Main() {
				try {
					Display.setDisplayMode(new DisplayMode(800, 600));
					Display.create();

					while(!Display.isCloseRequested()) {
						Display.update();
					}

					Display.destroy();
				} catch(LWJGLException e) {
					e.printStackTrace();
				}
			}

			public static void main(String[] args) {
				new Main();
			}
		}
		System.out.println("Hello World");
	}

	protected static void setTitle(String title) {
		Game.title = title;
	}

	@Override
	public final void stop() {
	}
}
