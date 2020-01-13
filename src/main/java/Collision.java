import org.joml.Vector3f;

public class Collision {

  public static boolean collision(Vector3f posA, MapItem typeA, Vector3f posB, MapItem typeB) {
    Vector3f scaleA = new Vector3f();
    typeA.model().getScale(scaleA);
    Vector3f scaleB = new Vector3f();
    typeB.model().getScale(scaleB);
    // posA and posB represent the center of the hitbox.
    return posA.x() + scaleA.x() / 2 > posB.x() - scaleB.x() / 2 &&
            posB.x() + scaleB.x() / 2 > posA.x() - scaleA.x() / 2 &&
            posA.y() + scaleA.y() / 2 > posB.y() - scaleB.y() / 2 &&
            posB.y() + scaleB.y() / 2 > posA.y() - scaleA.y() / 2 &&
            posA.z() + scaleA.z() / 2 > posB.z() - scaleB.z() / 2 &&
            posB.z() + scaleB.z() / 2 > posA.z() - scaleA.z() / 2;
  }

}
