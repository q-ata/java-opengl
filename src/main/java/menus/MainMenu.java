package menus;

import constants.GameConfig;
import constants.Logger;
import game.Game;
import structs.Menu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JFrame implements Menu {

  /**
   * The content of the main menu.
   */
  private static class MenuContent extends JPanel {
    private static final Image BG = genBackground();
    public MenuContent() {
      setPreferredSize(new Dimension(GameConfig.MENU_WIDTH, GameConfig.MENU_HEIGHT));
      setLayout(null);

      // Play button to start the game.
      JButton play = new JButton("PLAY");
      play.setBounds(185, 200, 140, 80);
      play.setFont(new Font("Comic Sans MS", Font.PLAIN, 32));
      play.addActionListener((ev) -> {
        // Do not block the button callback.
        new Thread(() -> {
          Game.reset();
          Game.game().start();
          // Show score after game.
          new ScoreDisplay(Game.game().getScore()).start();
        }).start();
      });
      add(play);

      // Instructions page.
      JButton instructions = new JButton("Instructions");
      instructions.setBounds(335, 200, 140, 80);
      instructions.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
      instructions.addActionListener((ev) -> {
        Instructions ins = new Instructions();
        ins.start();
      });
      add(instructions);

      // Options page.
      JButton options = new JButton("Options");
      options.setBounds(485, 200, 140, 80);
      options.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
      options.addActionListener((ev) -> {
        if (Desktop.isDesktopSupported()) {
          try {
            Desktop.getDesktop().edit(new File("./res/options.txt"));
          }
          catch (IOException e) {
            e.printStackTrace();
          }
        }
        else {
          Logger.error(getClass(), "Desktop API not supported.");
        }
      });
      add(options);

      // High scores page.
      JButton scores = new JButton("Highscores");
      scores.setBounds(635, 200, 140, 80);
      scores.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
      scores.addActionListener((ev) -> {
        Highscores hs = new Highscores();
        hs.start();
      });
      add(scores);

    }
    // Create the main menu background.
    private static Image genBackground() {
      try {
        return ImageIO.read(new File("./res/main_menu_bg.png"));
      }
      catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }
    // Draw background image, adapted from https://stackoverflow.com/questions/19125707/
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(BG, 0, 0, null);
    }
  }

  @Override
  public void start() {
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
