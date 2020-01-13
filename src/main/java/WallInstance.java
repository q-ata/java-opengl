import org.joml.Vector3f;

public class WallInstance extends ImmuneInstance {

  public WallInstance(Vector3f worldPos) {
    super(worldPos);
  }

  @Override
  public void onGravity() {
  }

  @Override
  public boolean onCollision(MapItemInstance other) {
    return !(other instanceof WallInstance);
  }

}