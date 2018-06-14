package game;

import vicinity.Log;
import game.utilities.Util;
import game.utilities.tasks.GameDataLoaderTask;
import game.utilities.tasks.GameDataSaverTask;
import game.utilities.tasks.TaskWorkerExecutionFrame;
import game.ux.GamePanel;
import game.world.Galaxy;
import game.world.GalaxyGenerator;
import game.world.Seed;

public class MainMenu extends GamePanel {
	private static final String LOG_MESSAGE_START = "Game started.";
	private static final String LOG_MESSAGE_FINISH = "Game ended.";
	private static final String INTRO_TEXT = "Welcome to Space Adventure!\nConquer new planets, manage your settlements, and expand your empire across the galaxy!";

	public final Seed seed;
	public final Galaxy galaxy;

	public MainMenu() {
		super(null);
		Seed loadedSeed = loadGameData();
		if (loadedSeed == null) {
			Log.info("No seed was loaded. New seed generated.");
			seed = Seed.generate();
		} else seed = loadedSeed;
		galaxy = (Galaxy) new TaskWorkerExecutionFrame(this, new GalaxyGenerator(seed)).getResult();
		Log.info("Loaded galaxy with " + galaxy.getPlanetCount() + " planets.");
		Util.mapGalaxy(galaxy);
		exit();
	}

	@Override
	protected void draw() {
	}

	@Override
	protected void update() {
	}

	@Override
	protected void onExit() {
		Log.info(LOG_MESSAGE_FINISH);
		saveGameData();
	}

	private void saveGameData() {
		// TODO Why isn't this label displaying?
		new TaskWorkerExecutionFrame(this, new GameDataSaverTask(seed.seed));
	}

	private Seed loadGameData() {
		Long result = (Long) new TaskWorkerExecutionFrame(this, new GameDataLoaderTask()).getResult();
		return result != null ? new Seed(result) : null;
	}
}
