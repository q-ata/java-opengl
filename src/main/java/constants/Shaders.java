package constants;

import structs.Shader;

public class Shaders {

  // General 3D rendering shader.
  public static final Shader RENDERER = new Shader("./res/basic_shader.vs", "./res/basic_shader.fs");
  // Renders 2D quads.
  public static final Shader QUAD_RENDERER = new Shader("./res/quad_shader.vs", "./res/quad_shader.fs");

}
