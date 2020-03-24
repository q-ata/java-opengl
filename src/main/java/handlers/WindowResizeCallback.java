package handlers;

import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.opengl.GL33;
import structs.Window;

/**
 * Callback is run whenever the window is resized.
 */
public class WindowResizeCallback implements GLFWFramebufferSizeCallbackI {
  
  private Window window;
  
  /**
   * Create a new callback.
   * @param window
   */
  public WindowResizeCallback(Window window) {
    this.window = window;
  }

  @Override
  public void invoke(long window, int width, int height) {
    // Update dimensions in window object.
    this.window.setWidth(width);
    this.window.setHeight(height);
    GL33.glViewport(0, 0, width, height);
  }

}
