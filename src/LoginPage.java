import Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPage extends JDialog {
    private JPanel main;
    private JTextField Usernametxt;
    private JPasswordField Passwordtxt;
    private JButton logInButton;
    public User user;

    Connect conn = Connect.getConnection();

    public LoginPage(JFrame parent) {
        // JFrame setting initialization
        super(parent);
        this.setTitle("Login");
        this.setContentPane(main);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Set JFrame at the middle of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
       this.setSize(dim.width, dim.height);

        // Button listener
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = Usernametxt.getText();
                String password = String.valueOf(Passwordtxt.getPassword());

                user = getAuthenticatedUser(username, password);

                if(user.UserID != 0) dispose();
                if(user.UserID == 0) {
                    JOptionPane.showMessageDialog(LoginPage.this,
                            "Invalid email or password",
                            "Try Again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.setVisible(true);
    }

    // Method to get data from database
    private User getAuthenticatedUser(String username, String password) {
        User userTemp = new User();
        ResultSet  rs = conn.validate("SELECT * FROM msuser");

        try {
            while (rs.next()){
                if(username.equals(rs.getString("Username"))){
                    if(password.equals(rs.getString("Password"))){
                        this.dispose();
                        App mast = new App(null);
                        userTemp.UserID = rs.getInt("UserID");
                        userTemp.Username = rs.getString("Username");
                        userTemp.Password = rs.getString("Password");
                        userTemp.Role = rs.getString("Role");
                    }
                }
            }

            rs.close();
            return userTemp;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userTemp;
    }

    public static void main(String[] args) {
        LoginPage loginPage = new LoginPage(null);
        User userTest = loginPage.user;
    }
}
