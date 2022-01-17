package View;

import Model.User;
import Utility.MenuBarTemplate;

import javax.swing.*;
import java.awt.*;

import static Utility.UserSession.userSession;

public class HomePage extends JDialog {
    private JPanel contentpane;
    private JMenuBar MenuBar;
    private JLabel UserIDtxt;

    public HomePage(JFrame parent) {
        // JFrame setting initialization
        super(parent);
        this.setTitle("Home");
        this.setContentPane(contentpane);
        this.setModal(true);
        this.setResizable(false);
        this.setJMenuBar(MenuBar);
        new MenuBarTemplate(MenuBar);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);



        UserIDtxt.setText(String.valueOf(userSession.Username));

        this.setVisible(true);
    }
}
