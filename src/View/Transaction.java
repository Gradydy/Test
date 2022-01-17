package View;

import javax.swing.*;
import java.awt.*;

public class Transaction extends JDialog {
    private JTabbedPane Transaction;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton searchButton;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;
    private JTextField textField10;
    private JButton searchButton1;
    private JTextField textField11;
    private JTextField textField12;
    private JTextField textField13;
    private JTextField textField14;
    private JTextField textField15;
    private JButton createButton;
    private JPanel pane;

    public Transaction(JFrame parent){
        super(parent);
        this.setTitle("Master Page");
        this.setContentPane(pane);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Set JFrame at the middle of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);
        this.setVisible(true);

    }

    public static void main(String[] args) {
        new Transaction(null);
    }
}
