package items;

import constants.GameConfig;
import game.Game;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import structs.MapItemInstance;

public class CameraInstance extends MapItemInstance {

  public static final Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);

  private Vector3f target = new Vector3f();

  private final float MOVE_SPEED = 0.06f;
  private final float SPRINT_SPEED = 0.12f;
  private final float JUMP_HEIGHT = 0.22f;

  private Grape grape = new Grape();
  private int shootCdr = 0;
  private int iframes = 0;
  private int sprintMeter = 400;
  private int sprintCdr = 30;

  public CameraInstance(Vector3f worldPos) {
    super(worldPos);
    Game.game().addItem(grape);
  }

  /**
   * Constructs a view matrix. Dynamic view camera created with help from https://learnopengl.com/Getting-started/Camera
   * @return The view matrix.
   */
  public Matrix4f constructView() {
    float yaw = Game.game().getMouseHandler().getYaw();
    float pitch = Game.game().getMouseHandler().getPitch();
    // Determine the current rotation of the camera based on yaw and pitch.
    target.setComponent(0, (float) Math.cos(Math.toRadians(pitch)) * (float) Math.cos(Math.toRadians(yaw)));
    target.setComponent(1, (float) Math.sin(Math.toRadians(pitch)));
    target.setComponent(2, (float) Math.cos(Math.toRadians(pitch)) * (float) Math.sin(Math.toRadians(yaw)));
    Vector3f result = new Vector3f();
    // Add current world coordinates.
    target.add(world(), result);
    Vector3f heighten = world();
    // Make the player appear taller.
    heighten.add(0f, 0.6f, 0f);
    result.add(0, 0.6f, 0);
    // Generate view matrix.
    return new Matrix4f().lookAt(heighten, result, UP);
  }

  @Override
  public boolean onCollision(MapItemInstance other) {
    // Do not collide with own projectiles.
    if (!(other instanceof GrapeInstance)) {
      return super.onCollision(other);
    }
    return false;
  }

  @Override
  public void onTick() {
    Vector3f v;
    // Check if sprinting.
    if (Game.game().getKeyHandler().isPressed(GameConfig.getAbility1()) && sprintMeter > 0) {
      v = Game.game().getKeyHandler().calculateVelocity(target, SPRINT_SPEED);
      // Consume sprint meter.
      if (!v.equals(GameConfig.EMPTY, GameConfig.EPSILON)) {
        sprintMeter -= 2;
        sprintCdr = 45;
      }
    }
    else {
      v = Game.game().getKeyHandler().calculateVelocity(target, MOVE_SPEED);
      // Start cooldown after sprintCdr has passed.
      if (sprintCdr == 0 && sprintMeter < 400) {
        sprintMeter++;
      }
      else if (sprintCdr != 0) {
        sprintCdr--;
      }
    }
    // Check if jumping.
    if (Game.game().getKeyHandler().isPressed(GameConfig.getJumpKey()) && getComponent(1)) {
      // Add a y component to velocity.
      v.add(0, JUMP_HEIGHT, 0);
    }
    setVel(v.add(0, vel().y, 0));
    // Check if shooting.
    if (Game.game().getClickHandler().getMouseButton(GameConfig.LEFT_CLICK) && shootCdr == 0) {
      Vector3f loc = world().add(0, 0.6f, 0);
      // Create new projectile.
      MapItemInstance grape = Game.game().addInstance(loc, this.grape.id());
      // Direct towards camera location.
      Vector3f dir = new Vector3f(target).normalize().mul(GameConfig.BULLET_SPEED);
      grape.setVel(dir);
      shootCdr = GameConfig.SHOOT_COOLDOWN;
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
    // Do not take damage in invincibility frames are active.
    if (iframes != 0) {
      return false;
    }
    iframes = GameConfig.IFRAMES;
    return super.addHealth(amt);
  }

  public int getSprint() {
    return sprintMeter;
  }

}
