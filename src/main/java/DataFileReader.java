import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Wrapper for BufferedReader to parse comments (lines that start with #).
public class DataFileReader {

  private BufferedReader in;

  public DataFileReader(String path) {
    try {
      in = new BufferedReader(new FileReader(path));
    }
    catch (IOException e) {
      e.printStackTrace();
      Logger.error(getClass(), "Error creating DataFileReader for path: " + path);
    }
  }

  public String readLine() {
    try {
      // Skip lines until it reaches one that is not a comment.
      String line = in.readLine();
      if (line == null) {
        return null;
      }
      while (line.startsWith("#")) {
        line = in.readLine();
      }
      return line;
    }
    catch (IOException e) {
      e.printStackTrace();
      Logger.error(getClass(), "Error reading line from DataFileReader.");
      return null;
    }
  }

  public void close() {
    try {
      in.close();
    }
    catch (IOException e) {
      e.printStackTrace();
      Logger.error(getClass(), "Error closing reader from DataFileReader.");
    }
  }

}