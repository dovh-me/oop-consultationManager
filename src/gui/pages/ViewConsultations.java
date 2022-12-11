package gui.pages;

import gui.components.Page;
import gui.components.layouts.TableWithActionButtonsPanel;
import gui.models.Consultation;
import javafx.scene.control.Button;
import util.ConsoleLog;

import java.util.ArrayList;

public class ViewConsultations extends Page {

    private TableWithActionButtonsPanel<Consultation> mainContentPanel;
    private Button addConsultationButton;

    public ViewConsultations(ArrayList<Consultation> consultations) {
        initActionButtons();
        this.mainContentPanel = new TableWithActionButtonsPanel<>(Consultation.tableFieldNames, Consultation.tableColumns, consultations, addConsultationButton);
        this.getChildren().add(this.mainContentPanel);
    }

    public void initActionButtons() {
        this.addConsultationButton = new Button("View Consultation");
        this.addConsultationButton.setOnAction(actionEvent -> {
            ConsoleLog.info("View Consultation button clicked");
        });
    }

    @Override
    public String getTitle() {
        return "View Consultations";
    }

    @Override
    public void onNavigation() {
        super.onNavigation();
        mainContentPanel.initTableData();
    }
}
