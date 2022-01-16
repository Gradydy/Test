import javax.swing.*;
import java.awt.*;

public class App extends JDialog {
    private JPanel contentpane;
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JTabbedPane tabbedPane3;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton insertButton;
    private JTable table1;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JPasswordField passwordField1;
    private JButton insertButton1;
    private JTable table2;

    public App(JFrame parent) {
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
        new App(null);
    }
}
