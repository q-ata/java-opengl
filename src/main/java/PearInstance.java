import org.joml.Vector3f;

public class PearInstance extends EnemyInstance {

  private int toDash = 120;
  private int remainDash = 0;
  private boolean dashing = false;
  private Vector3f dashVel;
  private Vector3f idleVel;

  public PearInstance(Vector3f worldPos) {
    super(worldPos, 0.02f);
    damage = 10;
  }

  @Override
  public void behaviour() {

    // The pear should periodically dash towards the player.
    Vector3f target = Game.game().getPlayer().world();
    Vector3f vel = target.sub(world()).normalize().mul(SPEED * GameConfig.getSpeedMulti());
    Vector3f modded = new Vector3f(vel.x, vel().y, vel.z);

    // If counting down to dash.
    if (toDash-- > 0) {
      if (idleVel == null) {
        setVel(modded);
      }
      else {
        setVel(idleVel);
      }
    }
    else if (remainDash == 0) {
      // If dash just finished.
      if (dashing) {
        toDash = (int) (Math.random() * 60) + 120;
        dashing = false;
        idleVel = dashVel.mul(0.1f, 1.0f, 0.1f);
      }
      // Otherwise dash just began.
      else {
        dashing = true;
        modded.mul(10f, 1.0f, 10f);
        remainDash = (int) (Game.game().getPlayer().world().sub(world()).length() / modded.length());
        remainDash *= Math.random() + 1;
        dashVel = modded;
      }
    }
    else {
      setVel(dashVel);
      remainDash--;
    }

  }

}
