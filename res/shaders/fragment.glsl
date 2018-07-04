#version 330 core

out vec4 color;

in VS_OUT
{
	vec4 color;
} fs_in;

void main() {
	color = vec4(fs_in.color.x, fs_in.color.y, fs_in.color.z, fs_in.color.w);
}