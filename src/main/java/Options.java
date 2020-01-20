import java.util.HashMap;
import java.util.Map;

public class Options {

  private Map<String, Float> values = new HashMap<>();

  /**
   * Create new options from file path.
   * @param optionsPath The options path.
   */
  public Options(String optionsPath) {
    DataFileReader reader = new DataFileReader(optionsPath);
    String line;
    while ((line = reader.readLine()) != null) {
      int pos = line.indexOf('=');
      String name = "";
      String value = "";
      try {
        // Assign float value for each option.
        name = line.substring(0, pos);
        value = line.substring(pos + 1);
        float val = Float.parseFloat(value);
        values.put(name, val);
      }
      catch (NumberFormatException e) {
        e.printStackTrace();
        Logger.error(getClass(), "Could not parse value " + value + " for option " + name);
      }
    }

    values.put("ar", values.get("width") / values.get("height"));
  }

  /**
   * Get value of an option.
   * @param name The option name.
   * @return The value.
   */
  public float get(String name) {
    Float f = values.get(name);
    if (f == null) {
      Logger.error(getClass(), "Failed to retrieve option value for " + name);
      return -1;
    }
    return f;
  }

}
