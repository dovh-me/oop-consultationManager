package gui.pages;

import gui.components.TextFieldInputGroup;
import gui.models.Doctor;
import util.GUIValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CheckAvailability extends Page{
    private Doctor doctor;
    private LocalDateTime consultationTime;

    private JPanel availabilityPanel;
    private TextFieldInputGroup consultationDateTime;
    private TextFieldInputGroup doctorMedLicNo;

    private JPanel infoPanel;
    private ArrayList<JLabel> infoPanelLabels;
    private JButton mainActionButton;

    public CheckAvailability() {

    }

    public CheckAvailability(Doctor doctor) {
        this.doctor = doctor;
    }

    private void initAvailabilityPanel() {
        this.availabilityPanel = new JPanel(new BorderLayout());

        // Initialise the section title label
        this.availabilityPanel.add(new JLabel("Check Availability of Doctor:"), BorderLayout.PAGE_START);

        // Initialise the input groups
        JPanel inputGroupsPanel = new JPanel(new GridLayout(2,1));

        this.consultationDateTime = new TextFieldInputGroup(
                "Consultation date time (format : yyyy-MM-dd HH:mm):", new GUIValidator[]{}, new GUIValidator[]{GUIValidator.IS_EMPTY,GUIValidator.DATE_TIME_STRING});

        this.doctorMedLicNo = new TextFieldInputGroup(
                "Doctor Medical License Number:", new GUIValidator[]{new GUIValidator<KeyEvent>("") {
            @Override
            public boolean validate(KeyEvent input) {
                return false;
            }
        }}, new GUIValidator[]{GUIValidator.IS_EMPTY,GUIValidator.MEDICAL_LICENSE_NO});

        inputGroupsPanel.add(this.consultationDateTime);
        inputGroupsPanel.add(this.doctorMedLicNo);
        // Initialise the action buttons

    }

    private void initInfoPanel() {
        this.infoPanelLabels = new ArrayList<>();
        this.infoPanelLabels.add(new JLabel(""));
        this.infoPanelLabels.add(new JLabel(""));
        this.infoPanelLabels.add(new JLabel(""));

        this.infoPanel = new JPanel(new GridLayout(3,2,5,5));
        this.infoPanel.add(new JLabel("Availability:"));
        this.infoPanel.add(this.infoPanelLabels.get(0));
        this.infoPanel.add(new JLabel("Doctor Name:"));
        this.infoPanel.add(this.infoPanelLabels.get(1));
        this.infoPanel.add(new JLabel("Specialisation"));
        this.infoPanel.add(this.infoPanelLabels.get(2));
    }

    private void loadInfoPanelData(String[] data) {
        for (int i = 0; i < data.length; i++) {
            this.infoPanelLabels.get(i).setText(data[i]);
        }
    }
}
