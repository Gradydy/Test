import javax.swing.*;
import java.awt.*;

public class HomePage extends JDialog {
    private JPanel contentpane;

    public HomePage(JFrame parent) {
        super (parent);
        add(contentpane);
        this.setTitle("Home");
        this.setModal(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);


        setVisible(true);

    }

    public static void main(String[] args) {
        new HomePage(null);
    }
}
