package gui.pages;

import constants.Formats;
import gui.components.*;
import gui.models.Consultation;
import gui.models.Doctor;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.GUIApplication;
import util.GUIValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class CheckAvailability extends Page {
    public static Doctor doctor;
    public static LocalDateTime consultationDateTime;
    private CDatePickerInputGroup consultationDate;
    private CTimeFieldInputGroup consultationTime;
    private CTextFieldInputGroup doctorMedLicNo;

    private ArrayList<Label> infoPanelLabels;

    private Label validationMessage;
    private Button continueButton;

    private final VBox mainPane;

    public CheckAvailability() {
        this.mainPane = new VBox();
        this.setMaxWidth(500);
        initAvailabilityPanel();
        initValidationMessage();
        initInfoPanel();
        initContinueButton();

        this.getChildren().add(mainPane);
    }

    @Override
    public String getTitle() {
        return "Check Availability";
    }

    @Override
    public void onNavigation() {
        resetAvailabilityPanelFields();
        resetInfoPanelFields();
        super.onNavigation();
        if(this.getPrevPage() == GUIApplication.app.getViewDoctors()) {
            loadDoctor();
        }
    }

    public void loadDoctor() {
        this.doctorMedLicNo.getInputField().setText(CheckAvailability.doctor.getMedicalLicenseNo());
    }

    private void initAvailabilityPanel() {
        BorderPane availabilityPanel = new BorderPane();

        // Initialise the section title label
        availabilityPanel.setTop(new Label("Select Doctor:"));

        // Initialise the input groups
        GridPane inputGroupsPanel = new GridPane();

        this.consultationDate = new CDatePickerInputGroup(
                "Consultation date time (yyyy-MM-dd HH:mm):", new GUIValidator[]{GUIValidator.NOT_NULL});
        this.consultationDate.getInputField().setAllowedStart(LocalDate.now());
        this.consultationTime = new CTimeFieldInputGroup(
                "Consultation Time:", new GUIValidator[]{GUIValidator.NOT_NULL}
        );

        this.doctorMedLicNo = new CTextFieldInputGroup(
                "Doctor Medical License Number:", new GUIValidator[]{}, new GUIValidator[]{GUIValidator.NOT_EMPTY,GUIValidator.MEDICAL_LICENSE_NO});

        inputGroupsPanel.add(this.consultationDate, 0,0);
        inputGroupsPanel.add(this.consultationTime, 0,1);
        inputGroupsPanel.add(this.doctorMedLicNo, 0, 2);
        availabilityPanel.setCenter(inputGroupsPanel);
        // Initialise the action buttons
        Button checkAvailabilityButton = new Button("Check Availability");
        checkAvailabilityButton.setStyle("-fx-background-color: #fe2c54");
        checkAvailabilityButton.setOnAction((event) -> {
            this.showValidationMessage("");
            if(!this.validateAvailabilityPanelInput()) {showValidationMessage("Invalid inputs. Availability not checked");return;}

            Optional<Doctor> optional = GUIApplication.app.manager.findDoctor(this.doctorMedLicNo.getInput());
            if(!optional.isPresent()) {
                showValidationMessage("Medical license number not found in the system. Please check again");
                return;
            }
            Doctor doctor = optional.get();
            // When this segment is executed the inputs are expected to be already validated
            LocalDateTime consultationDateTime = LocalDateTime.of(this.consultationDate.getInput(), this.consultationTime.getInput());
            if(!doctor.getAvailability(consultationDateTime)){
                doctor = GUIApplication.app.manager.getAvailableDoctor(optional.get().getSpecialization(), consultationDateTime);
                if(doctor == null) {
                    showValidationMessage("No doctors available for the selected date time: " + consultationDateTime.format(Formats.DATE_TIME_OUTPUT_FORMAT));
                    loadInfoPanelData(false, optional.get());
                    CheckAvailability.doctor = null;
                    return;
                }
            }
            loadInfoPanelData(true, doctor);
            this.continueButton.setDisable(false);
            CheckAvailability.doctor = doctor;
            CheckAvailability.consultationDateTime = consultationDateTime;
        });

        checkAvailabilityButton.setAlignment(Pos.CENTER_RIGHT);
        availabilityPanel.setBottom(checkAvailabilityButton);

        this.mainPane.getChildren().add(availabilityPanel);
    }

    private void initValidationMessage() {
        this.validationMessage = new Label("Default validation message");
        this.validationMessage.setStyle("-fx-text-fill: #f00");
        this.mainPane.getChildren().add(this.validationMessage);
        this.validationMessage.setVisible(false);
    }

    private void initInfoPanel() {
        this.infoPanelLabels = new ArrayList<>();
        this.infoPanelLabels.add(new Label("default availability"));
        this.infoPanelLabels.add(new Label("default doctor name"));
        this.infoPanelLabels.add(new Label("default specialisation"));

        GridPane infoPanel = new GridPane();
        infoPanel.setVgap(10);
        infoPanel.setHgap(10);
        infoPanel.add(new Label("Availability:"), 0,0);
        infoPanel.add(this.infoPanelLabels.get(0), 1, 0);
        infoPanel.add(new Label("Doctor Name:"), 0, 1);
        infoPanel.add(this.infoPanelLabels.get(1), 1, 1);
        infoPanel.add(new Label("Specialisation"), 0, 2);
        infoPanel.add(this.infoPanelLabels.get(2), 1, 2);

        this.mainPane.getChildren().add(infoPanel);
    }

    private void initContinueButton() {
        FlowPane pane = new FlowPane();
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.setMinWidth(Double.MIN_VALUE);
        // Add the Enter patient info button
        this.continueButton = new Button("Continue >");
        this.continueButton.setDisable(true);

        this.continueButton.setOnAction((event -> {
            if(
                    CheckAvailability.doctor == null || CheckAvailability.consultationDateTime == null
            ) return;

            GUIApplication.app.af.navigateTo(GUIApplication.app.getPatientInfo());
        }));

        pane.getChildren().add(this.continueButton);
        this.mainPane.getChildren().add(pane);
    }

    private void loadInfoPanelData(boolean isAvailable, Doctor doctor) {
        if(isAvailable){
            this.infoPanelLabels.get(0).setText("Available");
            this.infoPanelLabels.get(0).setStyle("-fx-text-fill: #0f0");
        } else {
            this.infoPanelLabels.get(0).setText("Unavailable");
            this.infoPanelLabels.get(0).setStyle("-fx-text-fill: #f00");
        }
        this.infoPanelLabels.get(1).setText(doctor.getFullName());
        this.infoPanelLabels.get(2).setText(doctor.getSpecialization());
    }

    private void resetInfoPanelFields() {
        this.infoPanelLabels.get(0).setStyle("-fx-text-fill: #000");
        for (Label infoPanelLabel : infoPanelLabels) {
            infoPanelLabel.setText("--");
        }
        // disable the continue button
        this.continueButton.setDisable(true);
    }

    private void resetAvailabilityPanelFields() {
        CheckAvailability.consultationDateTime = null;
        this.validationMessage.setText("");
        this.validationMessage.setVisible(false);

        this.consultationDate.getInputField().setValue(LocalDate.now());
        this.consultationTime.getInputField().resetValue();
    }

    private void showValidationMessage(String message) {
        this.validationMessage.setText(message);
        this.validationMessage.setVisible(true);
    }

    private boolean validateAvailabilityPanelInput() {
        boolean isValid;
        isValid = this.doctorMedLicNo.validateInput();
        isValid = this.consultationDate.validateInput() & isValid;
        isValid = this.consultationTime.validateInput() & isValid;
        return isValid;
    }
}

// TODO: improve with bindings
