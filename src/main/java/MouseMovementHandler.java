import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class MouseMovementHandler implements GLFWCursorPosCallbackI {

  private final float SENS = Game.game().getOption("sensitivity");
  // Previous x coordinate of mouse.
  private float prevX = 0;
  // Previous y coordinate of mouse.
  private float prevY = 0;
  private float yaw = -90f;
  private float pitch = 0;

  @Override
  public void invoke(long window, double newX, double newY) {
    // Get delta in mouse coordinate.
    float offX = (float) (newX - prevX);
    float offY = (float) (prevY - newY);
    offX *= SENS;
    offY *= SENS;

    // Add delta to pitch and yaw.
    yaw += offX;
    pitch += offY;

    // Constrain pitch and yaw.
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

  /**
   * Get current rotational yaw.
   * @return The yaw.
   */
  public float getYaw() {
    return yaw;
  }

  /**
   * Get current rotational pitch.
   * @return The pitch.
   */
  public float getPitch() {
    return pitch;
  }
}
