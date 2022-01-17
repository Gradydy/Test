package Utility;

import Model.User;
import View.MasterPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBarTemplate {
    public MenuBarTemplate(JMenuBar MenuBar) {
        JMenu homeMenu = new JMenu("Home");

        JMenuItem homeMenuItem = new JMenuItem("Home");
        JMenuItem masterMenuItem = new JMenuItem("Master");
        JMenuItem transactionMenuItem = new JMenuItem("Transaction");
        JMenuItem reportMenuItem = new JMenuItem("Report");
        JMenuItem logoutMenuItem = new JMenuItem("Log Out");

        masterMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MasterPage(null);
            }
        });

        homeMenu.add(homeMenuItem);
        homeMenu.add(masterMenuItem);
        homeMenu.add(transactionMenuItem);
        homeMenu.add(reportMenuItem);
        homeMenu.add(logoutMenuItem);
        MenuBar.add(homeMenu);
    }
}
