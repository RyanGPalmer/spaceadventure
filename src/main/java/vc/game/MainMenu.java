package vc.game;

import vc.engine.Log;
import vc.game.utilities.Util;
import vc.game.utilities.tasks.GameDataLoader;
import vc.game.utilities.tasks.GameDataSaver;
import vc.game.utilities.tasks.TaskWorkerExecutionFrame;
import vc.game.ux.GamePanel;
import vc.game.world.Galaxy;
import vc.game.world.GalaxyGenerator;
import vc.game.world.Seed;

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
		new TaskWorkerExecutionFrame(this, new GameDataSaver(seed.seed));
	}

	private Seed loadGameData() {
		Long result = (Long) new TaskWorkerExecutionFrame(this, new GameDataLoader()).getResult();
		return result != null ? new Seed(result) : null;
	}
}
