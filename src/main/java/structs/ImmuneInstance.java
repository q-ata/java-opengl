package structs;

import org.joml.Vector3f;

/**
 * Represents a structs.MapItem that cannot be marked for deletion.
 */
public abstract class ImmuneInstance extends MapItemInstance {
  public ImmuneInstance(Vector3f worldPos) {
    super(worldPos);
  }

  @Override
  public boolean addHealth(int amt) {
    // Do not allow health to increase.
    return false;
  }

}
