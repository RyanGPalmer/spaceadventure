package vc.game;

import vc.engine.GameObject;

import java.util.ArrayList;
import java.util.List;

public class TestObjectManager extends GameObject {
	private static List<String> messages = new ArrayList<>();
	private static TestObjectManager current = new TestObjectManager();

	@Override
	protected void init() {
	}

	@Override
	public void tick() {
	}

	@Override
	public void lateTick() {
		String result = "I have these messages: ";
		for (String m : messages) result += m + ", ";
		System.out.println(result);
		messages = new ArrayList<>();
	}

	public static void addMessage(String message) {
		messages.add(message);
	}
}
