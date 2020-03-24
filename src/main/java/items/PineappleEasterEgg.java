package items;

import structs.Figure;
import structs.MapItem;
import structs.Sprite;
import structs.Transformation;

public class PineappleEasterEgg extends MapItem {

  public PineappleEasterEgg() {
    super(Figure.CUBE, Transformation.PINEAPPLE, Sprite.PINEAPPLE, PineappleEasterEggInstance.class);
  }

}