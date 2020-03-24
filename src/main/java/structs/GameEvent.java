package structs;

import game.Game;

public interface GameEvent extends Resettable {

  /**
   * Process to run every frame.
   * @param game The singleton game.Game object.
   */
  public void run(Game game);

}
