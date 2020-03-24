package items;

import structs.Figure;
import structs.MapItem;
import structs.Sprite;
import structs.Transformation;

public class Camera extends MapItem {

  public Camera() {
    super(Figure.CUBE, Transformation.CAMERA, Sprite.CAMERA, CameraInstance.class);
  }

}
