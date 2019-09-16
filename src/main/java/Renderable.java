import org.lwjgl.opengl.GL33;

public abstract class Renderable extends MapItem {

  private Sprite sprite;

  public Renderable(Figure shape, Transformation transform, Sprite sprite) {
    super(shape, transform);
    this.sprite = sprite;
  }

  public void bind() {
    GL33.glActiveTexture(GL33.GL_TEXTURE0);
    GL33.glBindTexture(GL33.GL_TEXTURE0, sprite.get());
  }

}
