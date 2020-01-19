import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JFrame {

  private static class MenuContent extends JPanel {
    private static final Image BG = genBackground();
    public MenuContent() {
      setPreferredSize(new Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT));
      setLayout(null);

      JButton play = new JButton("PLAY");
      play.setBounds(185, 200, 140, 80);
      play.setFont(new Font("Comic Sans MS", Font.PLAIN, 32));
      play.addActionListener((ev) -> {
        Thread th = new Thread(() -> {
          Game.reset();
          Game.game().start();
        });
        th.start();
      });
      add(play);

      JButton instructions = new JButton("Instructions");
      instructions.setBounds(335, 200, 140, 80);
      instructions.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
      add(instructions);

      JButton options = new JButton("Options");
      options.setBounds(485, 200, 140, 80);
      options.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
      add(options);

      JButton scores = new JButton("Highscores");
      scores.setBounds(635, 200, 140, 80);
      scores.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
      add(scores);
    }
    private static Image genBackground() {
      try {
        return ImageIO.read(new File("./res/main_menu_bg.png"));
      }
      catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(BG, 0, 0, null);
    }
  }

  public MainMenu() {

    setTitle("Veggietales 2");
    setResizable(false);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    getContentPane().add(new MenuContent());
    pack();
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
    setVisible(true);

  }

}
