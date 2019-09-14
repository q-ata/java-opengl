import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

public abstract class MapItem {
  
  private Figure shape;
  private Sprite sprite;
  private Matrix4f pos = new Matrix4f();
  
  public MapItem(Figure shape, Sprite sprite) {
    this.shape = shape;
    this.sprite = sprite;
    pos.rotate((float) Math.toRadians(45), new Vector3f(1.0f, 0.0f, 0.0f));
  }
  
  public void bind() {
    GL33.glActiveTexture(GL33.GL_TEXTURE0);
    GL33.glBindTexture(GL33.GL_TEXTURE0, sprite.get());
  }
  
  public float[] getVertices() {
    return shape.getVertices();
  }
  
  public IntBuffer getIndices() {
    return shape.getIndices();
  }
  
  public Matrix4f pos() {
    return pos;
  }

}
