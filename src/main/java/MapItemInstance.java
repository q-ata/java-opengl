import org.joml.Vector3f;

public abstract class MapItemInstance {

  private Vector3f worldPos;

  public MapItemInstance(Vector3f worldPos) {
    this.worldPos = worldPos;
  }

  /**
   * How the instance should behave every frame.
   */
  public abstract void behaviour();

  /**
   * Translate this instance in world space.
   * @param vel The amount to translate by.
   */
  public void move(Vector3f vel) {
    worldPos.add(vel);
  }

  /**
   * Return this instance's world space coordinates.
   * @return The coordinates.
   */
  public Vector3f world() {
    return worldPos;
  }

}
