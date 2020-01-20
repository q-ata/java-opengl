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
    minInt = (int) game.getOption("minInt");
    maxInt = (int) game.getOption("maxInt");
    minMinInt = (int) game.getOption("maxIntReduction");
    intCurve = game.getOption("intCurve");
    stageInt = (int) game.getOption("stageInt");
    spawnDist = game.getOption("spawnDist");
    spawnDistVariance = game.getOption("spawnDistVariance");
    velIncrease = game.getOption("enemyVelIncrease");

    reqInt = (int) (Math.random() * (maxInt - minInt)) + minInt;

  }

  @Override
  public void run(Game game) {
    if (curInt++ == reqInt) {
      MapItem enemy = GameConfig.ALL_ENEMIES[(int) (Math.random() * GameConfig.ALL_ENEMIES.length)];
      Vector3f playerPos = Game.game().getPlayer().world();
      double deg;
      double x;
      double y;
      Vector3f spawnPos;
      int spawnAttempts = 0;
      do {
        deg = Math.toRadians(Math.random() * 360);
        x = Math.cos(deg);
        y = Math.sin(deg);
        spawnPos = new Vector3f((float) x, 0, (float) y).mul(spawnDist + (float) Math.random() * spawnDistVariance);
        spawnPos.add(playerPos);
        spawnPos.setComponent(1, 2f);
      }
      while ((Math.abs(spawnPos.x) > 14 || Math.abs(spawnPos.z) > 14) && ++spawnAttempts < 20);
      if (spawnAttempts == 20) {
        Logger.debug(getClass(), "Too many failed spawn attempts, aborting spawn.");
      }
      else {
        Game.game().addInstance(spawnPos, enemy.id());
        Logger.debug(getClass(), "Spawned " + enemy.getClass().getName() + " at " + GameConfig.printVector(spawnPos));
      }
      curInt = 0;
      reqInt = (int) (Math.random() * (maxInt - minInt)) + minInt;
    }
    if (curStageCounter++ == stageInt) {
      minInt *= intCurve;
      if (minInt < minMinInt) {
        minInt = minMinInt;
      }
      else {
        maxInt *= intCurve;
      }
      stage++;
      curStageCounter = 0;
      GameConfig.setSpeedMulti(GameConfig.getSpeedMulti() + velIncrease);
      Logger.debug(getClass(), "Increased stage to " + stage + " min int = " + minInt + ", max int = " + maxInt + " vel multi = " + GameConfig.getSpeedMulti());
    }

  }
}
