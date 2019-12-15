#version 330 core

layout (location = 0) in vec2 inPos;

uniform mat4 transform;

void main()
{
  gl_Position = transform * vec4(inPos, 0.0, 1.0);
}