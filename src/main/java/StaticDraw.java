import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL33;

public class StaticDraw {

  private static final int VAO = GL33.glGenVertexArrays();
  private static final int VBO = GL33.glGenBuffers();
  private static final int VBOI = GL33.glGenBuffers();
  private static int windowWidth;
  private static int windowHeight;
  private static float ar;

  public static void drawRect(float locX, float locY, float scaleX, float scaleY, Vector4f color, boolean... cartesian) {
    drawRect(new Vector2f(locX, locY), new Vector2f(scaleX, scaleY), color, cartesian);
  }

  public static void drawRect(float locX, float locY, float scaleX, float scaleY, float r, float g, float b, float a, boolean... cartesian) {
    drawRect(new Vector2f(locX, locY), new Vector2f(scaleX, scaleY), new Vector4f(r, g, b, a), cartesian);
  }

  public static void drawRect(Vector2f loc, Vector2f scale, Vector4f color, boolean... cartesian) {

    GL33.glBindVertexArray(VAO);
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, VBO);
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, Figure.QUAD.getVertices(), GL33.GL_STATIC_DRAW);
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, VBOI);
    GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, Figure.QUAD.getIndices(), GL33.GL_STATIC_DRAW);
    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 20, 0);
    GL33.glEnableVertexAttribArray(0);
    GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 20, 12);
    GL33.glEnableVertexAttribArray(1);

    Shaders.QUAD_RENDERER.use();
    Shaders.QUAD_RENDERER.setUniformVec4("outColor", color);
    Vector3f center = cartesian.length > 0 ? new Vector3f(loc, 0) : new Vector3f(-1 + scale.x / 2 + loc.x, 1 - scale.y * ar / 2 - loc.y, 0);
    Shaders.QUAD_RENDERER.setUniformMatrix("transform", new Matrix4f().translate(center).scale(scale.x, scale.y * ar, 1f));
    GL33.glDrawElements(GL33.GL_TRIANGLES, Figure.QUAD.getIndices().length, GL33.GL_UNSIGNED_INT, 0);
    // Unbind buffers.
    GL33.glBindVertexArray(0);
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, 0);

  }

  public static void setWidth(int w) {
    windowWidth = w;
    ar = (float) windowWidth / windowHeight;
  }

  public static void setHeight(int h) {
    windowHeight = h;
    ar = (float) windowWidth / windowHeight;
  }

}
