package constants;

import game.Game;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL33;
import structs.Figure;

/**
 * Methods to draw 2D squads onto the HUD.
 */
public class StaticDraw {

  private static int VAO = GL33.glGenVertexArrays();
  private static int VBO = GL33.glGenBuffers();
  private static int VBOI = GL33.glGenBuffers();

  /**
   * Generate new OpenGL identifiers.
   */
  public static void reset() {
    VAO = GL33.glGenVertexArrays();
    VBO = GL33.glGenBuffers();
    VBOI = GL33.glGenBuffers();
  }

  public static void drawRect(float locX, float locY, float scaleX, float scaleY, Vector4f color, boolean... cartesian) {
    drawRect(new Vector2f(locX, locY), new Vector2f(scaleX, scaleY), color, cartesian);
  }

  public static void drawRect(float locX, float locY, float scaleX, float scaleY, float r, float g, float b, float a, boolean... cartesian) {
    drawRect(new Vector2f(locX, locY), new Vector2f(scaleX, scaleY), new Vector4f(r, g, b, a), cartesian);
  }

  /**
   * Draw a 2D rectangle onto the screen.
   * @param loc The location.
   * @param scale Scale multiplier of the rectangle.
   * @param color RGBA of rectangle.
   * @param cartesian Specify a cartesian boolean to use cartesian coordinates.
   */
  public static void drawRect(Vector2f loc, Vector2f scale, Vector4f color, boolean... cartesian) {

    // Apply quads VAO.
    GL33.glBindVertexArray(VAO);
    // Give vertices for a quad.
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, VBO);
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, Figure.QUAD.getVertices(), GL33.GL_STATIC_DRAW);
    // Apply vertex indices for a quad.
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, VBOI);
    GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, Figure.QUAD.getIndices(), GL33.GL_STATIC_DRAW);
    // Apply shader attributes.
    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 20, 0);
    GL33.glEnableVertexAttribArray(0);
    GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 20, 12);
    GL33.glEnableVertexAttribArray(1);

    // Use the quad shader.
    Shaders.QUAD_RENDERER.use();
    Shaders.QUAD_RENDERER.setUniformVec4("outColor", color);
    float ar = Game.game().getOption("ar");
    // By default the coordinate specified is the middle of the quad. If a cartesian boolean is not provided,
    // Calculate the coordinates such that the provided vector is the top left.
    Vector3f center = cartesian.length > 0 ? new Vector3f(loc, 0) : new Vector3f(-1 + scale.x / 2 + loc.x, 1 - scale.y * ar / 2 - loc.y, 0);
    Shaders.QUAD_RENDERER.setUniformMatrix("transform", new Matrix4f().translate(center).scale(scale.x, scale.y * ar, 1f));
    GL33.glDrawElements(GL33.GL_TRIANGLES, Figure.QUAD.getIndices().length, GL33.GL_UNSIGNED_INT, 0);
    // Unbind buffers.
    GL33.glBindVertexArray(0);
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, 0);

  }

}
