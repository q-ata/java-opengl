import org.joml.Vector3f;

public class WallInstance extends MapItemInstance {

  public WallInstance(Vector3f worldPos, int id) {
    super(worldPos, id);
  }

  @Override
  public void onGravity() {
  }

  @Override
  public boolean onCollision(MapItemInstance other) {
    return !(other instanceof WallInstance);
  }
}