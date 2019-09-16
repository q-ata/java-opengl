import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class MouseMovementHandler implements GLFWCursorPosCallbackI {

  private Camera camera;

  public MouseMovementHandler(Camera camera) {
    this.camera = camera;
  }

  @Override
  public void invoke(long window, double xpos, double ypos) {
    camera.processMouseMovement(xpos, ypos);
  }
}
