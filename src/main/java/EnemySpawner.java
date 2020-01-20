import org.joml.Vector3f;

public class EnemySpawner implements GameEvent {

  private int minInt;
  private int maxInt;
  private int minMinInt;
  private float intCurve;
  private int stageInt;
  private float spawnDist;
  private float spawnDistVariance;
  private float velIncrease;

  private int reqInt;
  private int curInt;
  private int curStageCounter;
  private int stage = 0;

  @Override
  public void reset() {

    curInt = curStageCounter = stage = 0;

    Game game = Game.game();
    // Minimum time to spawn enemy.
    minInt = (int) game.getOption("minInt");
    // Maximum time to spawn enemy.
    maxInt = (int) game.getOption("maxInt");
    // Minimum value of minInt.
    minMinInt = (int) game.getOption("maxIntReduction");
    // Multiplier for minInt and maxInt every stage.
    intCurve = game.getOption("intCurve");
    // Time between increasing stage.
    stageInt = (int) game.getOption("stageInt");
    // Minimum distance away from player to spawn.
    spawnDist = game.getOption("spawnDist");
    // spawnDist + spawnDistVariance = max distance from player.
    spawnDistVariance = game.getOption("spawnDistVariance");
    // Flat increase to velocity multiplier every stage.
    velIncrease = game.getOption("enemyVelIncrease");

    reqInt = (int) (Math.random() * (maxInt - minInt)) + minInt;

  }

  @Override
  public void run(Game game) {
    // If time to spawn new enemy.
    if (curInt++ == reqInt) {
      // Pick a random enemy to spawn.
      MapItem enemy = GameConfig.ALL_ENEMIES[(int) (Math.random() * GameConfig.ALL_ENEMIES.length)];
      Vector3f playerPos = Game.game().getPlayer().world();
      double deg;
      double x;
      double y;
      Vector3f spawnPos;
      int spawnAttempts = 0;
      // Repeatedly attempt to spawn new enemy, give up after 20 tries.
      do {
        // Random direction to spawn in.
        deg = Math.toRadians(Math.random() * 360);
        // Determine the unit circle coordinate for given degrees.
        x = Math.cos(deg);
        y = Math.sin(deg);
        // Extend in the calculated direction.
        spawnPos = new Vector3f((float) x, 0, (float) y).mul(spawnDist + (float) Math.random() * spawnDistVariance);
        spawnPos.add(playerPos);
        spawnPos.setComponent(1, 2f);
      }
      // Do not allow if object would be outside of map.
      while ((Math.abs(spawnPos.x) > 14 || Math.abs(spawnPos.z) > 14) && ++spawnAttempts < 20);
      if (spawnAttempts == 20) {
        Logger.debug(getClass(), "Too many failed spawn attempts, aborting spawn.");
      }
      else {
        Game.game().addInstance(spawnPos, enemy.id());
        Logger.debug(getClass(), "Spawned " + enemy.getClass().getName() + " at " + GameConfig.printVector(spawnPos));
      }
      curInt = 0;
      // Generate new random interval for enemy spawn.
      reqInt = (int) (Math.random() * (maxInt - minInt)) + minInt;
    }
    // If time to accelerate enemy spawning.
    if (curStageCounter++ == stageInt) {
      minInt *= intCurve;
      // Do not go past set minimum.
      if (minInt < minMinInt) {
        minInt = minMinInt;
      }
      else {
        maxInt *= intCurve;
      }
      stage++;
      curStageCounter = 0;
      // Increase enemy velocity.
      GameConfig.setSpeedMulti(GameConfig.getSpeedMulti() + velIncrease);
      Logger.debug(getClass(), "Increased stage to " + stage + " min int = " + minInt + ", max int = " + maxInt + " vel multi = " + GameConfig.getSpeedMulti());
    }

  }
}
