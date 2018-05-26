package vc.engine;

import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class ShaderProgram {
	private static final int FLOAT_SIZE = 4; // There are 4 bytes in a floating point variable

	private final int id;
	private final List<Shader> shaders;

	private ShaderProgram(boolean legacy, Shader... shaders) {
		id = glCreateProgram();
		for (Shader s : shaders) attach(s);
		this.shaders = Arrays.asList(shaders);
	}

	public static ShaderProgram create(boolean legacy, Shader... shaders) {
		return new ShaderProgram(legacy, shaders);
	}

	public void attach(Shader shader) {
		glAttachShader(id, shader.getID());
	}

	public void bindFragDataLocation(int number, String name) {
		glBindFragDataLocation(id, number, name);
	}

	public void addAttribute(String name) {
		addAttribute(name, 0);
	}

	public void addAttribute(String name, int offset) {
		int location = getAttributeLocation(name);
		enableVertexAttribute(location);
		pointVertexAttribute(location, 3, 6 * FLOAT_SIZE, offset * FLOAT_SIZE);
	}

	private int getAttributeLocation(String name) {
		return glGetAttribLocation(id, name);
	}

	private void enableVertexAttribute(int location) {
		glEnableVertexAttribArray(location);
	}

	private void disableVertexAttribute(int location) {
		glDisableVertexAttribArray(location);
	}

	private void pointVertexAttribute(int location, int size, int stride, int offset) {
		glVertexAttribPointer(location, size, GL_FLOAT, false, stride, offset);
	}

	public void setUniform(String name, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(4 * 4);
			value.toBuffer(buffer);
			glUniformMatrix4fv(getUniformLocation(name), false, buffer);
		}
	}

	private int getUniformLocation(String name) {
		return glGetUniformLocation(id, name);
	}

	public void link() {
		glLinkProgram(id);
	}

	private boolean check() {
		return glGetProgrami(id, GL_LINK_STATUS) == GL_TRUE;
	}

	public void use() {
		if (check()) glUseProgram(id);
		else Log.error("Unlinked shader program. Info: " + glGetProgramInfoLog(id), new RuntimeException());
	}

	public int getID() {
		return id;
	}

	public void delete() {
		for (Shader s : shaders) s.delete();
		glDeleteProgram(id);
	}
}
