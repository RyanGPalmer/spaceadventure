#version 150 core

in vec3 position;
in vec4 offset;
in vec4 color;

out vec4 vs_color;

void main() {
	vec4 pos = vec4(position.x, position.y, position.z, 1.0);
    gl_Position = pos + offset;
    vs_color = color;
}