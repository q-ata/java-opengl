package structs;

/**
 * Represents something that needs to be reset every time the game is restarted.
 */
public interface Resettable {

  /**
   * Reset this object for use in a new game.
   */
  public void reset();

}
