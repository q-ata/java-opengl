import org.joml.Vector3f;

public class GrapeInstance extends MapItemInstance {

  public GrapeInstance(Vector3f worldPos) {
    super(worldPos);
  }

  @Override
  public boolean onCollision(MapItemInstance other) {
    if (!(other instanceof CameraInstance || other instanceof GrapeInstance)) {
      mark();
      if (other.addHealth(-25)) {
        other.mark();
      }
    }
    return false;
  }

  @Override
  public void onGravity() {

  }

}
