import org.joml.Matrix4f;
import org.joml.Vector3f;

public enum Transformation {

  APPLE(new Matrix4f().scale(1.5f, 1.5f, 1.5f)),
  CAMERA(new Matrix4f().scale(0.5f, 1f, 0.5f)),
  WALL(new Matrix4f().scale(10f, 1f, 10f)),
  GRAPE(new Matrix4f().scale(0.1f, 0.1f, 0.1f)),
  IDENTITY(new Matrix4f());

  private final Matrix4f transform;

  private Transformation(Matrix4f transform) {
    this.transform = transform;
  }

  public Matrix4f get() {
    return transform;
  }

}
