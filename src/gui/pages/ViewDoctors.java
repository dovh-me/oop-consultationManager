package gui.pages;

import gui.components.Page;
import gui.components.layouts.TableWithActionButtonsPanel;
import gui.models.Doctor;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import main.GUIApplication;
import util.ConsoleLog;

public class ViewDoctors extends Page {
    private TableWithActionButtonsPanel<Doctor> mainContentPanel;
    private Button addConsultationButton;

    public ViewDoctors() {
        initActionButtons();
        this.mainContentPanel = new TableWithActionButtonsPanel<>(Doctor.tableFieldNames, Doctor.tableColumns, GUIApplication.app.manager.getDoctors(), addConsultationButton);
        this.mainContentPanel.getTable().getSelectionModel().selectedItemProperty().addListener((observable,oldValue, newValue) -> {
            ConsoleLog.info("Table selection listener triggered...");
            if(newValue == null) {
                addConsultationButton.setDisable(true);
                return;
            }
            System.out.println("Setting selected value...");
            CheckAvailability.doctor = newValue;
            addConsultationButton.setDisable(false);
        });
        this.getChildren().add(this.mainContentPanel);
    }

    public void initActionButtons() {
        this.addConsultationButton = new Button("Add Consultation");
        this.addConsultationButton.setDisable(true);
        this.addConsultationButton.setOnAction(actionEvent -> {
            GUIApplication.app.af.navigateTo(GUIApplication.app.getCheckAvailability());
        });
        FlowPane pane = new FlowPane();
        pane.setMinWidth(Double.MAX_VALUE);
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.getChildren().add(this.addConsultationButton);
    }

    @Override
    public String getTitle() {
        return "View Doctors";
    }

    @Override
    public void onNavigation() {
        super.onNavigation();
        mainContentPanel.loadTableData(GUIApplication.app.manager.getDoctors());
    }
}
