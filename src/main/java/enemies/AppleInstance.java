package enemies;

import constants.GameConfig;
import game.Game;
import org.joml.Vector3f;
import structs.EnemyInstance;

public class AppleInstance extends EnemyInstance {

  public AppleInstance(Vector3f worldPos) {
    super(worldPos, 0.03f);
    damage = 20;
    addHealth(100);
  }

  /**
   * The apple slowly appraoches the player at a constant speed.
   */
  @Override
  public void behaviour() {
    // Slowly approach the player in a straight line.
    Vector3f target = Game.game().getPlayer().world();
    Vector3f vel = target.sub(world()).normalize().mul(SPEED * GameConfig.getSpeedMulti());
    Vector3f cur = vel();
    setVel(new Vector3f(vel.x, cur.y, vel.z));
  }

}
