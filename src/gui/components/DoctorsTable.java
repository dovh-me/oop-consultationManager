package gui.components;

import gui.components.layouts.TableWithActionButtonsPanel;
import gui.models.Doctor;
import gui.pages.CheckAvailability;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import main.GUIApplication;
import util.ConsoleLog;

public class DoctorsTable extends StackPane {
        private final TableWithActionButtonsPanel<Doctor> mainContentPanel;
        private Button selectDoctorButton;

        public DoctorsTable() {
            initActionButtons();
            this.mainContentPanel = new TableWithActionButtonsPanel<>(Doctor.tableFieldNames, Doctor.tableColumns, GUIApplication.app.manager.getDoctors(), selectDoctorButton);
            this.mainContentPanel.getTable().getSelectionModel().selectedItemProperty().addListener((observable,oldValue, newValue) -> {
                ConsoleLog.info("Table selection listener triggered...");
                if(newValue == null) {
                    selectDoctorButton.setDisable(true);
                    return;
                }
                System.out.println("Setting selected value...");
                CheckAvailability.doctor = newValue;
                selectDoctorButton.setDisable(false);
            });
            this.getChildren().add(this.mainContentPanel);
        }

        public void initActionButtons() {
            this.selectDoctorButton = new Button("Select Doctor");
            this.selectDoctorButton.setDisable(true);
            FlowPane pane = new FlowPane();
            pane.setMinWidth(Double.MAX_VALUE);
            pane.setAlignment(Pos.CENTER_RIGHT);
            pane.getChildren().add(this.selectDoctorButton);
        }

        public void repopulateTable() {
            mainContentPanel.loadTableData(GUIApplication.app.manager.getDoctors());
        }

        public void setActionButtonOnClickListener(EventHandler<ActionEvent> listener) {
            this.selectDoctorButton.setOnAction(listener);
        }
}
