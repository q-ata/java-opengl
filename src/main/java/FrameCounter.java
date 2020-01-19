public class FrameCounter implements GameEvent {

  private long lastSecond = System.currentTimeMillis();
  private int frames = 0;
  private int fps = 0;

  @Override
  public void reset() {
    frames = fps = 0;
    lastSecond = System.currentTimeMillis();
  }

  @Override
  public void run(Game game) {

    frames++;
    if (System.currentTimeMillis() - lastSecond >= 1000) {
      lastSecond = System.currentTimeMillis();
      fps = frames;
      frames = 0;
      System.out.println(fps + " FPS");
    }

  }

}
