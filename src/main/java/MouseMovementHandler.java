import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class MouseMovementHandler implements GLFWCursorPosCallbackI {

  private CameraInstance camera;

  public MouseMovementHandler(CameraInstance camera) {
    this.camera = camera;
  }

  @Override
  public void invoke(long window, double xpos, double ypos) {
    camera.processMouseMovement(xpos, ypos);
  }
}
