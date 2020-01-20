import org.joml.Vector3f;

public class PlayerMovementHandler {

  private static final int FORWARD = 1;
  private static final int BACKWARD = 2;
  private static final int RIGHT = 1;
  private static final int LEFT = 2;
  // When forward is 0, there is not forward or backward movement,
  // When forward is FORWARD, there is forward movement, when forward is BACKWARD, there is backward movement.
  // The same applies for right and left.
  private int forward = 0;
  private int right = 0;

  public PlayerMovementHandler(KeyboardInputHandler handler) {

    // Register callbacks for WASD.
    handler.addCallback('W', (press) -> {
      if (press) {
        forward = FORWARD;
      }
      // Allow backward movement to override the ended forward movement.
      else if (forward == FORWARD) {
        if (KeyboardInputHandler.KEYS['S']) {
          forward = BACKWARD;
        }
        else {
          forward = 0;
        }
      }
    });
    handler.addCallback('S', (press) -> {
      if (press) {
        forward = BACKWARD;
      }
      // Allow forward movement to override the backward movement.
      else if (forward == BACKWARD) {
        if (KeyboardInputHandler.KEYS['W']) {
          forward = FORWARD;
        }
        else {
          forward = 0;
        }
      }
    });
    handler.addCallback('A', (press) -> {
      if (press) {
        right = LEFT;
      }
      // Allow right and left to override each other.
      else if (right == LEFT) {
        if (KeyboardInputHandler.KEYS['D']) {
          right = RIGHT;
        }
        else {
          right = 0;
        }
      }
    });
    handler.addCallback('D', (press) -> {
      if (press) {
        right = RIGHT;
      }
      else if (right == RIGHT) {
        if (KeyboardInputHandler.KEYS['A']) {
          right = LEFT;
        }
        else {
          right = 0;
        }
      }
    });
  }

  /**
   * Calculate correct velocity for player movement.
   * @param target The direction the player is looking at.
   * @param sensitivity Move multiplier.
   * @return The velocity the player should move at.
   */
  public Vector3f calculateVelocity(Vector3f target, float sensitivity) {
    Vector3f fb = new Vector3f();
    Vector3f rl = new Vector3f();
    // If forward or backward movement exists.
    if (forward != 0) {
      fb.add(target);
      if (forward == BACKWARD) {
        fb.negate();
      }
    }
    // If rightward or leftward movement exists.
    if (right != 0) {
      rl.add(new Vector3f(target).cross(CameraInstance.UP));
      if (right == LEFT) {
        rl.negate();
      }
    }
    // Check if the player is not moving.
    if (fb.equals(GameConfig.EMPTY, GameConfig.EPSILON) && rl.equals(GameConfig.EMPTY, GameConfig.EPSILON)) {
      return new Vector3f(GameConfig.EMPTY);
    }
    fb.add(rl);
    // Force the player to the ground.
    fb.setComponent(1, 0);
    // Apply multiplier.
    fb.normalize().mul(sensitivity);
    return fb;
  }

  /**
   * Determine is a key is pressed.
   * @param k The key to check.
   * @return If it is pressed.
   */
  public boolean isPressed(int k) {
    return KeyboardInputHandler.KEYS[k];
  }

}
