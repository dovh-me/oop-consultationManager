package gui.pages;

import gui.components.layouts.TableWithActionButtonsPanel;
import gui.models.Doctor;
import util.ConsoleLog;

import javax.swing.*;
import java.util.ArrayList;

public class ViewDoctors extends Page{
    private TableWithActionButtonsPanel<Doctor> mainContentPanel;
    private JButton addConsultationButton;

    public ViewDoctors(ArrayList<Doctor> doctors) {
        initActionButtons();
        this.mainContentPanel = new TableWithActionButtonsPanel<>(Doctor.tableColumns, doctors, addConsultationButton);
        this.add(this.mainContentPanel);
    }

    public void initActionButtons() {
        this.addConsultationButton = new JButton("Add Consultation");
        this.addConsultationButton.addActionListener(actionEvent -> {
            ConsoleLog.info("Add consultation button clicked");
        });
    }
}
