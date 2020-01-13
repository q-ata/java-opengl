import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CameraInstance extends MapItemInstance {

  public static final Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);

  private Vector3f target = new Vector3f();

  private float pitch = 0.0f;
  private float yaw = -90.0f;
  private float prevX = 0.0f;
  private float prevY = 0.0f;
  private final float SENS = 0.05f;
  private final float MOVE_SPEED = 0.06f;
  private final float SPRINT_SPEED = 0.1f;
  private final float JUMP_HEIGHT = 0.22f;

  private Grape grape = new Grape();
  private int shootCdr = 0;
  private int iframes = 0;
  private int sprintMeter = 400;
  private int sprintCdr = 30;

  public CameraInstance(Vector3f worldPos) {
    super(worldPos);
    Game.INSTANCE.addItem(grape);
  }

  public Matrix4f constructView() {
    target.setComponent(0, (float) Math.cos(Math.toRadians(pitch)) * (float) Math.cos(Math.toRadians(yaw)));
    target.setComponent(1, (float) Math.sin(Math.toRadians(pitch)));
    target.setComponent(2, (float) Math.cos(Math.toRadians(pitch)) * (float) Math.sin(Math.toRadians(yaw)));
    Vector3f result = new Vector3f();
    target.add(world(), result);
    Vector3f heighten = world();
    heighten.add(0f, 0.6f, 0f);
    result.add(0, 0.6f, 0);
    return new Matrix4f().lookAt(heighten, result, UP);
  }

  public void processMouseMovement(double newX, double newY) {
    float offX = (float) (newX - prevX);
    float offY = (float) (prevY - newY);
    offX *= SENS;
    offY *= SENS;

    yaw += offX;
    pitch += offY;

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

  @Override
  public boolean onCollision(MapItemInstance other) {
    if (!(other instanceof GrapeInstance)) {
      return super.onCollision(other);
    }
    return false;
  }

  @Override
  public void onTick() {
    Vector3f v;
    if (Game.INSTANCE.getKeyHandler().isPressed(GameConstants.ABILITY_1_KEY) && sprintMeter > 0) {
      v = Game.INSTANCE.getKeyHandler().calculateVelocity(target, SPRINT_SPEED);
      if (!v.equals(GameConstants.EMPTY, GameConstants.EPSILON)) {
        sprintMeter -= 2;
        sprintCdr = 45;
      }
    }
    else {
      v = Game.INSTANCE.getKeyHandler().calculateVelocity(target, MOVE_SPEED);
      if (sprintCdr == 0 && sprintMeter < 400) {
        sprintMeter++;
      }
      else if (sprintCdr != 0) {
        sprintCdr--;
      }
    }
    if (Game.INSTANCE.getKeyHandler().isPressed(GameConstants.JUMP_KEY) && getComponent(1)) {
      v.add(0, JUMP_HEIGHT, 0);
    }
    setVel(v.add(0, vel().y, 0));
    if (Game.INSTANCE.getClickHandler().getMouseButton(GameConstants.LEFT_CLICK) && shootCdr == 0) {
      Vector3f loc = world().add(0, 0.6f, 0);
      MapItemInstance grape = Game.INSTANCE.addInstance(loc, this.grape.id());
      Vector3f dir = new Vector3f(target).normalize().mul(GameConstants.BULLET_SPEED);
      grape.setVel(dir);
      shootCdr = GameConstants.SHOOT_COOLDOWN;
    }
    if (shootCdr != 0) {
      shootCdr--;
    }
    if (iframes != 0) {
      iframes--;
    }
    super.onTick();
  }

  @Override
  public boolean addHealth(int amt) {
    if (iframes != 0) {
      return false;
    }
    iframes = GameConstants.IFRAMES;
    return super.addHealth(amt);
  }

  public int getSprint() {
    return sprintMeter;
  }

}
