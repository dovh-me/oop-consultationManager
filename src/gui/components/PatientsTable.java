package gui.components;

import gui.components.layouts.TableWithActionButtonsPanel;
import models.Patient;
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
    private final Button addPatientButton;

    public PatientsTable() {
        BorderPane borderPane = new BorderPane();

        TextField searchBar = initSearchbar();
        this.addPatientButton = initActionButtons();
        this.mainContentPanel = new TableWithActionButtonsPanel<>(Patient.tableFieldNames, Patient.tableColumns, GUIApplication.app.manager.getPatients(), addPatientButton);
        this.mainContentPanel.getTable().setPrefHeight(300);
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
                        patient.getUID().toLowerCase().contains(value) ||
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
        this.addPatientButton.setOnAction(listener);
    }

    public void setTableRowSelectionListener(ChangeListener<Patient> listener) {
        mainContentPanel.getTable().getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public void resetTableSelection() {
        mainContentPanel.getTable().getSelectionModel().clearSelection();
    }
}
