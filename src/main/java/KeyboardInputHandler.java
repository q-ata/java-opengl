import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardInputHandler implements GLFWKeyCallbackI {

  // Represents the state of every key.
  public static final boolean[] KEYS = new boolean[512];

  // Maps key to callback.
  private Map<Integer, List<KeyPressCallback>> cbs = new HashMap<>();
  
  @Override
  public void invoke(long window, int key, int scancode, int action, int mods) {
    // Set state of affected key.
    if (action == GLFW.GLFW_PRESS) {
      KEYS[key] = true;
    }
    else if (action == GLFW.GLFW_RELEASE) {
      KEYS[key] = false;
    }
    if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_RELEASE) {
      if (!cbs.containsKey(key)) {
        return;
      }
      // Run all callbacks for this key.
      for (KeyPressCallback cb : cbs.get(key)) {
        cb.run(action == GLFW.GLFW_PRESS);
      }
    }

  }

  /**
   * Register a new callback for a key.
   * @param key The key.
   * @param cb The callback.
   */
  public void addCallback(int key, KeyPressCallback cb) {
    if (!cbs.containsKey(key)) {
      cbs.put(key, new ArrayList<>());
    }
    cbs.get(key).add(cb);
  }

}
