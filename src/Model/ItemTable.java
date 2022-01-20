package Model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ItemTable extends AbstractTableModel {
    private final String[] COLUMNS = {"Item Name", "Item Type", "Quantity", "Item Price", "Description"};
    private ArrayList<Item> items;

    public ItemTable(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return items.get(rowIndex).ItemName;
            case 1:
                return items.get(rowIndex).ItemType;
            case 2:
                return items.get(rowIndex).Quantity;
            case 3:
                return items.get(rowIndex).Price;
            case 4:
                return items.get(rowIndex).Description;
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