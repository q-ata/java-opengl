package handlers;

import constants.Logger;
import game.Game;
import structs.GameEvent;

public class FrameCounter implements GameEvent {

  private long lastSecond = System.currentTimeMillis();
  private int frames = 0;
  private int fps = 0;

  @Override
  public void reset() {
    frames = fps = 0;
    lastSecond = System.currentTimeMillis();
  }

  /**
   * Prints FPS every second.
   * @param game The singleton game.Game object.
   */
  @Override
  public void run(Game game) {

    frames++;
    if (System.currentTimeMillis() - lastSecond >= 1000) {
      lastSecond = System.currentTimeMillis();
      fps = frames;
      frames = 0;
      Logger.debug(getClass(), fps + " FPS");
    }

  }

}
