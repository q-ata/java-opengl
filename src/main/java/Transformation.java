import org.joml.Matrix4f;
import org.joml.Vector3f;

public enum Transformation {

  TRUMP(new Matrix4f()),
  CAMERA(new Matrix4f().scale(new Vector3f(0.5f, 1.0f, 0.5f))),
  IDENTITY(new Matrix4f());

  private final Matrix4f transform;

  private Transformation(Matrix4f transform) {
    this.transform = transform;
  }

  public Matrix4f get() {
    return transform;
  }

}
