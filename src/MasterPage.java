import javax.swing.*;
import java.awt.*;

public class MasterPage extends JDialog {

    private JTabbedPane tab;
    private JPanel main;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton insertButton;
    private JTable TableAdmin;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JButton insertButton1;
    private JTable TableSales;
    private JTextField textField9;
    private JTextField textField10;
    private JTextField textField11;
    private JTextField textField12;
    private JPanel Description;
    private JTextField textField13;
    private JButton insertButton2;
    private JTable TableDistributor;
    private JTextField textField14;
    private JTextField textField15;
    private JTextField textField16;
    private JTextField textField17;
    private JTextField textField18;
    private JButton insertButton3;
    private JTable TableItem;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton updateButton1;
    private JButton deleteButton1;
    private JButton updateButton2;
    private JButton deleteButton2;
    private JButton updateButton3;
    private JButton deleteButton3;

    public MasterPage(JFrame parent){
        super(parent);
        this.setTitle("Master Page");
        this.setContentPane(main);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Set JFrame at the middle of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        MasterPage MasterPage = new MasterPage(null);
    }
}
