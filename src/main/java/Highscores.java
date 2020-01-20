import javax.swing.*;
import java.awt.*;

public class Highscores extends JFrame implements Menu {

  public void start() {
    int[] top = GameConfig.getScores();
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(300, 400));
    panel.setLayout(null);
    JLabel info = new JLabel();
    info.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
    info.setBounds(60, 40, 120, 200);
    StringBuilder sb = new StringBuilder("<html>High Scores");
    int pos = 1;
    for (int score : top) {
      sb.append("<br>");
      sb.append(pos++);
      sb.append(". ");
      sb.append(score);
    }
    info.setText(sb.toString());
    panel.add(info);

    getContentPane().add(panel);
    pack();
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
    setVisible(true);
  }

}
