
public class Logger {
  
  public static void error(Class<?> source, String msg) {
    System.err.println("[ERROR]: (" + source.getName() + ") " + msg);
  }
  
  public static void debug(Class<?> source, String msg) {
    System.out.println("[DEBUG]: (" + source.getName() + ") " + msg);
  }

}
