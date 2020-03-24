package items;

import structs.Figure;
import structs.MapItem;
import structs.Sprite;
import structs.Transformation;

public class Grape extends MapItem {

  public Grape() {
    super(Figure.CUBE, Transformation.GRAPE, Sprite.GRAPE, GrapeInstance.class);
  }

}
