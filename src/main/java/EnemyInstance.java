import org.joml.Vector3f;

public abstract class EnemyInstance extends MapItemInstance {

  protected int damage = 0;

  public EnemyInstance(Vector3f worldPos) {
    super(worldPos);
  }

  @Override
  public boolean onCollision(MapItemInstance other) {
    if (other instanceof CameraInstance) {
      other.addHealth(-damage);
    }
    return super.onCollision(other);
  }
}
