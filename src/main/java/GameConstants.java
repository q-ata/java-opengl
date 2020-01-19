import org.joml.Vector3f;

public class GameConstants {

  public static int JUMP_KEY = ' ';
  public static int ABILITY_1_KEY = 340;
  public static int ABILITY_2_KEY = 'E';
  public static float SPEED_MULTIPLIER = 1.0f;
  public static int DIFFICULTY_INTERVAL = 600;

  public static final int LEFT_CLICK = 0;
  public static final int SHOOT_COOLDOWN = 20;
  public static final int IFRAMES = 60;
  public static final float BULLET_SPEED = 0.4f;
  public static final float EPSILON = Float.MIN_VALUE;
  public static final Vector3f EMPTY = new Vector3f();
  public static final String OPTIONS_PATH = "./res/options.txt";
  public static final int WINDOW_WIDTH = 960;
  public static final int WINDOW_HEIGHT = 540;

  public static final MapItem[] ALL_ENEMIES = {new Apple(), new Squash(), new Pear()};
  public static final GameEvent[] ALL_EVENTS = {new PhysicsHandler(), new RenderHandler(), new EnemySpawner(), new FrameCounter()};

}
