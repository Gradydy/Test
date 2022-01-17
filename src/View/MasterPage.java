package View;

import Model.User;
import Utility.Connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;

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
    private JTable TableSales;

    private JTextField textField9;
    private JTextField textField10;
    private JTextField textField11;
    private JTextField textField12;
    private JPanel DistributorTab;
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

    private JPanel SalesTab;
    private JPanel AdminTab;
    private JPanel ItemTab;
    private JTextField AdminPhonetxt;
    private JComboBox AdminRolecb;

    public MasterPage(JFrame parent) {
        super(parent);
        this.setTitle("Master Page");
        this.setContentPane(main);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);


        Connect con = Connect.getConnection();

        // Admin button listener
        AdminInsertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String in = "INSERT INTO MsUser" +
                        "(AuditedUserID, RoleID, Name, Phone, Username, Password) " +
                        "VALUES(?, (SELECT RoleID FROM MsRole WHERE RoleName LIKE ?), ?, ?, ?, ?)";
        		PreparedStatement ps = con.preparedStatement(in);
        		try {
        			ps.setInt(1, userSession.UserID);
        			ps.setString(2, AdminRolecb.getSelectedItem().toString());
                    ps.setString(3, AdminNametxt.getText());
                    ps.setString(4, AdminPhonetxt.getText());
                    ps.setString(5, AdminUsernametxt.getText());
                    ps.setString(6, AdminPasswordtxt.getText());
        			ps.executeUpdate();
        		} catch (Exception e) {
        			// TODO: handle exception
        			e.printStackTrace();
        		}
            }
        });

        this.setVisible(true);
    }
}
