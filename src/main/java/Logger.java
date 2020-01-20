/**
 * General logging utility.
 */
public class Logger {

  /**
   * Report a critical error.
   * @param source The class from which the error originated.
   * @param msg The error message.
   */
  public static void error(Class<?> source, String msg) {
    System.err.println("[ERROR]: (" + source.getName() + ") " + msg);
  }

  /**
   * Report a diagnostic message.
   * @param source The class from which the message originated.
   * @param msg The message.
   */
  public static void debug(Class<?> source, String msg) {
    System.out.println("[DEBUG]: (" + source.getName() + ") " + msg);
  }

}
