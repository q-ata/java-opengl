import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class MouseMovementHandler implements GLFWCursorPosCallbackI {

  private final float SENS = Game.game().getOption("sensitivity");
  private float prevX = 0;
  private float prevY = 0;
  private float yaw = -90f;
  private float pitch = 0;

  @Override
  public void invoke(long window, double newX, double newY) {
    float offX = (float) (newX - prevX);
    float offY = (float) (prevY - newY);
    offX *= SENS;
    offY *= SENS;

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

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }
}
