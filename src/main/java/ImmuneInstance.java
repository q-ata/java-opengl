import org.joml.Vector3f;

public abstract class ImmuneInstance extends MapItemInstance {
  public ImmuneInstance(Vector3f worldPos) {
    super(worldPos);
  }

  @Override
  public boolean addHealth(int amt) {
    return false;
  }
}
