import org.joml.Matrix4f;
import org.joml.Vector3f;

public enum Transformation {

  APPLE(new Matrix4f().scale(1.5f, 1.5f, 1.5f)),
  CAMERA(new Matrix4f().scale(0.5f, 1f, 0.5f)),
  WALL(new Matrix4f().scale(10f, 10f, 10f)),
  PINEAPPLE(new Matrix4f().scale(1.1f, 2.8f, 1.1f)),
  GRAPE(new Matrix4f().scale(0.1f, 0.1f, 0.1f)),
  SQUASH(new Matrix4f().scale(0.9f, 1.5f, 0.9f)),
  PEAR(new Matrix4f()),
  IDENTITY(new Matrix4f());

  private final Matrix4f transform;

  private Transformation(Matrix4f transform) {
    this.transform = transform;
  }

  public Matrix4f get() {
    return transform;
  }

}
