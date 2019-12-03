public class Wall extends MapItem {
  public Wall() {
    super(Figure.QUAD, Transformation.IDENTITY, Sprite.WALL, WallInstance.class);
  }
}
