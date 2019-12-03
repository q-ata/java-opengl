import org.joml.Vector3f;

public class PlayerMovementHandler {

  private static final int FORWARD = 1;
  private static final int BACKWARD = 2;
  private static final int RIGHT = 1;
  private static final int LEFT = 2;
  private int moving = 0;
  private int forward = 0;
  private int right = 0;

  public PlayerMovementHandler(KeyboardInputHandler handler) {

    handler.addCallback('W', (press) -> {
      if (press) {
        forward = FORWARD;
      }
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

  public Vector3f calculateVelocity(Vector3f target, float sensitivity) {
    Vector3f fb = new Vector3f();
    Vector3f rl = new Vector3f();
    if (forward != 0) {
      fb.add(target);
      if (forward == BACKWARD) {
        fb.negate();
      }
    }
    if (right != 0) {
      rl.add(target.cross(Camera.UP));
      if (right == LEFT) {
        rl.negate();
      }
    }
    Vector3f empty =  new Vector3f();
    if (fb.equals(empty, Constants.EPSILON) && rl.equals(empty, Constants.EPSILON)) {
      return empty;
    }
    fb.add(rl);
    fb.setComponent(1, 0);
    fb.normalize().mul(sensitivity);
    return fb;
  }

}
