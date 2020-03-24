package structs;

import java.lang.reflect.InvocationTargetException;

import constants.Logger;
import game.Game;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

/**
 * Carries metadata for all associated instances.
 */
public abstract class MapItem {
  
  private Figure shape;
  private Sprite sprite;
  private Matrix4f model;
  private int vao = 0;
  private int identifier = -1;
  private Class<? extends MapItemInstance> type;

  /**
   * Create a new structs.MapItem.
   * @param shape The model.
   * @param transform The transformation on the model.
   * @param sprite The model's sprite.
   * @param type The structs.MapItemInstance this represents.
   */
  public MapItem(Figure shape, Transformation transform, Sprite sprite, Class<? extends MapItemInstance> type) {
    this.shape = shape;
    model = transform.get();
    this.sprite = sprite;
    this.type = type;
    setVao(Game.game().genBinding(this));
  }

  /**
   * Create a new structs.MapItemInstance of this type.
   * @param pos The 3D world space coordinates of where this instance is located.
   * @return The instance.
   */
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

  /**
   * Get the 3D local space coordinates of this structs.MapItem's hitbox vertices.
   * @return The vertices in [x1, y1, z1, x2, y2, z2...] format.
   */
  public float[] getVertices() {
    return shape.getVertices();
  }

  /**
   * The indices provided to OpenGL.
   * @return The indices.
   */
  public int[] getIndices() {
    return shape.getIndices();
  }

  /**
   * Bind this structs.MapItem's texture as the current active texture.
   */
  public void bind() {
    GL33.glActiveTexture(GL33.GL_TEXTURE0);
    GL33.glBindTexture(GL33.GL_TEXTURE_2D, sprite.get());
  }

  /**
   * structs.Transformation to apply to model.
   * @return The transform.
   */
  public Matrix4f model() {
    return new Matrix4f(model);
  }

  /**
   * The unique identifier for this item's VAO.
   * @return The vao ID.
   */
  public int vao() {
    return vao;
  }

  /**
   * Set this item's VAO. Can only be done once.
   * @param vao The vao to set to.
   */
  protected void setVao(int vao) {
    if (this.vao != 0) {
      Logger.error(getClass(), "VAO already bound with ID: " + vao);
      return;
    }
    this.vao = vao;
  }

  /**
   * Set the unique identifier for this structs.MapItem. This can only be done once per object.
   * @param id The ID to set.
   */
  public void setIdentifier(int id) {
    if (identifier != -1) {
      Logger.error(getClass(), "Unique identifer already bound with ID: " + identifier);
      return;
    }
    identifier = id;
  }

  /**
   * Get the unique identifier for this structs.MapItem.
   * @return The ID.
   */
  public int id() {
    return identifier;
  }

  /**
   * Get the structs.MapItemInstance for this structs.MapItem.
   * @return The instance class.
   */
  public Class<? extends MapItemInstance> getType() {
    return type;
  }

}
