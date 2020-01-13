import org.joml.Vector3f;

public class AppleInstance extends EnemyInstance {

  private static final float SPEED = 0.01f;

  public AppleInstance(Vector3f worldPos) {
    super(worldPos);
    damage = 20;
  }

  @Override
  public void behaviour() {
    Vector3f target = Game.INSTANCE.getPlayer().world();
    Vector3f vel = target.sub(world()).normalize().mul(SPEED * GameConstants.SPEED_MULTIPLIER);
    Vector3f cur = vel();
    setVel(new Vector3f(vel.x, cur.y, vel.z));
  }

}
