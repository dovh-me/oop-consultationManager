package gui.components.layouts;

import gui.components.TabularModel;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class TableWithActionButtonsPanel<T extends TabularModel> extends BorderPane {
    private TableView<T> table;
    private FlowPane actionButtonsContainer;
    private final List<T> tableData;
    private final String[] tableColumns;

    private String[] modelProperties;

    public TableWithActionButtonsPanel(String[] modelProperties, String[] tableColumns,List<T> tableData, Button... actionButtons) {
        this.modelProperties = modelProperties;
        this.tableData = tableData;
        this.tableColumns = tableColumns;

        this.table = new TableView<T>();
        // Initialize table columns
        for (int i = 0;i < this.tableColumns.length; i++) {
            // TODO: move the model properties and table column names to a single data structure
            TableColumn<T, String> col = new TableColumn<>(this.tableColumns[i]);
            col.setCellValueFactory(new PropertyValueFactory<>(this.modelProperties[i]));
            this.table.getColumns().add(col);
        }

        this.initActionButtons(actionButtons);

        this.setCenter(this.table);
        this.setBottom(actionButtonsContainer);
    }

    public int getTableSelectionIndex() {
        return this.table.getSelectionModel().getSelectedIndex();
    }

    public void registerTableMouseListener(MouseListener listener) {

    }

    public void loadTableData(ArrayList<T> tableData) {
        // Get data to populate in the table
        this.table.setItems(FXCollections.observableList(tableData));

        this.table.setVisible(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.table.setVisible(true);
    }

    private void initActionButtons(Button[] actionButtons) {
        this.actionButtonsContainer = new FlowPane();
        this.actionButtonsContainer.setAlignment(Pos.CENTER_RIGHT);
        for (Button actionButton : actionButtons) {
            this.actionButtonsContainer.getChildren().add(actionButton);
        }
    }

    public TableView<T> getTable() {
        return table;
    }
}
