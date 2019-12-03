public class Constants {

  public static final float EPSILON = Float.MIN_VALUE;

  // Approximate epsilon.
  private static float calculate() {
    float e = 0.5f;
    while (1.0 + e > 1.0) {
      e *= 0.5;
    }
    return e;
  }

}
