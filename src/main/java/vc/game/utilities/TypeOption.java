package vc.game.utilities;

import java.io.Serializable;

public class TypeOption implements Serializable {
	private final String name;
	private final int code;

	public TypeOption(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public boolean equals(String s) {
		return name.equals(s);
	}

	public boolean equals(int i) {
		return code == i;
	}

	public String getName() {
		return name;
	}

	public int getCode() {
		return code;
	}
}
