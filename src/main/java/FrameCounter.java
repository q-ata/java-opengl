public class FrameCounter {

  private long lastSecond = System.currentTimeMillis();
  private int frames = 0;
  private int fps = 0;

  public boolean increment() {
    frames++;
    if (System.currentTimeMillis() - lastSecond >= 1000) {
      lastSecond = System.currentTimeMillis();
      fps = frames;
      frames = 0;
      return true;
    }
    return false;
  }

  public int get() {
    return fps;
  }

}
