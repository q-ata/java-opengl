import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

public class GameConfig {

  private static int jumpKey = ' ';
  public static int getJumpKey() {
    return jumpKey;
  }
  public static void setJumpKey(int jmp) {
    jumpKey = jmp;
  }
  private static int ability1 = 340;
  public static int getAbility1() {
    return ability1;
  }
  public static void setAbility1(int a1) {
    ability1 = a1;
  }
  private static float speedMulti = 1.0f;
  public static float getSpeedMulti() {
    return speedMulti;
  }
  public static void setSpeedMulti(float m) {
    speedMulti = m;
  }

  public static final int LEFT_CLICK = 0;
  public static final int SHOOT_COOLDOWN = 20;
  public static final int IFRAMES = 60;
  public static final float BULLET_SPEED = 0.4f;
  public static final float EPSILON = Float.MIN_VALUE;
  public static final Vector3f EMPTY = new Vector3f();
  public static final String OPTIONS_PATH = "./res/options.txt";
  public static final int MENU_WIDTH = 960;
  public static final int MENU_HEIGHT = 540;

  public static final MapItem[] ALL_ENEMIES = {null, null, null};
  public static final GameEvent[] ALL_EVENTS = {new PhysicsHandler(), new RenderHandler(), new EnemySpawner(), new FrameCounter()};

  public static String printVector(Vector3f vec) {
    DecimalFormat df = new DecimalFormat("0.00");
    return "(" + df.format(vec.x) + ", " + df.format(vec.y) + ", " + df.format(vec.z) + ")";
  }

  /**
   * Get list of top 5 scores.
   * @return List of scores in descending order.
   */
  public static int[] getScores() {
    int[] top = new int[5];
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File("./res/highscores.txt")));
      for (int i = 0; i < top.length; i++) {
        top[i] = Integer.parseInt(reader.readLine());
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return top;
  }

  public static void aa() {
    System.out.println("aoidjasoipdjasiod");
  }

}
