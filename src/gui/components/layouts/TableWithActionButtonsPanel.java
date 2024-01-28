package gui.components.layouts;

import gui.components.TabularModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.awt.event.MouseListener;
import java.util.List;

public class TableWithActionButtonsPanel<T extends TabularModel> extends BorderPane {
    private final TableView<T> table;
    private FlowPane actionButtonsContainer;

    public TableWithActionButtonsPanel(String[] modelProperties, String[] tableColumns,List<T> tableData, Button... actionButtons) {

        this.table = new TableView<T>();
        // Initialize table columns
        for (int i = 0; i < tableColumns.length; i++) {
            // TODO: move the model properties and table column names to a single data structure
            TableColumn<T, String> col = new TableColumn<>(tableColumns[i]);
            col.setPrefWidth(200);
            col.setCellValueFactory(new PropertyValueFactory<>(modelProperties[i]));
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

    public void loadTableData(List<T> tableData) {
        // Get data to populate in the table
        this.table.getItems().removeIf((e) -> true);
        this.table.getItems().addAll(tableData);

        this.table.setVisible(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.table.setVisible(true);
    }

    private void initActionButtons(Button[] actionButtons) {
        this.actionButtonsContainer = new FlowPane();
        this.actionButtonsContainer.setHgap(5);
        this.actionButtonsContainer.setPadding(new Insets(5,0,0,0));
        this.actionButtonsContainer.setAlignment(Pos.CENTER_RIGHT);
        for (Button actionButton : actionButtons) {
            this.actionButtonsContainer.getChildren().add(actionButton);
        }
    }

    public TableView<T> getTable() {
        return table;
    }
}
