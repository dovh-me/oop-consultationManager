package gui.components;

import gui.components.layouts.TableWithActionButtonsPanel;
import gui.models.Patient;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import main.GUIApplication;

import java.util.ArrayList;
import java.util.List;

public class PatientsTable extends StackPane {
    private final TableWithActionButtonsPanel<Patient> mainContentPanel;
    private final Button selectDoctorButton;

    public PatientsTable() {
        BorderPane borderPane = new BorderPane();

        TextField searchBar = initSearchbar();
        this.selectDoctorButton = initActionButtons();
        this.mainContentPanel = new TableWithActionButtonsPanel<>(Patient.tableFieldNames, Patient.tableColumns, GUIApplication.app.manager.getPatients(), selectDoctorButton);
        borderPane.setTop(searchBar);
        borderPane.setCenter(mainContentPanel);
        this.getChildren().add(borderPane);
    }

    public Button initActionButtons() {
        return new Button("Add Patient");
    }

    private TextField initSearchbar() {
        TextField searchbar = new TextField();
        searchbar.setPromptText("Filter by name or UID...");
        searchbar.textProperty().addListener(((observable, oldValue, newValue) -> {
            String value = newValue.toLowerCase();

            FilteredList<Patient> filteredList = new FilteredList<>(FXCollections.observableList(GUIApplication.app.manager.getPatients()), (patient) -> {
                // check for substring values of uid and patient full name
                return
                        // uid
                        patient.getUid().toLowerCase().contains(value) ||
                        // full name
                        patient.getFullName().toLowerCase().contains(value) ;
            });

            populateTable(filteredList);
        }));

        return searchbar;
    }

    public void populateTable() {
        mainContentPanel.loadTableData(new ArrayList<>(GUIApplication.app.manager.getPatients()));
    }

    public void populateTable(List<Patient> items) {
        mainContentPanel.loadTableData(items);
    }

    public void setActionButtonOnClickListener(EventHandler<ActionEvent> listener) {
        this.selectDoctorButton.setOnAction(listener);
    }

    public void setTableRowSelectionListener(ChangeListener<Patient> listener) {
        mainContentPanel.getTable().getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public void resetTableSelection() {
        mainContentPanel.getTable().getSelectionModel().clearSelection();
    }
}
