package View;

import Model.AdminTable;
import Model.Distributor;
import Model.DistributorTable;
import Model.User;
import Utility.Connect;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static Utility.UserSession.userSession;

public class MasterPage extends JDialog {

    private JTabbedPane tab;
    private JPanel main;

    // Tab admin
    private JPanel AdminTab;
    private JTextField AdminNametxt;
    private JTextField AdminPhonetxt;
    private JComboBox AdminRolecb;
    private JTextField AdminUsernametxt;
    private JTextField AdminPasswordtxt;
    private JTextField AdminPassword2txt;
    private JButton AdminInsertBtn;
    private JButton AdminUpdateBtn;
    private JButton AdminDeleteBtn;
    private JTable AdminTbl;

    private JPanel DistributorTab;
    private JTextField DistributorRepresentative;
    private JTextField DistributorPhone;
    private JTextField DistributorEmail;
    private JTextField DistributorCompany;
    private JTextField DistributorDescription;
    private JButton DistributorInsertBtn;
    private JButton DistributorUpdateBtn;
    private JButton DistributorDeleteBtn;
    private JTable DistributorTbl;

    private JPanel ItemTab;
    private JTextField textField14;
    private JTextField textField15;
    private JTextField textField16;
    private JTextField textField17;
    private JTextField textField18;
    private JButton insertButton3;
    private JButton updateButton;
    private JButton deleteButton;



    private JTable ItemTbl;

    // Variable buatan
    Connect con = Connect.getConnection();
    ArrayList<User> users = new ArrayList<User>();
    ArrayList<Distributor> distributors = new ArrayList<Distributor>();
    private int selectedIndex;
    private User selectedUser;
    private Distributor selectedDistributor;


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
        clearAdminField();
        AdminTable adminTableModel = new AdminTable(users);
        AdminTbl.setModel(adminTableModel);
        AdminTbl.setAutoCreateRowSorter(true);

        // Setup Distributor Tab
        getAllDistributor();
        clearDistributorField();
        DistributorTable distributorTableModel = new DistributorTable(distributors);
        DistributorTbl.setModel(distributorTableModel);
        DistributorTbl.setAutoCreateRowSorter(true);

        // Initialize Button listener
        adminBtnListener(adminTableModel);
        distributorBtnListener(distributorTableModel);

        this.setVisible(true);
    }

    //<editor-fold desc="Admin">
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

        // Delete distributor
        AdminDeleteBtn.addActionListener(ae -> {
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure want to delete this user?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.NO_OPTION) {
                return;
            }
            String query = "DELETE FROM MsUser WHERE UserID = ?";
            PreparedStatement ps = con.preparedStatement(query);
            try {
                ps.setInt(1, selectedUser.UserID);
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
                    AdminNametxt.setText(selectedUser.Name);
                    AdminPhonetxt.setText(selectedUser.Phone);
                    AdminUsernametxt.setText(selectedUser.Username);
                    AdminPasswordtxt.setText(selectedUser.Password);
                    AdminPassword2txt.setText(selectedUser.Password);
                    AdminRolecb.setSelectedItem(selectedUser.RoleName);
                    AdminInsertBtn.setEnabled(false);
                    AdminDeleteBtn.setEnabled(true);
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
        AdminDeleteBtn.setEnabled(false);
        AdminUpdateBtn.setEnabled(false);
        selectedUser = null;
        selectedIndex = -1;
    }
    //</editor-fold>

    //<editor-fold desc="Distributor">
    private void distributorBtnListener(DistributorTable distributorTableModelTemp) {
        // Insert
        DistributorInsertBtn.addActionListener(ae -> {
            if(validateInputDistributor() == false) return;
            String query = "INSERT INTO MsDistributor" +
                    "(AuditedUserID, Representative, Company, Email, Phone, Description) " +
                    "VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.preparedStatement(query);
            try {
                ps.setInt(1, userSession.UserID);
                ps.setString(2, DistributorRepresentative.getText());
                ps.setString(3, DistributorCompany.getText());
                ps.setString(4, DistributorEmail.getText());
                ps.setString(5, DistributorPhone.getText());
                ps.setString(6, DistributorDescription.getText());
                if(ps.executeUpdate() > 0) {
                    clearDistributorField();
                    getAllDistributor();
                    distributorTableModelTemp.fireTableDataChanged();
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        });

        // Update distributor
        DistributorUpdateBtn.addActionListener(ae -> {
            if(validateInputDistributor() == false) return;
            String query = "UPDATE MsDistributor " +
                    "SET AuditedUserID = ?, Representative = ?, Company = ?, Email = ?, Phone = ?, Description = ? " +
                    "WHERE DistributorID = ?";
            PreparedStatement ps = con.preparedStatement(query);
            try {
                ps.setInt(1, userSession.UserID);
                ps.setString(2, DistributorRepresentative.getText());
                ps.setString(3, DistributorCompany.getText());
                ps.setString(4, DistributorEmail.getText());
                ps.setString(5, DistributorPhone.getText());
                ps.setString(6, DistributorDescription.getText());
                ps.setInt(7, selectedDistributor.DistributorID);
                if(ps.executeUpdate() > 0) {
                    clearDistributorField();
                    getAllDistributor();
                    distributorTableModelTemp.fireTableDataChanged();
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        });

        // Delete distributor
        DistributorDeleteBtn.addActionListener(ae -> {
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure want to delete this distributor?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.NO_OPTION) {
                return;
            }
            String query = "DELETE FROM MsDistributor WHERE DistributorID = ?";
            PreparedStatement ps = con.preparedStatement(query);
            try {
                ps.setInt(1, selectedDistributor.DistributorID);
                if(ps.executeUpdate() > 0) {
                    clearDistributorField();
                    getAllDistributor();
                    distributorTableModelTemp.fireTableDataChanged();
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        });

        // Select row
        DistributorTbl.getSelectionModel().addListSelectionListener(e -> {
            if(!DistributorTbl.getSelectionModel().isSelectionEmpty()) {
                selectedIndex = DistributorTbl.convertRowIndexToModel(DistributorTbl.getSelectedRow());
                selectedDistributor = distributors.get(selectedIndex);
                if(selectedDistributor != null) {
                    DistributorRepresentative.setText(selectedDistributor.Representative);
                    DistributorCompany.setText(selectedDistributor.Company);
                    DistributorPhone.setText(selectedDistributor.Phone);
                    DistributorEmail.setText(selectedDistributor.Email);
                    DistributorDescription.setText(selectedDistributor.Description);
                    DistributorInsertBtn.setEnabled(false);
                    DistributorDeleteBtn.setEnabled(true);
                    DistributorUpdateBtn.setEnabled(true);
                }
            }
        });
    }

    private void getAllDistributor() {
        distributors.removeAll(distributors);
        String query = "SELECT DistributorID, Representative, Company, Email, Phone, Description " +
                "FROM MsDistributor";
        ResultSet rs = con.executeQuery(query);
        try {
            while(rs.next()) {
                Distributor distributor = new Distributor();
                distributor.DistributorID = rs.getInt(1);
                distributor.Representative = rs.getString(2);
                distributor.Company = rs.getString(3);
                distributor.Email = rs.getString(4);
                distributor.Phone = rs.getString(5);
                distributor.Description = rs.getString(6);
                distributors.add(distributor);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private boolean validateInputDistributor() {
        if(DistributorEmail.getText().isEmpty() ||
                DistributorPhone.getText().isEmpty() || DistributorRepresentative.getText().isEmpty() ||
                DistributorCompany.getText().isEmpty()) {
            JOptionPane.showMessageDialog(MasterPage.this,
                    "Please fill all field",
                    "Ok",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearDistributorField() {
        DistributorRepresentative.setText("");
        DistributorCompany.setText("");
        DistributorEmail.setText("");
        DistributorPhone.setText("");
        DistributorDescription.setText("");
        DistributorTbl.clearSelection();
        DistributorInsertBtn.setEnabled(true);
        DistributorDeleteBtn.setEnabled(false);
        DistributorUpdateBtn.setEnabled(false);
        selectedDistributor = null;
        selectedIndex = -1;
    }
    //</editor-fold>
}
