import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL33;

public abstract class MapItem {
  
  private Figure shape;
  private Vector3f pos = new Vector3f();
  private Matrix4f model;
  
  public MapItem(Figure shape, Transformation transform) {
    this.shape = shape;
    model = transform.get();
  }
  
  public float[] getVertices() {
    return shape.getVertices();
  }
  
  public IntBuffer getIndices() {
    return shape.getIndices();
  }
  
  public Vector3f pos() {
    return pos;
  }

  public void addPos(Vector3f pos) {
    this.pos.add(pos);
  }

  public Matrix4f model() {
    return model;
  }

}
