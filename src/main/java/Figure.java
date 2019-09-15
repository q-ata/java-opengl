import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

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
  private final IntBuffer indices;
  
  private Figure(float[] vertices, int[] indices) {
    this.vertices = vertices;
    this.indices = BufferUtils.createIntBuffer(indices.length);
    this.indices.put(indices).flip();
  }
  
  public float[] getVertices() {
    return vertices;
  }
  
  public IntBuffer getIndices() {
    return indices;
  }
  
}
