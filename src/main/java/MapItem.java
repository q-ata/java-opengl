import java.lang.reflect.InvocationTargetException;
import java.nio.IntBuffer;
import java.util.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL33;

public abstract class MapItem {
  
  private Figure shape;
  private Sprite sprite;
  private Vector3f localPos = new Vector3f();
  private Matrix4f model;
  private int vao = 0;
  private int identifier;
  private Class<? extends MapItemInstance> type;

  public MapItem(Figure shape, Transformation transform, Sprite sprite, Class<? extends MapItemInstance> type) {
    this.shape = shape;
    model = transform.get();
    this.sprite = sprite;
    this.type = type;
  }

  public MapItemInstance create(Vector3f pos) {
    MapItemInstance item = null;
    try {
      item = type.getConstructor(Vector3f.class).newInstance(pos);
    }
    catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      Logger.error(getClass(), "Could not create map item instance.");
      e.printStackTrace();
    }
    return item;
  }
  
  public float[] getVertices() {
    return shape.getVertices();
  }
  
  public int[] getIndices() {
    return shape.getIndices();
  }

  public void bind() {
    GL33.glActiveTexture(GL33.GL_TEXTURE0);
    GL33.glBindTexture(GL33.GL_TEXTURE_2D, sprite.get());
  }
  
  public Vector3f local() {
    return localPos;
  }

  public Matrix4f model() {
    return model;
  }

  public int vao() {
    return vao;
  }

  public void setVao(int vao) {
    if (this.vao != 0) {
      Logger.error(getClass(), "VAO already bound with ID: " + vao);
      return;
    }
    this.vao = vao;
  }

  public void setIdentifier(int id) {
    identifier = id;
  }

  public int id() {
    return identifier;
  }

  public Class<? extends MapItemInstance> getType() {
    return type;
  }

}
