import javax.swing.*;

public class testGUI extends JFrame {
    private JPanel main;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton logInButton;

    public testGUI(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(main);
        this.pack();
        this.setSize(800,800);
    }

    public static void main(String[] args) {
        JFrame frame = new testGUI("kontol");
        frame.setVisible(true);
    }
}
