import org.joml.Vector3f;

public abstract class MapItemInstance {

  private Vector3f worldPos;

  public MapItemInstance(Vector3f worldPos) {
    this.worldPos = worldPos;
  }

  public abstract void behaviour();

  public void move(Vector3f vel) {
    worldPos.add(vel);
  }

  public Vector3f world() {
    return worldPos;
  }

}
