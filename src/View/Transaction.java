package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Transaction extends JDialog {
    private JTabbedPane tab;
    private JTextField PurchasingItemNametxt;
    private JTextField PurchasingDistributortxt;
    private JTextField PurchasingTransactionIdtxt;
    private JButton PurchasingSearchButton;
    private JTextField PurchasingDate2txt;
    private JTextField PurchasingDatetxt;
    private JTextField SalesItemNametxt;
    private JTextField SalesCostumerNametxt;
    private JTextField SalesTransactionIdtxt;
    private JTextField SalesDatetxt;
    private JTextField SalesDate2txt;
    private JButton SalesSearchButton;
    private JTextField OrderCostumerNametxt;
    private JTextField OrderCostumerPhoneNumbertxt;
    private JTextField OrderItemTypetxt;
    private JTextField OrderQuantitytxt;
    private JTextField OrderPaymentTypetxt;
    private JButton OrderCreateButton;
    private JPanel main;
    private JPanel TransactionTab;
    private JPanel PurchasingTab;
    private JPanel SalesTab;
    private JPanel OrderTab;
    private JTable Purchasingtbl;
    private JTable Salestbl;
    private JTable Ordertbl;

    public Transaction(JFrame parent){
        super(parent);
        this.setTitle("Master Page");
        this.setContentPane(main);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Set JFrame at the middle of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, dim.height);
        this.setVisible(true);

        PurchasingSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        SalesSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        OrderCreateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        new Transaction(null);
    }
}
