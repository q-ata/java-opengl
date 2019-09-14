#version 330 core

in vec2 texCoord;

out vec4 fragColor;

uniform sampler2D texData;

void main()
{
  fragColor = texture(texData, texCoord);
} 