import org.joml.Vector3f;

public abstract class EnemyInstance extends MapItemInstance {

  protected int damage = 0;
  protected final float SPEED;

  public EnemyInstance(Vector3f worldPos, float speed) {
    super(worldPos);
    SPEED = speed;
  }

  @Override
  public boolean onCollision(MapItemInstance other) {
    if (other instanceof CameraInstance) {
      other.addHealth(-damage);
    }
    if (other instanceof EnemyInstance) {
      return false;
    }
    return super.onCollision(other);
  }

}
