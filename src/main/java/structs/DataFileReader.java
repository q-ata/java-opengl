package structs;

import constants.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Wrapper for BufferedReader to parse comments (lines that start with #).
// Adapted from https://github.com/q-ata/javafx-roguelike/blob/master/src/game/DataFileReader.java
public class DataFileReader {

  private BufferedReader in;

  /**
   * Create a new file reader with given path.
   * @param path Path to file to read.
   */
  public DataFileReader(String path) {
    try {
      in = new BufferedReader(new FileReader(path));
    }
    catch (IOException e) {
      e.printStackTrace();
      Logger.error(getClass(), "Error creating structs.DataFileReader for path: " + path);
    }
  }

  /**
   * Read a line of information from file.
   * @return The line.
   */
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
      Logger.error(getClass(), "Error reading line from structs.DataFileReader.");
      return null;
    }
  }

  /**
   * Close the reader.
   */
  public void close() {
    try {
      in.close();
    }
    catch (IOException e) {
      e.printStackTrace();
      Logger.error(getClass(), "Error closing reader from structs.DataFileReader.");
    }
  }

}