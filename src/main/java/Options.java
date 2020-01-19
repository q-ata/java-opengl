import java.util.HashMap;
import java.util.Map;

public class Options {

  private Map<String, Float> values = new HashMap<>();

  public Options(String optionsPath) {
    DataFileReader reader = new DataFileReader(optionsPath);
    String line;
    while ((line = reader.readLine()) != null) {
      int pos = line.indexOf('=');
      String name = "";
      String value = "";
      try {
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

  public float get(String name) {
    return values.get(name);
  }

}
