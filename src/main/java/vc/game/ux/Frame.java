package vc.game.ux;

import java.util.ArrayList;
import java.util.List;

public abstract class Frame {
	protected final Frame parent;
	private final List<Frame> children = new ArrayList<>();

	protected boolean exit = false;

	public Frame(Frame parent) {
		this.parent = parent;
		if (parent != null) parent.addChild(this);
	}

	protected final void addChild(Frame child) {
		children.add(child);
	}

	protected final void removeChild(Frame child) {
		children.remove(child);
	}

	protected final void exit() {
		if (exit) return;
		onExit();
		for (Frame f : new ArrayList<>(children)) f.exit();
		exit = true;
		dispose();
		if (parent != null) parent.removeChild(this);
	}

	protected void onExit() {
	}

	private void dispose() {
	}
}
