package enemies;

import structs.Figure;
import structs.MapItem;
import structs.Sprite;
import structs.Transformation;

public class Squash extends MapItem {

  public Squash() {
    super(Figure.CUBE, Transformation.SQUASH, Sprite.SQUASH, SquashInstance.class);
  }

}
