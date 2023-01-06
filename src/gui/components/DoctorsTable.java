package gui.components;

import gui.components.layouts.TableWithActionButtonsPanel;
import models.Doctor;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import main.GUIApplication;

public class DoctorsTable extends StackPane {
        private final TableWithActionButtonsPanel<Doctor> mainContentPanel;

        public DoctorsTable() {
            initActionButtons();
            this.mainContentPanel = new TableWithActionButtonsPanel<>(Doctor.tableFieldNames, Doctor.tableColumns, GUIApplication.app.manager.getDoctors());
            this.getChildren().add(this.mainContentPanel);
        }

        public void initActionButtons() {
            FlowPane pane = new FlowPane();
            pane.setMinWidth(Double.MAX_VALUE);
            pane.setAlignment(Pos.CENTER_RIGHT);
        }

        public void repopulateTable() {
            mainContentPanel.loadTableData(GUIApplication.app.manager.getDoctors());
        }

        public void setTableRowSelectionListener(ChangeListener<Doctor> listener) {
            mainContentPanel.getTable().getSelectionModel().selectedItemProperty().addListener(listener);
        }
}
