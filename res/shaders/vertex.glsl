#version 330 core

layout (location = 0) in vec3 position;

out VS_OUT
{
	vec4 color;
} vs_out;

uniform mat4 mv_matrix;
uniform mat4 pj_matrix;

void main() {
	vec4 pos4 = vec4(position.x, position.y, position.z, 1.0);
    gl_Position = pj_matrix * mv_matrix * pos4;
    vs_out.color = pos4 * 2.0 + vec4(0.5, 0.5, 0.5, 0);
}