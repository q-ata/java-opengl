import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

public class KeyboardInputHandler implements GLFWKeyCallbackI {

  public static final boolean[] KEYS = new boolean[512];
  
  @Override
  public void invoke(long window, int key, int scancode, int action, int mods) {
    if (action == GLFW.GLFW_PRESS) {
      KEYS[key] = true;
    }
    else if (action == GLFW.GLFW_RELEASE) {
      KEYS[key] = false;
    }
  }

}
