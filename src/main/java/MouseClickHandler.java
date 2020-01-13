import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class MouseClickHandler implements GLFWMouseButtonCallbackI {

  private boolean[] mouseState = new boolean[32];

  @Override
  public void invoke(long window, int button, int action, int mod) {

    mouseState[button] = !mouseState[button];

  }

  public boolean getMouseButton(int b) {
    return mouseState[b];
  }

}
