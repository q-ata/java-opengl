package enemies;

import structs.Figure;
import structs.MapItem;
import structs.Sprite;
import structs.Transformation;

public class Apple extends MapItem {

  public Apple() {
    super(Figure.CUBE, Transformation.APPLE, Sprite.APPLE, AppleInstance.class);
  }
  
}
