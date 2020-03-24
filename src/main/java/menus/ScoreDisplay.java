package menus;

import constants.GameConfig;
import structs.Menu;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// A game over display giving the final score.
public class ScoreDisplay extends JFrame implements Menu {

  private int score;

  /**
   * Create a new game.Game Over screen.
   * @param score The score achieved.
   */
  public ScoreDisplay(int score) {
    this.score = score;
  }

  @Override
  public void start() {
    // Read in top scores.
    int[] top = GameConfig.getScores();
    int index = 5;
    // Insert the current score in correct position.
    for (int i = 0; i < top.length; i++) {
      if (top[i] <= score) {
        index = i;
        break;
      }
    }
    if (index != 5) {
      for (int i = top.length - 1; i > index; i--) {
        top[i] = top[i - 1];
      }
      top[index] = score;
    }
    try {
      // Write to highscores file.
      BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./res/highscores.txt")));
      writer.write(top[0] + "\n" + top[1] + "\n" + top[2] + "\n" + top[3] + "\n" + top[4]);
      writer.flush();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    setTitle("game.Game Over");

    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(200, 100));
    panel.setLayout(null);
    // Show text.
    JLabel info = new JLabel("Final score: " + score);
    info.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
    info.setBounds(20, 20, 160, 60);
    panel.add(info);

    getContentPane().add(panel);
    pack();
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
    setVisible(true);
  }

}
