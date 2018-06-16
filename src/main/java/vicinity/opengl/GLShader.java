package vicinity.opengl;

import vicinity.util.FileUtils;
import vicinity.util.TextUtils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public final class GLShader {
	private static final String SHADER_FILE_EXTENSION = "glsl";
	private static final String SHADER_FOLDER_NAME = "shaders";
	private static final int VERTEX_SHADER_CODE = 0;
	private static final int GEOMETRY_SHADER_CODE = 1;
	private static final int FRAGMENT_SHADER_CODE = 2;
	private static final String VERTEX_SHADER_NAME = "vertex";
	private static final String GEOMETRY_SHADER_NAME = "geometry";
	private static final String FRAGMENT_SHADER_NAME = "fragment";

	public static final String DEFAULT_VERTEX_SHADER_PATH = FileUtils.buildFilePath(VERTEX_SHADER_NAME, SHADER_FILE_EXTENSION, "res", SHADER_FOLDER_NAME);
	public static final String DEFAULT_GEOMETRY_SHADER_PATH = FileUtils.buildFilePath(GEOMETRY_SHADER_NAME, SHADER_FILE_EXTENSION, "res", SHADER_FOLDER_NAME);
	public static final String DEFAULT_FRAGMENT_SHADER_PATH = FileUtils.buildFilePath(FRAGMENT_SHADER_NAME, SHADER_FILE_EXTENSION, "res", SHADER_FOLDER_NAME);

	private final int id;
	private final int type;
	private final String source;

	public GLShader(int type, String source) throws GLShaderException {
		id = glCreateShader(type);
		this.type = simplifyTypeCode(type);
		this.source = source;
		glShaderSource(id, source);
		glCompileShader(id);
		checkCompilation();
	}

	public int getID() {
		return id;
	}

	public int getType() {
		return type;
	}

	private void checkCompilation() throws GLShaderException {
		int status = glGetShaderi(id, GL_COMPILE_STATUS);
		if (status == GL_FALSE) {
			String info = glGetShaderInfoLog(id);
			throw new GLShaderException("Failed to compile " + getReadableTypeName(type) + " shader.\n" + info + TextUtils.formatSourceCodeSnippet(source));
		}
	}

	private static String getReadableTypeName(int simplifiedTypeCode) {
		switch (simplifiedTypeCode) {
			case VERTEX_SHADER_CODE:
				return VERTEX_SHADER_NAME;
			case GEOMETRY_SHADER_CODE:
				return GEOMETRY_SHADER_NAME;
			case FRAGMENT_SHADER_CODE:
				return FRAGMENT_SHADER_NAME;
			default:
				return "unknown (" + simplifiedTypeCode + ")";
		}
	}

	private static int simplifyTypeCode(int type) throws GLShaderException {
		switch (type) {
			case GL_VERTEX_SHADER:
				return VERTEX_SHADER_CODE;
			case GL_GEOMETRY_SHADER:
				return GEOMETRY_SHADER_CODE;
			case GL_FRAGMENT_SHADER:
				return FRAGMENT_SHADER_CODE;
			default:
				throw new GLShaderException("Unable to interpret shader type code: " + type);
		}
	}
}
