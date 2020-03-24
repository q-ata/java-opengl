package enemies;

import structs.Figure;
import structs.MapItem;
import structs.Sprite;
import structs.Transformation;

public class Pear extends MapItem {

  public Pear() {
    super(Figure.CUBE, Transformation.PEAR, Sprite.PEAR, PearInstance.class);
  }

}
