package Model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class DistributorTable extends AbstractTableModel {
    private final String[] COLUMNS = {"Representative Name", "Company", "Email", "Phone Number","Description"};
    private ArrayList<Distributor> distributor;

    public DistributorTable(ArrayList<Distributor> distributor) {
        this.distributor = distributor;
    }

    @Override
    public int getRowCount() {
        return distributor.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return distributor.get(rowIndex).Representative;
            case 1:
                return distributor.get(rowIndex).Company;
            case 2:
                return distributor.get(rowIndex).Email;
            case 3:
                return distributor.get(rowIndex).Phone;
            case 4:
                return distributor.get(rowIndex).Description;
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