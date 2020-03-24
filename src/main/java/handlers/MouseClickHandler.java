package handlers;

import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class MouseClickHandler implements GLFWMouseButtonCallbackI {

  // State for every mouse button.
  private boolean[] mouseState = new boolean[32];

  @Override
  public void invoke(long window, int button, int action, int mod) {

    // Set relevant state.
    mouseState[button] = !mouseState[button];

  }

  /**
   * Get the state of a mouse button.
   * @param b The button.
   * @return The state.
   */
  public boolean getMouseButton(int b) {
    return mouseState[b];
  }

}
