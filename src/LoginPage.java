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

    public LoginPage(JFrame parent) {
        // JFrame setting initialization
        super(parent);
        this.setTitle("Login");
        this.setContentPane(main);
        this.setModal(true);
        this.setSize(800,600);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Set JFrame at the middle of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - 800) / 2;
        int y = (dim.height - 600) / 2;
        this.setLocation(x, y);

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

        final String DB_URL = "jdbc:mysql://localhost/gui";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM MsUser WHERE Username=? AND Password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                userTemp.UserID = resultSet.getInt("UserID");
                userTemp.Username = resultSet.getString("Username");
                userTemp.Password = resultSet.getString("Password");
                userTemp.Role = resultSet.getString("Role");
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userTemp;
    }

    public static void main(String[] args) {
        LoginPage loginPage = new LoginPage(null);
        User userTest = loginPage.user;
        if(userTest.UserID != 0) {
            System.out.println(userTest.UserID);
            System.out.println(userTest.Username);
            System.out.println(userTest.Password);
            System.out.println(userTest.Role);
        }
    }
}
