import org.joml.Vector3f;

public abstract class MapItemInstance {

  private Vector3f worldPos;
  private Vector3f vel = new Vector3f();
  private boolean[] collisionComponents = new boolean[3];
  private int health = 100;
  private boolean garbage = false;

  private static final Vector3f GRAVITY = new Vector3f(0.0f, -0.008f, 0.0f);

  public MapItemInstance(Vector3f worldPos) {
    this.worldPos = worldPos;
  }

  /**
   * How the instance should behave every frame.
   */
  public void behaviour() {

  };

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
   * Adjust the location of this instance in world space.
   * @param amt The amount to move by.
   */
  public void move(Vector3f amt) {
    worldPos.add(amt);
  }

  /**
   * Callback for when the game ticks forward.
   */
  public void onTick() {
    move(vel());
    collisionComponents[0] = collisionComponents[1] = collisionComponents[2] = false;
  }

  /**
   * Callback for when this instance collides with another.
   * @param other The other instance.
   */
  public boolean onCollision(MapItemInstance other) {
    move(vel().negate());
    return true;
  }

  /**
   * Callback for after this instance has collided with another. By default used to determine if any components can still be applied.
   * @param other The other instance.
   */
  public void onCollisionResolution(MapItemInstance other) {
    Vector3f vel = vel();
    Vector3f v = new Vector3f(vel.x, 0f, 0f);
    if (Collision.collision(world().add(v), Game.INSTANCE.retrieve(this), other.world(), Game.INSTANCE.retrieve(other))) {
      vel.x = 0;
      setVel(vel);
      collisionComponents[0] = true;
    }
    else {
      move(v);
      collisionComponents[0] = false;
    }
    v.x = 0;
    v.z = vel.z;
    if (Collision.collision(world().add(v), Game.INSTANCE.retrieve(this), other.world(), Game.INSTANCE.retrieve(other))) {
      vel.z = 0;
      setVel(vel);
      collisionComponents[2] = true;
    }
    else {
      move(v);
      collisionComponents[2] = false;
    }
    vel.z = 0;
    v.y = vel.y;
    if (Collision.collision(world().add(v), Game.INSTANCE.retrieve(this), other.world(), Game.INSTANCE.retrieve(other))) {
      vel.y = 0;
      setVel(vel);
      collisionComponents[1] = true;
    }
    else {
      move(v);
      collisionComponents[1] = false;
    }

  }

  /**
   * Callback for when this instance should be affected by gravity.
   */
  public void onGravity() {
    setVel(vel().add(GRAVITY));
  }

  protected boolean getComponent(int c) {
    return collisionComponents[c];
  }

  public int getHealth() {
    return health;
  }

  public boolean addHealth(int amt) {
    health += amt;
    if (health < 0) {
      health = 0;
    }
    return health == 0;
  }

  public void mark() {
    garbage = true;
  }

  public boolean garbage() {
    return garbage;
  }

}
