package vc.game.utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vc.engine.Log;

public class Type implements Serializable {
	private final List<TypeOption> options;
	private TypeOption current;

	public Type(TypeOption... options) {
		this.options = new ArrayList<>();
		if (options == null) Log.error("Type object constructed with null TypeOption list.");
		else {
			for (TypeOption o : options) {
				if (!contains(o.getName()) && !contains(o.getCode())) this.options.add(o);
				else
					Log.error("Type object constructed with TypeOption list that contains duplicate codes or names. Skipping duplicate: " + o.getName() + " [" + o.getCode() + "]");
			}
		}
	}

	public boolean set(String name) {
		TypeOption option = getOptionIfExists(name);
		if (option == null) return false;
		current = option;
		return true;
	}

	public boolean set(int code) {
		TypeOption option = getOptionIfExists(code);
		if (option == null) return false;
		current = option;
		return true;
	}

	public void random() {

	}

	public String getName() {
		return current == null ? null : current.getName();
	}

	public int getCode() {
		return current == null ? null : current.getCode();
	}

	public boolean equals(String name) {
		if (current == null) return name == null;
		return current.equals(name);
	}

	public boolean equals(int code) {
		if (current == null) return false;
		return current.equals(code);
	}

	private TypeOption getOptionIfExists(String name) {
		for (TypeOption o : options) if (o.equals(name)) return o;
		return null;
	}

	private TypeOption getOptionIfExists(int code) {
		for (TypeOption o : options) if (o.equals(code)) return o;
		return null;
	}

	private boolean contains(String name) {
		for (TypeOption o : options) if (o.equals(name)) return true;
		return false;
	}

	private boolean contains(int code) {
		for (TypeOption o : options) if (o.equals(code)) return true;
		return false;
	}
}
