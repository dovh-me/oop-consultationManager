package gui.pages;

import gui.components.Page;
import gui.components.layouts.TableWithActionButtonsPanel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import main.GUIApplication;
import models.Consultation;
import util.ConsoleLog;

public class ViewConsultations extends Page {
    private Consultation selectedConsultation;
    private final TableWithActionButtonsPanel<Consultation> mainContentPanel;
    private Button viewConsultationButton;
    private Button cancelConsultationButton;

    public ViewConsultations() {
        initActionButtons();
        this.mainContentPanel = new TableWithActionButtonsPanel<>(Consultation.tableFieldNames, Consultation.tableColumns, GUIApplication.app.manager.getConsultations(), viewConsultationButton, cancelConsultationButton);
        this.setPadding(new Insets(5));
        this.getChildren().add(this.mainContentPanel);

        mainContentPanel.getTable().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ConsoleLog.info("Table selection listener triggered...");
            if(newValue == null) {
                viewConsultationButton.setDisable(true);
                cancelConsultationButton.setDisable(true);
                return;
            }
            this.selectedConsultation = newValue;
            // enabling buttons if a record is selected in the table
            viewConsultationButton.setDisable(false);
            cancelConsultationButton.setDisable(false);
        });
    }

    public void initActionButtons() {
        try {
            this.cancelConsultationButton = new Button("Cancel Consultation");
            this.viewConsultationButton = new Button("View Consultation");

            this.cancelConsultationButton.setStyle("-fx-background-color: #f00; -fx-text-fill: #fff;");
            this.cancelConsultationButton.setDisable(true);
            this.viewConsultationButton.setDisable(true);

            this.cancelConsultationButton.setOnAction(actionEvent -> {
                ConsoleLog.info("Removing the selected consultation");
                GUIApplication.app.manager.cancelConsultation(
                        this.selectedConsultation
                );
                this.mainContentPanel.loadTableData(GUIApplication.app.manager.getConsultations()); // resetting the table content
            });
            this.viewConsultationButton.setOnAction(actionEvent -> {
                ConsoleLog.info("View Consultation button clicked");
                ViewConsultation.consultationSimpleObjectProperty.set(this.selectedConsultation);
                GUIApplication.app.af.navigateTo(GUIApplication.app.getViewConsultation());
            });
        } catch (Exception e) {
            ConsoleLog.error("Unknown error in the actions buttons");
            e.printStackTrace();
        }
    }

    @Override
    public String getTitle() {
        return "View Consultations";
    }

    @Override
    public void onNavigation() {
        super.onNavigation();
        mainContentPanel.loadTableData(GUIApplication.app.manager.getConsultations());
    }

    @Override
    public void onExit() {
        super.onExit();
        this.mainContentPanel.getTable().getSelectionModel().clearSelection();
    }
}
