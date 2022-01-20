package View;

import Model.AdminTable;
import Model.User;
import Utility.Connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static Utility.UserSession.userSession;

public class MasterPage extends JDialog {

    private JTabbedPane tab;
    private JPanel main;

    // Tab admin
    private JTextField AdminNametxt;
    private JTextField AdminUsernametxt;
    private JTextField AdminPasswordtxt;
    private JTextField AdminPassword2txt;
    private JButton AdminInsertBtn;
    private JButton AdminUpdateBtn;
    private JButton AdminDeleteBtn;
    private JTable AdminTbl;

    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JButton insertButton1;

    private JTextField textField9;
    private JTextField textField10;
    private JTextField textField11;
    private JTextField textField12;
    private JPanel DistributorTab;
    private JTextField textField13;
    private JButton insertButton2;
    private JTextField textField14;
    private JTextField textField15;
    private JTextField textField16;
    private JTextField textField17;
    private JTextField textField18;
    private JButton insertButton3;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton updateButton1;
    private JButton deleteButton1;
    private JButton updateButton2;
    private JButton deleteButton2;

    private JPanel SalesTab;
    private JPanel AdminTab;
    private JPanel ItemTab;
    private JTextField AdminPhonetxt;
    private JComboBox AdminRolecb;
    private JTable SalesTbl;
    private JTable DistibutorTbl;
    private JTable ItemTbl;

    // Variable buatan
    Connect con = Connect.getConnection();
    ArrayList<User> users = new ArrayList<User>();

    public MasterPage(JFrame parent) {
        super(parent);
        this.setTitle("Master Page");
        this.setContentPane(main);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);

        // Get semua user yang ada di database
        getAllUser();

        // Setup JTable
        AdminTable adminTableModel = new AdminTable(users);
        AdminTbl.setModel(adminTableModel);
        AdminTbl.setAutoCreateRowSorter(true);

        // Initialize Button listener
        adminBtnListener(adminTableModel);

        this.setVisible(true);
    }

    public void adminBtnListener(AdminTable adminTableModelTemp) {
        // Insert
        AdminInsertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String query = "INSERT INTO MsUser" +
                        "(AuditedUserID, RoleID, Name, Phone, Username, Password) " +
                        "VALUES(?, (SELECT RoleID FROM MsRole WHERE RoleName LIKE ?), ?, ?, ?, ?)";
                PreparedStatement ps = con.preparedStatement(query);
                try {
                    ps.setInt(1, userSession.UserID);
                    ps.setString(2, AdminRolecb.getSelectedItem().toString());
                    ps.setString(3, AdminNametxt.getText());
                    ps.setString(4, AdminPhonetxt.getText());
                    ps.setString(5, AdminUsernametxt.getText());
                    ps.setString(6, AdminPasswordtxt.getText());
                    if(ps.executeUpdate() > 0) {
                        getAllUser();
                        adminTableModelTemp.fireTableDataChanged();
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }

    public void getAllUser() {
        String query = "SELECT MsUser.UserID, Name, RoleName, Name, Phone, Username " +
                "FROM MsUser " +
                "JOIN MsRole ON MsUser.RoleID = MsRole.RoleID";
        ResultSet rs = con.executeQuery(query);
        try {
            while(rs.next()) {
                User userTemp = new User();
                userTemp.UserID = rs.getInt(1);
                userTemp.Name = rs.getString(2);
                userTemp.RoleName = rs.getString(3);
                userTemp.Name = rs.getString(4);
                userTemp.Phone = rs.getString(5);
                userTemp.Username = rs.getString(6);
                users.add(userTemp);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
