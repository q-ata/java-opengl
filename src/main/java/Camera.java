import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends MapItem {

  public Camera() {
    super(Figure.CUBE, Transformation.CAMERA, Sprite.CAMERA, CameraInstance.class);
  }

}
