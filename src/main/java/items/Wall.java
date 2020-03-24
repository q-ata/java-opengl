package items;

import structs.Figure;
import structs.MapItem;
import structs.Sprite;
import structs.Transformation;

public class Wall extends MapItem {
  public Wall() {
    super(Figure.CUBE, Transformation.WALL, Sprite.WALL, WallInstance.class);
  }
}
