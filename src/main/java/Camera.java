import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends MapItem {

  public static final Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);

  private Vector3f target = new Vector3f();
  private Vector3f pos = new Vector3f();

  private float pitch = 0.0f;
  private float yaw = -90.0f;

  private float prevX = 0.0f;
  private float prevY = 0.0f;

  private float sensitivity = 0.05f;

  public Camera() {
    super(Figure.CUBE, Transformation.IDENTITY, Sprite.CAMERA, DonaldTrumpInstance.class);
  }

  public Matrix4f constructView() {
    target.setComponent(0, (float) Math.cos(Math.toRadians(pitch)) * (float) Math.cos(Math.toRadians(yaw)));
    target.setComponent(1, (float) Math.sin(Math.toRadians(pitch)));
    target.setComponent(2, (float) Math.cos(Math.toRadians(pitch)) * (float) Math.sin(Math.toRadians(yaw)));
    Vector3f result = new Vector3f();
    target.add(pos(), result);
    return new Matrix4f().lookAt(pos(), result, UP);
  }

  public void processMouseMovement(double newX, double newY) {
    float offX = (float) (newX - prevX);
    float offY = (float) (prevY - newY);
    offX *= sensitivity;
    offY *= sensitivity;

    yaw += offX;
    pitch += offY;

    if (pitch > 89.0f) {
      pitch = 89.0f;
    }
    else if (pitch < -89.0f) {
      pitch = -89.0f;
    }
    if (Math.abs(yaw) >= 360.0f) {
      yaw = 0.0f;
    }
    prevX = (float) newX;
    prevY = (float) newY;
  }

  public Vector3f target() {
    return target;
  }

  public void addPos(Vector3f pos) {
    this.pos.add(pos);
  }

  public Vector3f pos() {
    return pos;
  }

}
