package menus;

import structs.Menu;

import javax.swing.*;
import java.awt.*;

/**
 * game.Game instructions menu.
 */
public class Instructions extends JFrame implements Menu {

  @Override
  public void start() {

    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(600, 400));
    panel.setLayout(null);
    JLabel info = new JLabel("<html>How to play VeggieTales 2<br>" +
            "Cody Cabbage, the noble knight of the vegetable kingdom<br>" +
            "has been surrounded on all sides by the evil fruit empire.<br>" +
            "Help him fend off endless hoards of fruits and survive as long as possible.<br>" +
            "Use WASD to move around.<br>" +
            "Use your mouse to look around you.<br>" +
            "Hold shift to sprint and move faster - watch your sprint meter.<br>" +
            "Use LEFT MOUSE to fire a grape shot - hit the fruits spawning around you.<br>" +
            "Watch your health bar - don't get hit by the fruits.<br>" +
            "Defeat as many fruits as you can - the game gets harder as time goes on.");
    info.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
    info.setBounds(60, -80, 480, 550);
    panel.add(info);

    setTitle("Instructions");

    getContentPane().add(panel);
    pack();
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
    setVisible(true);

  }

}
