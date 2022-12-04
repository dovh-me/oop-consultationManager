package gui.components.layouts;

import gui.components.TabularModel;
import gui.components.UneditableDefaultTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;

public class TableWithActionButtonsPanel<T extends TabularModel> extends JPanel {
    private JScrollPane tableContainer;
    private JTable table;
    private JPanel actionButtonsContainer;
    private final List<T> tableData;
    private final String[] tableColumns;

    public TableWithActionButtonsPanel(String[] tableColumns,List<T> tableData, JButton... actionButtons) {
        this.tableData = tableData;
        this.tableColumns = tableColumns;
        this.setLayout(new BorderLayout());
        this.initTable();
        this.initActionButtons(actionButtons);

        this.add(tableContainer, BorderLayout.CENTER);
        this.add(actionButtonsContainer, BorderLayout.PAGE_END);
    }

    public int getTableSelectionIndex() {
        return this.table.getSelectedRow();
    }

    public void registerTableMouseListener(MouseListener listener) {
        this.table.addMouseListener(listener);
    }

    private void initTable() {
        // Get data to populate in the table
        String[][] tableData = new String[this.tableData.size()][];
        for (int i = 0; i < this.tableData.size(); i++) {
            tableData[i] = this.tableData.get(i).getTableRowData();
            System.out.println(Arrays.deepToString(this.tableData.get(i).getTableRowData()));
        }

        this.table = new JTable(new UneditableDefaultTableModel(tableData,this.tableColumns));
//        this.table = new JTable(new String[][] {
//                {"Osura Hettiarahchi", "Cosmetic Dermatologist", "a123456"},
//                {"Osura Hettiarahchi", "Cosmetic Dermatologist", "a123456"},
//        },new String[] {"Full name", "Specialisation", "Medical License No."});
        this.table.setVisible(false);
//        table.setModel(new DefaultTableModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        this.table.setVisible(true);
        this.tableContainer = new JScrollPane(this.table);
    }

    private void initActionButtons(JButton[] actionButtons) {
        this.actionButtonsContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        for (JButton actionButton : actionButtons) {
            this.actionButtonsContainer.add(actionButton);
        }
    }
}
