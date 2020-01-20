import org.joml.Vector3f;

public class GrapeInstance extends MapItemInstance {

  public GrapeInstance(Vector3f worldPos) {
    super(worldPos);
  }

  @Override
  public boolean onCollision(MapItemInstance other) {
    // Deal damage on collision to any enemy.
    if (!(other instanceof CameraInstance || other instanceof GrapeInstance)) {
      // Delete this projectile.
      mark();
      // Delete enemy object in health is 0.
      if (other.addHealth(-25)) {
        other.mark();
        Game.game().addScore(100);
      }
    }
    return false;
  }

  // Do not apply gravity to this object.
  @Override
  public void onGravity() {

  }

}
