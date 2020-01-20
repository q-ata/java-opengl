public interface GameEvent extends Resettable {

  /**
   * Process to run every frame.
   * @param game The singleton Game object.
   */
  public void run(Game game);

}
