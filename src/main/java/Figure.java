import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * Contains coordinate information for every drawable figure.
 */
public enum Figure {

  TRIANGLE(new float[] {
      -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
      0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
      0.0f, 0.5f, 0.0f, 0.5f, 0.5f
  }, new int[] {
      0, 1, 2
  }),
  QUAD(new float[] {
      -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
      0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
      0.5f, 0.5f, 0.0f, 1.0f, 1.0f,
      -0.5f, 0.5f, 0.0f, 0.0f, 1.0f
  }, new int[] {
      0, 1, 2,
      0, 2, 3
  }),
  CUBE(new float[] {
      -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
      0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
      0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
      -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,

      0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
      0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
      0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
      0.5f, 0.5f, 0.5f, 0.0f, 1.0f,

      0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
      -0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
      -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
      0.5f, 0.5f, -0.5f, 0.0f, 1.0f,

      -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
      -0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
      -0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
      -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,

      -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
      0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
      0.5f, -0.5f, 0.5f, 1.0f, 1.0f,
      -0.5f, -0.5f, 0.5f, 0.0f, 1.0f,

      -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
      0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
      0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
      -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
  }, new int[] {
      0, 1, 2,
      0, 2, 3,

      4, 5, 6,
      4, 6, 7,

      8, 9, 10,
      8, 10, 11,

      12, 13, 14,
      12, 14, 15,

      16, 17, 18,
      16, 18, 19,

      20, 21, 22,
      20, 22, 23
  });
  
  private final float[] vertices;
  private final int[] indices;

  /**
   * Create new figure using vertices and indices array. This method of representing models is inspired by https://ahbejarano.gitbook.io/lwjglgamedev/chapter5
   * @param vertices Array of vertices representing the figure. Each coordinate has 5 components, x y z t_x t_y.
   *                 x y z represents the position of the point in local space. t_x and t_y represent the coordinates in the texture where this point should be.
   * @param indices Array of indices representing the order of which to draw the vertices coordinates.
   */
  private Figure(float[] vertices, int[] indices) {
    this.vertices = vertices;
    this.indices = indices;
  }
  
  public float[] getVertices() {
    return vertices;
  }
  
  public int[] getIndices() {
    return indices;
  }
  
}
