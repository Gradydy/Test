package View;

import Model.AdminTable;
import Model.User;
import Utility.Connect;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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

    private JPanel AdminTab;
    private JPanel ItemTab;
    private JTextField AdminPhonetxt;
    private JComboBox AdminRolecb;
    private JTable DistibutorTbl;
    private JTable ItemTbl;

    // Variable buatan
    Connect con = Connect.getConnection();
    ArrayList<User> users = new ArrayList<User>();
    private int selectedIndex;
    private User selectedUser;


    public MasterPage(JFrame parent) {
        super(parent);
        this.setTitle("Master Page");
        this.setContentPane(main);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);

        // Setup Admin Tab
        getAllUser();
        DefaultComboBoxModel<String> roleComboModel =
                new DefaultComboBoxModel<>(getAllRole().toArray(new String[0]));
        AdminRolecb.setModel(roleComboModel);
        AdminUpdateBtn.setEnabled(false);
        AdminTable adminTableModel = new AdminTable(users);
        AdminTbl.setModel(adminTableModel);
        AdminTbl.setAutoCreateRowSorter(true);

        // Initialize Button listener
        adminBtnListener(adminTableModel);

        this.setVisible(true);
    }

    // Method to list admin btn listener
    private void adminBtnListener(AdminTable adminTableModelTemp) {
        // Insert
        AdminInsertBtn.addActionListener(ae -> {
            if(validateInputAdmin() == false) return;
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
                    clearAdminField();
                    getAllUser();
                    adminTableModelTemp.fireTableDataChanged();
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        });

        // Update item
        AdminUpdateBtn.addActionListener(ae -> {
            if(validateInputAdmin() == false) return;
            String query = "UPDATE MsUser " +
                    "SET AuditedUserID = ?, RoleID = (SELECT RoleID FROM MsRole WHERE RoleName LIKE ?), Name = ?, Phone = ?, Username = ?, Password = ? " +
                    "WHERE UserID = ?";
            PreparedStatement ps = con.preparedStatement(query);
            try {
                ps.setInt(1, userSession.UserID);
                ps.setString(2, AdminRolecb.getSelectedItem().toString());
                ps.setString(3, AdminNametxt.getText());
                ps.setString(4, AdminPhonetxt.getText());
                ps.setString(5, AdminUsernametxt.getText());
                ps.setString(6, AdminPasswordtxt.getText());
                ps.setInt(7, selectedUser.UserID);
                if(ps.executeUpdate() > 0) {
                    clearAdminField();
                    getAllUser();
                    adminTableModelTemp.fireTableDataChanged();
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        });

        // Select row
        AdminTbl.getSelectionModel().addListSelectionListener(e -> {
            if(!AdminTbl.getSelectionModel().isSelectionEmpty()) {
                selectedIndex = AdminTbl.convertRowIndexToModel(AdminTbl.getSelectedRow());
                selectedUser = users.get(selectedIndex);
                if(selectedUser != null) {
                    System.out.println(selectedUser.Password);
                    AdminNametxt.setText(selectedUser.Name);
                    AdminPhonetxt.setText(selectedUser.Name);
                    AdminUsernametxt.setText(selectedUser.Username);
                    AdminPasswordtxt.setText(selectedUser.Password);
                    AdminPassword2txt.setText(selectedUser.Password);
                    AdminRolecb.setSelectedItem(selectedUser.RoleName);
                    AdminInsertBtn.setEnabled(false);
                    AdminUpdateBtn.setEnabled(true);
                }
            }
        });
    }

    private void getAllUser() {
        users.removeAll(users);
        String query = "SELECT MsUser.UserID, Name, RoleName, Name, Phone, Username, Password " +
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
                userTemp.Password = rs.getString(7);
                users.add(userTemp);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private ArrayList<String> getAllRole() {
        String query = "SELECT RoleName FROM MsRole";
        ArrayList<String> roles = new ArrayList<String>();
        ResultSet rs = con.executeQuery(query);
        try {
            while(rs.next()) {
                if(rs.getString(1).equals("SuperAdmin")) continue;
                roles.add(rs.getString(1));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return roles;
    }

    private boolean validateInputAdmin() {
        if(AdminNametxt.getText().isEmpty() ||
                AdminPhonetxt.getText().isEmpty() || AdminUsernametxt.getText().isEmpty() ||
                AdminPasswordtxt.getText().isEmpty() || AdminPassword2txt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(MasterPage.this,
                    "Please fill all field",
                    "Ok",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if(!AdminPasswordtxt.getText().equals(AdminPassword2txt.getText())) {
            JOptionPane.showMessageDialog(MasterPage.this,
                    "Password didn't match",
                    "Ok",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearAdminField() {
        AdminNametxt.setText("");
        AdminPasswordtxt.setText("");
        AdminPassword2txt.setText("");
        AdminUsernametxt.setText("");
        AdminPhonetxt.setText("");
        AdminTbl.clearSelection();
        AdminInsertBtn.setEnabled(true);
        AdminUpdateBtn.setEnabled(false);
        selectedUser = null;
        selectedIndex = -1;
    }
}
