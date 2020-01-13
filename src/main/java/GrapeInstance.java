import org.joml.Vector3f;

public class GrapeInstance extends MapItemInstance {

  public GrapeInstance(Vector3f worldPos, int id) {
    super(worldPos, id);
  }

  @Override
  public boolean onCollision(MapItemInstance other) {
    if (!(other instanceof CameraInstance || other instanceof GrapeInstance)) {
      // TODO: Delete instance from game.
      return super.onCollision(other);
    }
    return false;
  }

  @Override
  public void onGravity() {

  }

}
