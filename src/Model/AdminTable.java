package Model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class AdminTable extends AbstractTableModel {
    private final String[] COLUMNS = {"Name", "Role", "Phone", "Username"};
    private ArrayList<User> users;

    public AdminTable(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return users.get(rowIndex).Name;
            case 1:
                return users.get(rowIndex).RoleName;
            case 2:
                return users.get(rowIndex).Phone;
            case 3:
                return users.get(rowIndex).Username;
            default:
                return "-";
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(getValueAt(0, columnIndex) != null) {
            return getValueAt(0, columnIndex).getClass();
        } else {
            return Object.class;
        }
    }
}
