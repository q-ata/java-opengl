import org.joml.Vector3f;

public class Collision {

  public static boolean collision(Vector3f posA, MapItem typeA, Vector3f posB, MapItem typeB) {
    Vector3f scaleA = new Vector3f();
    typeA.model().getScale(scaleA);
    Vector3f scaleB = new Vector3f();
    typeB.model().getScale(scaleB);
    return posA.x() + scaleA.x() > posB.x() &&
            posB.x() + scaleB.x() > posA.x() &&
            posA.y() + scaleA.y() > posB.y() &&
            posB.y() + scaleB.y() > posA.y() &&
            posA.z() + scaleA.z() > posB.z() &&
            posB.z() + scaleB.z() > posA.z();
  }

}
