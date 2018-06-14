package vicinity;

import static org.lwjgl.opengl.GL20.glVertexAttrib4fv;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArrayObject {
	private static final int ATTRIB_LOCATION_POSITION = 0;
	private static final int ATTRIB_LOCATION_OFFSET = 1;
	private static final int ATTRIB_LOCATION_COLOR = 2;

	private final int id;

	public VertexArrayObject() {
		id = glGenVertexArrays();
	}

	public void bind() {
		glBindVertexArray(id);
	}

	public void delete() {
		glDeleteVertexArrays(id);
	}

	public void setOffset(float... offset) {
		glVertexAttrib4fv(ATTRIB_LOCATION_OFFSET, offset);
	}

	public void setColor(float... color) {
		glVertexAttrib4fv(ATTRIB_LOCATION_COLOR, color);
	}
}
