package View;

import Model.*;
import Utility.Connect;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private JTextField DistributorRepresentativetxt;
    private JTextField DistributorPhonetxt;
    private JTextField DistributorEmailtxt;
    private JTextField DistributorCompanytxt;
    private JTextField DistributorDescriptiontxt;
    private JButton DistributorInsertBtn;
    private JButton DistributorUpdateBtn;
    private JButton DistributorDeleteBtn;
    private JTable DistributorTbl;

    private JPanel ItemTab;
    private JTextField ItemNametxt;
    private JTextField ItemQuantitytxt;
    private JTextField ItemTypetxt;
    private JTextField ItemPricetxt;
    private JTextField ItemDescriptiontxt;
    private JButton ItemInsertBtn;
    private JButton ItemUpdateBtn;
    private JButton ItemDeleteBtn;
    private JTable ItemTbl;
    private JButton AdminSearchBtn;
    private JButton DistributorBtn;
    private JButton ItemSearchBtn;

    // Variable buatan
    Connect con = Connect.getConnection();
    ArrayList<User> users = new ArrayList<User>();
    ArrayList<Distributor> distributors = new ArrayList<Distributor>();
    ArrayList<Item> items = new ArrayList<Item>();
    private int selectedIndex;
    private User selectedUser;
    private Distributor selectedDistributor;
    private Item selectedItem;

    public MasterPage(JFrame parent) {
        super(parent);
        this.setTitle("Master Page");
        this.setContentPane(main);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - dim.width/4, dim.height/2 - dim.height/4);
        this.setSize(dim.width/2, dim.height/2);

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

        // Setup Item Tab
        getAllItem();
        clearItemField();
        ItemTable itemTableModel = new ItemTable(items);
        ItemTbl.setModel(itemTableModel);
        ItemTbl.setAutoCreateRowSorter(true);

        // Initialize Button listener
        adminBtnListener(adminTableModel);
        distributorBtnListener(distributorTableModel);
        itemBtnListener(itemTableModel);

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

        // Search
        AdminSearchBtn.addActionListener(ae -> {
            users.removeAll(users);
            String query = "SELECT MsUser.UserID, Name, RoleName, Phone, Username, Password "  +
                    "FROM MsUser " +
                    "JOIN MsRole ON MsUser.RoleID = MsRole.RoleID " +
                    "WHERE Name LIKE COALESCE(?, Name) AND Username LIKE COALESCE(?, Username)";
            PreparedStatement ps = con.preparedStatement(query);
            ResultSet rs = null;
            try {
                if(AdminNametxt.getText().isEmpty())
                    ps.setNull(1, java.sql.Types.INTEGER);
                else
                    ps.setString(1 , "%" + AdminNametxt.getText() + "%");

                if(AdminUsernametxt.getText().isEmpty())
                    ps.setNull(2, java.sql.Types.INTEGER);
                else
                    ps.setString(2 , "%" + AdminUsernametxt.getText() + "%");

                rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                boolean flag = false;
                while(rs.next()) {
                    User userTemp = new User();
                    userTemp.UserID = rs.getInt(1);
                    userTemp.Name = rs.getString(2);
                    userTemp.RoleName = rs.getString(3);
                    userTemp.Phone = rs.getString(4);
                    userTemp.Username = rs.getString(5);
                    userTemp.Password = rs.getString(6);
                    users.add(userTemp);
                    flag = true;
                }
                if(flag)
                    adminTableModelTemp.fireTableDataChanged();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            clearAdminField();

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
        String query = "SELECT MsUser.UserID, Name, RoleName, Phone, Username, Password " +
                "FROM MsUser " +
                "JOIN MsRole ON MsUser.RoleID = MsRole.RoleID";
        ResultSet rs = con.executeQuery(query);
        try {
            while(rs.next()) {
                User userTemp = new User();
                userTemp.UserID = rs.getInt(1);
                userTemp.Name = rs.getString(2);
                userTemp.RoleName = rs.getString(3);
                userTemp.Phone = rs.getString(4);
                userTemp.Username = rs.getString(5);
                userTemp.Password = rs.getString(6);
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
            String query = "INSERT INTO MsDistributor " +
                    "(AuditedUserID, Representative, Company, Email, Phone, Description) " +
                    "VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.preparedStatement(query);
            try {
                ps.setInt(1, userSession.UserID);
                ps.setString(2, DistributorRepresentativetxt.getText());
                ps.setString(3, DistributorCompanytxt.getText());
                ps.setString(4, DistributorEmailtxt.getText());
                ps.setString(5, DistributorPhonetxt.getText());
                ps.setString(6, DistributorDescriptiontxt.getText());
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

        // Search distributor
        DistributorBtn.addActionListener(ae -> {
            distributors.removeAll(distributors);

            String query = "SELECT DistributorID, Representative, Company, Email, Phone, Description " +
                    "FROM MsDistributor " +
                    "WHERE Representative LIKE COALESCE(?, Representative) AND Company LIKE COALESCE(?, Company)";
            PreparedStatement ps = con.preparedStatement(query);
            ResultSet rs = null;
            try {
                if(DistributorRepresentativetxt.getText().isEmpty())
                    ps.setNull(1, java.sql.Types.INTEGER);
                else
                    ps.setString(1 , "%" + DistributorRepresentativetxt.getText() + "%");

                if(DistributorCompanytxt.getText().isEmpty())
                    ps.setNull(2, java.sql.Types.INTEGER);
                else
                    ps.setString(2 , "%" + DistributorCompanytxt.getText() + "%");

                rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                boolean flag = false;
                while(rs.next()) {
                    Distributor distribtorTemp = new Distributor();
                    distribtorTemp.DistributorID = rs.getInt(1);
                    distribtorTemp.Representative = rs.getString(2);
                    distribtorTemp.Company = rs.getString(3);
                    distribtorTemp.Email = rs.getString(4);
                    distribtorTemp.Phone = rs.getString(5);
                    distribtorTemp.Description = rs.getString(6);
                    distributors.add(distribtorTemp);
                    flag = true;
                }
                if(flag)
                    distributorTableModelTemp.fireTableDataChanged();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            clearAdminField();

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
                ps.setString(2, DistributorRepresentativetxt.getText());
                ps.setString(3, DistributorCompanytxt.getText());
                ps.setString(4, DistributorEmailtxt.getText());
                ps.setString(5, DistributorPhonetxt.getText());
                ps.setString(6, DistributorDescriptiontxt.getText());
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
                    DistributorRepresentativetxt.setText(selectedDistributor.Representative);
                    DistributorCompanytxt.setText(selectedDistributor.Company);
                    DistributorPhonetxt.setText(selectedDistributor.Phone);
                    DistributorEmailtxt.setText(selectedDistributor.Email);
                    DistributorDescriptiontxt.setText(selectedDistributor.Description);
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
        if(DistributorEmailtxt.getText().isEmpty() ||
                DistributorPhonetxt.getText().isEmpty() || DistributorRepresentativetxt.getText().isEmpty() ||
                DistributorCompanytxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(MasterPage.this,
                    "Please fill all field",
                    "Ok",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearDistributorField() {
        DistributorRepresentativetxt.setText("");
        DistributorCompanytxt.setText("");
        DistributorEmailtxt.setText("");
        DistributorPhonetxt.setText("");
        DistributorDescriptiontxt.setText("");
        DistributorTbl.clearSelection();
        DistributorInsertBtn.setEnabled(true);
        DistributorDeleteBtn.setEnabled(false);
        DistributorUpdateBtn.setEnabled(false);
        selectedDistributor = null;
        selectedIndex = -1;
    }
    //</editor-fold>

    //<editor-fold desc="Item">
    private void itemBtnListener(ItemTable itemTableModelTemp) {
        // Insert
        ItemInsertBtn.addActionListener(ae -> {
            if(validateInputItem() == false) return;
            String query = "INSERT INTO MsItem" +
                    "(AuditedUserID, ItemName, ItemType, Quantity, Price, Description) " +
                    "VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.preparedStatement(query);
            try {
                ps.setInt(1, userSession.UserID);
                ps.setString(2, ItemNametxt.getText());
                ps.setString(3, ItemTypetxt.getText());
                ps.setString(4, ItemQuantitytxt.getText());
                ps.setString(5, ItemPricetxt.getText());
                ps.setString(6, ItemDescriptiontxt.getText());
                if(ps.executeUpdate() > 0) {
                    clearItemField();
                    getAllItem();
                    itemTableModelTemp.fireTableDataChanged();
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        });

        // Search Item
        ItemSearchBtn.addActionListener(ae -> {
            items.removeAll(items);

            String query = "SELECT ItemID, ItemName, ItemType, Quantity, Price, Description " +
                    "FROM MsItem " +
                    "WHERE ItemName LIKE COALESCE(?, ItemName) AND ItemType LIKE COALESCE(?, ItemType)";
            PreparedStatement ps = con.preparedStatement(query);
            ResultSet rs = null;
            try {
                if(ItemNametxt.getText().isEmpty())
                    ps.setNull(1, java.sql.Types.INTEGER);
                else
                    ps.setString(1 , "%" + ItemNametxt.getText() + "%");

                if(ItemTypetxt.getText().isEmpty())
                    ps.setNull(2, java.sql.Types.INTEGER);
                else
                    ps.setString(2 , "%" + ItemTypetxt.getText() + "%");

                rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                boolean flag = false;
                while(rs.next()) {
                    Item itemTemp = new Item();
                    itemTemp.ItemID = rs.getInt(1);
                    itemTemp.ItemName = rs.getString(2);
                    itemTemp.ItemType = rs.getString(3);
                    itemTemp.Quantity = rs.getInt(4);
                    itemTemp.Price = rs.getInt(5);
                    itemTemp.Description = rs.getString(6);
                    items.add(itemTemp);
                    flag = true;
                }
                if(flag)
                    itemTableModelTemp.fireTableDataChanged();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            clearAdminField();
            clearAdminField();

        });

        // Update item
        ItemUpdateBtn.addActionListener(ae -> {
            if(validateInputItem() == false) return;
            String query = "UPDATE MsItem " +
                    "SET AuditedUserID = ?, ItemName = ?, ItemType = ?, Quantity = ?, Price = ?, Description = ? " +
                    "WHERE ItemID = ?";
            PreparedStatement ps = con.preparedStatement(query);
            try {
                ps.setInt(1, userSession.UserID);
                ps.setString(2, ItemNametxt.getText());
                ps.setString(3, ItemTypetxt.getText());
                ps.setString(4, ItemQuantitytxt.getText());
                ps.setString(5, ItemPricetxt.getText());
                ps.setString(6, ItemDescriptiontxt.getText());
                ps.setInt(7, selectedItem.ItemID);
                if(ps.executeUpdate() > 0) {
                    clearItemField();
                    getAllItem();
                    itemTableModelTemp.fireTableDataChanged();
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        });

        // Delete item
        ItemDeleteBtn.addActionListener(ae -> {
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure want to delete this item?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.NO_OPTION) {
                return;
            }
            String query = "DELETE FROM MsItem WHERE ItemID = ?";
            PreparedStatement ps = con.preparedStatement(query);
            try {
                ps.setInt(1, selectedItem.ItemID);
                if(ps.executeUpdate() > 0) {
                    clearItemField();
                    getAllItem();
                    itemTableModelTemp.fireTableDataChanged();
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        });

        // Select row
        ItemTbl.getSelectionModel().addListSelectionListener(e -> {
            if(!ItemTbl.getSelectionModel().isSelectionEmpty()) {
                selectedIndex = ItemTbl.convertRowIndexToModel(ItemTbl.getSelectedRow());
                selectedItem = items.get(selectedIndex);
                if(selectedItem != null) {
                    ItemNametxt.setText(selectedItem.ItemName);
                    ItemTypetxt.setText(selectedItem.ItemType);
                    ItemQuantitytxt.setText(String.valueOf(selectedItem.Quantity));
                    ItemPricetxt.setText(String.valueOf(selectedItem.Price));
                    ItemDescriptiontxt.setText(selectedItem.Description);
                    ItemInsertBtn.setEnabled(false);
                    ItemDeleteBtn.setEnabled(true);
                    ItemUpdateBtn.setEnabled(true);
                }
            }
        });
    }

    private void getAllItem() {
        items.removeAll(items);
        String query = "SELECT ItemID, ItemName, ItemType, Quantity, Price, Description " +
                "FROM MsItem";
        ResultSet rs = con.executeQuery(query);
        try {
            while(rs.next()) {
                Item item = new Item();
                item.ItemID = rs.getInt(1);
                item.ItemName = rs.getString(2);
                item.ItemType = rs.getString(3);
                item.Quantity = rs.getInt(4);
                item.Price = rs.getInt(5);
                item.Description = rs.getString(6);
                items.add(item);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private boolean validateInputItem() {
        if(ItemNametxt.getText().isEmpty() ||
                ItemTypetxt.getText().isEmpty() || ItemQuantitytxt.getText().isEmpty() ||
                ItemPricetxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(MasterPage.this,
                    "Please fill all field",
                    "Ok",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearItemField() {
        ItemNametxt.setText("");
        ItemTypetxt.setText("");
        ItemQuantitytxt.setText("");
        ItemPricetxt.setText("");
        ItemDescriptiontxt.setText("");
        ItemTbl.clearSelection();
        ItemInsertBtn.setEnabled(true);
        ItemDeleteBtn.setEnabled(false);
        ItemUpdateBtn.setEnabled(false);
        selectedItem = null;
        selectedIndex = -1;
    }
    //</editor-fold>
}
