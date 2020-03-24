package enemies;

import constants.GameConfig;
import game.Game;
import items.CameraInstance;
import org.joml.Vector3f;
import structs.EnemyInstance;
import structs.MapItemInstance;

// The squash tries to jump on the player for bonus damage. The player can jump on it for instant kill.
public class SquashInstance extends EnemyInstance {

  private final float MARGIN = 0.03f;
  private final float JUMP_HEIGHT = 0.25f;

  private int prevJump = 0;
  private boolean inAir = false;

  public SquashInstance(Vector3f worldPos) {
    super(worldPos, 0.02f);
    damage = 10;
  }

  /**
   * The squash periodically tries to jump on the player and dies when jumped on.
   */
  @Override
  public void behaviour() {
    // Get direction vector to player.
    Vector3f target = Game.game().getPlayer().world();
    Vector3f targ2 = new Vector3f(target);
    Vector3f vel = target.sub(world()).normalize().mul(SPEED * GameConfig.getSpeedMulti());
    Vector3f cur = vel();
    setVel(new Vector3f(vel.x, cur.y, vel.z));
    // Attempt to jump if on the ground and within range of player and has not jumped recently.
    if (getComponent(1) && targ2.sub(world()).length() < 3f && Math.random() < 0.1f && prevJump <= 0) {
      cur = vel();
      setVel(new Vector3f(cur.x, cur.y + JUMP_HEIGHT, cur.z));
      prevJump = 60;
    }
    else {
      // Otherwise decrease cooldown to next jump if on ground.
      inAir = !getComponent(1);
      // If jumping increase speed.
      if (inAir) {
        setVel(vel().mul(5f, 1.0f, 5f));
      }
      else {
        prevJump--;
      }
    }
  }

  @Override
  public boolean onCollision(MapItemInstance other) {
    move(vel().negate());
    if (other instanceof CameraInstance) {
      Vector3f cur = world();
      Vector3f player = other.world();
      Vector3f scaleE = new Vector3f();
      Game.game().retrieve(this).model().getScale(scaleE);
      Vector3f scaleP = new Vector3f();
      Game.game().retrieve(Game.game().getPlayer()).model().getScale(scaleP);
      // Determine if player is colliding with top of squash.
      float top = cur.y + scaleE.y / 2;
      float pBot = player.y - scaleP.y / 2;
      if (top <= pBot + MARGIN) {
        // If player jumped on squash, mark it for deletion.
        mark();
      }
      else {
        // If squash jumped on player, apply 4x damage.
        float bot = cur.y - scaleE.y / 2;
        float pTop = player.y + scaleP.y / 2;
        if (bot + MARGIN >= pTop) {
          other.addHealth(-damage * 4);
        }
        // Otherwise normal damage.
        else {
          other.addHealth(-damage);
        }
      }
    }
    return true;
  }

}
