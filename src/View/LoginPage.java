package View;

import Model.User;
import Utility.Connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static Utility.UserSession.userSession;

public class LoginPage extends JDialog {
    private JPanel main;
    private JTextField Usernametxt;
    private JPasswordField Passwordtxt;
    private JButton logInButton;

    Connect conn = Connect.getConnection();

    public LoginPage(JFrame parent) {
        // JFrame setting initialization
        super(parent);
        this.setTitle("Login");
        this.setContentPane(main);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Usernametxt.setText("SuperAdmin");
        Passwordtxt.setText("sa123");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);


        // Button listener
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = Usernametxt.getText();
                String password = String.valueOf(Passwordtxt.getPassword());
                userSession = new User();
                getAuthenticatedUser(username, password);

                if(userSession.UserID != 0) {
                    dispose();
                    new HomePage(null);
                }
                if(userSession.UserID == 0) {
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
    private void getAuthenticatedUser(String username, String password) {
        ResultSet  rs = conn.validate("SELECT * FROM msuser");

        try {
            while (rs.next()){
                if(username.equals(rs.getString("Username"))){
                    if(password.equals(rs.getString("Password"))){
                        userSession.UserID = rs.getInt("UserID");
                        userSession.RoleID = rs.getInt("RoleID");
                        userSession.Name = rs.getString("Name");
                        userSession.Username = rs.getString("Username");
                        userSession.Password = rs.getString("Password");
                    }
                }
            }

            rs.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    public static void main(String[] args) {
        LoginPage loginPage = new LoginPage(null);
    }
}
