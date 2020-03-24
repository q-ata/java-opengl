package items;

import org.joml.Vector3f;
import structs.ImmuneInstance;
import structs.MapItemInstance;

/**
 * An immune block that makes up the map.
 */
public class WallInstance extends ImmuneInstance {

  public WallInstance(Vector3f worldPos) {
    super(worldPos);
  }

  // Do not apply gravity.
  @Override
  public void onGravity() {
  }

  @Override
  public boolean onCollision(MapItemInstance other) {
    // Do not collide with other walls.
    return !(other instanceof WallInstance);
  }

}