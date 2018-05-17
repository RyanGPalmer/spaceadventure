package vc.engine;

import javafx.application.Application;
import javafx.stage.Stage;

//import org.lwjgl.;
//import org.lwjgl.opengl.Display;
//import org.lwjgl.opengl.DisplayMode;

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
//		try {
//			Display.setDisplayMode(new DisplayMode(640, 480));
//			Display.setTitle("Episode 1 – Display Test");
//			Display.create();
//		} catch (LWJGLException e) {
//			System.err.println("Display wasn't initialized correctly.");
//			System.exit(1);
//		}
//
//		while (!Display.isCloseRequested()) {
//			Display.update();
//			Display.sync(60);
//		}
//
//		Display.destroy();
//		System.exit(0);
		System.out.println("Hello World");
	}

	protected static void setTitle(String title) {
		Game.title = title;
	}

	@Override
	public final void stop() {
	}
}
