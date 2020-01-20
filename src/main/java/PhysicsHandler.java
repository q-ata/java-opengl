import org.joml.Vector3f;

public class PhysicsHandler implements GameEvent {

  @Override
  public void reset() {

  }

  @Override
  public void run(Game game) {
    // Handle collision, tick events and gravity for all instances.
    // First remove any instances marked for deletion.
    for (int i = 0; i < game.getAll().size(); i++) {
      MapItemInstance is = game.getAll().get(i);
      if (is.garbage()) {
        game.removeInstance(i);
      }
      else {
        Vector3f loc = is.world();
        if (Math.abs(loc.x) > 500 || Math.abs(loc.y) > 500 || Math.abs(loc.z) > 500) {
          is.mark();
        }
      }
      // Otherwise run their behaviour.
      is.behaviour();
    }
    // Process all actions done every tick.
    for (int i = 0; i < game.getAll().size(); i++) {
      game.getAll().get(i).onTick();
    }
    // Check and resolve collision between all instances.
    for (int i = 0; i < game.getAll().size(); i++) {
      for (int j = i + 1; j < game.getAll().size(); j++) {
        MapItemInstance a = game.getAll().get(i);
        MapItemInstance b = game.getAll().get(j);
        if (Collision.collision(a.world(), game.retrieve(a), b.world(), game.retrieve(b)) && a.onCollision(b) && b.onCollision(a)) {
          a.onCollisionResolution(b);
          b.onCollisionResolution(a);
        }
      }
    }

    // Apply gravity to all objects.
    for (MapItemInstance instance : game.getAll()) {
      instance.onGravity();
    }
  }
}
