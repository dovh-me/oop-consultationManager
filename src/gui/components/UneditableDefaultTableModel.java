package gui.components;

import gui.models.Consultation;
import gui.models.Doctor;

import javax.swing.table.DefaultTableModel;

public class UneditableDefaultTableModel extends DefaultTableModel {

    public UneditableDefaultTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
