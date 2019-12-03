import org.joml.Vector3f;

public class Collision {

  public static boolean collision(Vector3f posA, MapItem typeA, Vector3f posB, MapItem typeB) {
    float disX = Math.abs(posB.x() - posA.x());
    float disY = Math.abs(posB.y() - posA.y());
    float disZ = Math.abs(posB.z() - posA.z());
    return disX < 1 && disY < 1 && disZ < 1;
  }

}
