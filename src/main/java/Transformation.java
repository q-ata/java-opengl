import org.joml.Matrix4f;
import org.joml.Vector3f;

public enum Transformation {

  TRUMP(new Matrix4f().rotate((float) Math.toRadians(45), new Vector3f(1.0f, 0.0f, 0.0f))),
  IDENTITY(new Matrix4f());

  private final Matrix4f transform;

  private Transformation(Matrix4f transform) {
    this.transform = transform;
  }

  public Matrix4f get() {
    return transform;
  }

}
