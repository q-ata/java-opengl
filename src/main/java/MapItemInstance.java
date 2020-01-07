import org.joml.Vector3f;

public abstract class MapItemInstance {

  private Vector3f worldPos;

  private Vector3f vel = new Vector3f();
  private static final Vector3f GRAVITY = new Vector3f(0.0f, -0.0000002f, 0.0f);

  public MapItemInstance(Vector3f worldPos) {
    this.worldPos = worldPos;
  }

  /**
   * How the instance should behave every frame.
   */
  public abstract void behaviour();

  /**
   * Sets this instance's velocity.
   * @param vel The new velocity.
   */
  public void setVel(Vector3f vel) {
      this.vel = vel;
  }

  /**
   * Return a copy of this instance's velocity.
   * @return The velocity.
   */
  public Vector3f vel() {
    return new Vector3f(vel);
  }

  /**
   * Return a copy of this instance's world space coordinates.
   * @return The coordinates.
   */
  public Vector3f world() {
    return new Vector3f(worldPos);
  }

  /**
   * Callback for when the game ticks forward.
   */
  public void onTick() {
    worldPos.add(vel());
  }

  /**
   * Callback for when this instance collides with another.
   * @param other The other instance.
   */
  public void onCollision(MapItemInstance other) {
    worldPos.add(vel().negate());
  }

  /**
   * Callback for after this instance has collided with another. By default used to determine if any components can still be applied.
   * @param other The other instance.
   */
  public void onCollisionResolution(MapItemInstance other) {
    Vector3f v = new Vector3f(vel().x, 0f, 0f);
    worldPos.add(v);
    if (Collision.collision(world(), Game.INSTANCE.retrieve(this), other.world(), Game.INSTANCE.retrieve(other))) {
      worldPos.add(v.negate());
    }
    v = new Vector3f(0f, vel().y, 0f);
    worldPos.add(v);
    if (Collision.collision(world(), Game.INSTANCE.retrieve(this), other.world(), Game.INSTANCE.retrieve(other))) {
      worldPos.add(v.negate());
    }
    v = new Vector3f(0f, 0f, vel().z);
    worldPos.add(v);
    if (Collision.collision(world(), Game.INSTANCE.retrieve(this), other.world(), Game.INSTANCE.retrieve(other))) {
      worldPos.add(v.negate());
    }
  }

  /**
   * Callback for when this instance should be affected by gravity.
   */
  public void onGravity() {
    setVel(vel().add(GRAVITY));
  }

}
