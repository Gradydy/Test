import javax.swing.*;
import java.awt.*;
<<<<<<< HEAD

public class HomePage extends JDialog {
    private JPanel contentpane;
=======
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HomePage extends JDialog {
    private JPanel contentpane;
    private JLabel Transaction;
>>>>>>> f33d075a27334c8cdb228384a5b95cf8c61dee91

    public HomePage(JFrame parent) {
        super (parent);
        add(contentpane);
        this.setTitle("Home");
        this.setModal(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);

<<<<<<< HEAD

=======
>>>>>>> f33d075a27334c8cdb228384a5b95cf8c61dee91
        setVisible(true);

    }

    public static void main(String[] args) {
        new HomePage(null);
    }
}
