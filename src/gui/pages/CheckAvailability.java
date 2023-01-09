package gui.pages;

import constants.Formats;
import gui.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import main.GUIApplication;
import models.Doctor;
import util.ConsoleLog;
import util.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

public class CheckAvailability extends Page {
    public static Doctor doctor;
    public static LocalDateTime consultationDateTime;
    public static int consultationDuration;
    private CDatePickerInputGroup consultationDate;
    private CTimeFieldInputGroup consultationTime;
    private CTextFieldInputGroup doctorMedLicNo;

    private ArrayList<Label> infoPanelLabels;

    private Label validationMessage;
    private Button continueButton;
    private CTextFieldInputGroup consultationDurationInput;

    public CheckAvailability() {
        GridPane mainPane = createGeneralGridPane();

        mainPane.setVgap(10);
        mainPane.setHgap(5);

        mainPane.add(initAvailabilityInfoPane(), 0,0);
        mainPane.add(initValidationMessage(), 0,1);
        mainPane.add(initAvailabilityPanel(), 0,2);
        mainPane.add(initContinueButton(), 0,3);
        mainPane.add(initDoctorsTable(),1,0,1,3);

        this.setPadding(new Insets(5));
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(mainPane);
    }

    public void loadDoctor() {
        if(CheckAvailability.doctor.getMedicalLicenceNo() != null)
            this.doctorMedLicNo.getInputField().setText(CheckAvailability.doctor.getMedicalLicenceNo());
    }

    private GridPane initAvailabilityPanel() {
        GridPane availabilityPanel = createGeneralGridPane();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        Label titleLabel = new Label("Select Doctor");
        this.consultationDate = new CDatePickerInputGroup(
                "Consultation date (yyyy-MM-dd):", new Validator[]{Validator.NOT_NULL});
        this.consultationTime = new CTimeFieldInputGroup(
                "Consultation Time (HH:mm):", new Validator[]{Validator.NOT_NULL}
        );
        this.doctorMedLicNo = new CTextFieldInputGroup(
                "Medical Licence No:", new Validator[]{}, new Validator[]{Validator.NOT_EMPTY, Validator.MEDICAL_LICENSE_NO});
        Button checkAvailabilityButton = new Button("Check Availability");
        this.consultationDurationInput = new CTextFieldInputGroup("Consultation Duration (h)", new Validator[]{ Validator.NUMBERS_ONLY }, new Validator[]{Validator.NOT_EMPTY, new Validator<String>("Should be within range 0-24") {
            @Override
            public boolean validate(String input) {
                return Integer.parseInt(input) > 0 && Integer.parseInt(input) < 24;
            }
        }});


        titleLabel.setStyle("-fx-font-weight: bold;");
        columnConstraints.setPercentWidth(60);
        availabilityPanel.setStyle("-fx-padding: 10px;");

        this.consultationDate.getInputField().setAllowedStart(LocalDate.now());
        this.consultationDate.setGroupPaneColumnConstraints(columnConstraints, columnConstraints);
        this.consultationTime.setGroupPaneColumnConstraints(columnConstraints, columnConstraints);
        this.doctorMedLicNo.setGroupPaneColumnConstraints(columnConstraints, columnConstraints);
        this.consultationDurationInput.setGroupPaneColumnConstraints(columnConstraints, columnConstraints);

        // Initialise the action buttons
        checkAvailabilityButton.setStyle("-fx-background-color: #fe2c54; -fx-text-fill: white;");
        checkAvailabilityButton.setOnAction((event) -> {
            try {
                this.showValidationMessage("", "-fx-background-color: red;");
                if (!this.validateAvailabilityPanelInput()) {
                    showValidationMessage("Invalid inputs. Availability not checked", "-fx-text-fill: red");
                    return;
                }

                int consultationDuration = Integer.parseInt(consultationDurationInput.getInput());

                Optional<Doctor> optional = GUIApplication.app.manager.findDoctor(this.doctorMedLicNo.getInput());
                if (!optional.isPresent()) {
                    showValidationMessage("Medical licence number not found in the system. Please check again", "-fx-text-fill: red");
                    return;
                }
                Doctor doctor = optional.get();
                // When this segment is executed the inputs are expected to be already validated
                LocalDateTime consultationDateTime = LocalDateTime.of(this.consultationDate.getInput(), this.consultationTime.getInput());

                // check if the consultation starts and ends within the same day
                if (!LocalDate.from(consultationDateTime).isEqual(LocalDate.from(consultationDateTime.plusHours(consultationDuration))))  {
                    showValidationMessage("Consultation start and time should be within the same day", "-fx-text-fill: red");
                    return;
                }

                // Show the error message if the consultation being booked for a past date time
                if (consultationDateTime.isBefore(LocalDateTime.now())) {
                    showValidationMessage("Consultation cannot be booked for date and times from the past", "-fx-text-fill: red");
                    return;
                }

                if (!doctor.getAvailability(consultationDateTime, consultationDuration)) {
                    doctor = GUIApplication.app.manager.getAvailableDoctor(optional.get().getSpecialization(), consultationDateTime, consultationDuration);
                    if (doctor == null) {
                        showValidationMessage("No doctors available for the selected date time: " + consultationDateTime.format(Formats.DATE_TIME_OUTPUT_FORMAT), "-fx-text-fill: red");
                        loadInfoPanelData(false, optional.get());
                        CheckAvailability.doctor = null;
                        return;
                    } else {
                        showValidationMessage(String.format("Selected doctor not available: %s. System has selected a available doctor", doctorMedLicNo.getInput()), "-fx-text-fill: blue;");
                    }
                }
                loadInfoPanelData(true, doctor);
                this.continueButton.setDisable(false);
                CheckAvailability.consultationDuration = consultationDuration;
                CheckAvailability.doctor = doctor;
                CheckAvailability.consultationDateTime = consultationDateTime;
            }catch (NumberFormatException e) {
                showValidationMessage("Invalid consultation duration", "-fx-text-fill: red;");
            }
            catch (Exception e) {
                showValidationMessage("Unexpected error occurred. Please try again", "-fx-text-fill: red;");
                ConsoleLog.error(e.getLocalizedMessage());
            }
        });

        availabilityPanel.add(titleLabel,0,0,2,1);
        availabilityPanel.add(this.consultationDate, 0,1);
        availabilityPanel.add(this.consultationTime, 0,2);
        availabilityPanel.add(this.doctorMedLicNo, 0, 3);
        availabilityPanel.add(this.consultationDurationInput, 0, 4);
        availabilityPanel.add(checkAvailabilityButton, 0,5,2,1);

        return availabilityPanel;
    }

    private Label initValidationMessage() {
        this.validationMessage = new Label("Default validation message");
        this.validationMessage.setStyle("-fx-text-fill: red");
        this.validationMessage.setVisible(false);
        return this.validationMessage;
    }

    private GridPane initAvailabilityInfoPane() {
        this.infoPanelLabels = new ArrayList<>();
        this.infoPanelLabels.add(new Label("default availability"));
        this.infoPanelLabels.add(new Label("default doctor name"));
        this.infoPanelLabels.add(new Label("default specialisation"));

        GridPane infoPanel = createGeneralGridPane();
        infoPanel.setAlignment(Pos.CENTER);
        infoPanel.setVgap(10);
        infoPanel.setHgap(10);
        infoPanel.add(new Label("Availability:"), 0,0);
        infoPanel.add(this.infoPanelLabels.get(0), 1, 0);
        infoPanel.add(new Label("Doctor Name:"), 0, 1);
        infoPanel.add(this.infoPanelLabels.get(1), 1, 1);
        infoPanel.add(new Label("Specialisation"), 0, 2);
        infoPanel.add(this.infoPanelLabels.get(2), 1, 2);

        return infoPanel;
    }

    private DoctorsTable initDoctorsTable() {
        DoctorsTable doctorsTable = new DoctorsTable();
        doctorsTable.repopulateTable();
        doctorsTable.setTableRowSelectionListener((observableValue,oldValue, newValue) -> {
            if(newValue == null) return;
            CheckAvailability.doctor = newValue;
            this.continueButton.setDisable(true);
            this.loadDoctor();
        });

        return doctorsTable;
    }

    private FlowPane initContinueButton() {
        FlowPane pane = new FlowPane();
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.setMinWidth(Double.MIN_VALUE);
        // Add the Enter patient info button
        this.continueButton = new Button("Continue");
        this.continueButton.setDisable(true);

        this.continueButton.setOnAction((event -> {
            if(
                    CheckAvailability.doctor == null || CheckAvailability.consultationDateTime == null
            ) return;

            GUIApplication.app.af.navigateTo(GUIApplication.app.getPatientInfo());
        }));

        pane.getChildren().add(this.continueButton);
        return pane;
    }

    private void loadInfoPanelData(boolean isAvailable, Doctor doctor) {
        if(isAvailable){
            this.infoPanelLabels.get(0).setText("Available");
            this.infoPanelLabels.get(0).setStyle("-fx-background-radius: 5px; -fx-padding: 3px;-fx-font-weight: bold;-fx-text-fill: #ffffff; -fx-background-color: #0a7a3b");
        } else {
            this.infoPanelLabels.get(0).setText("Unavailable");
            this.infoPanelLabels.get(0).setStyle("-fx-background-radius: 5px; -fx-padding: 3px;-fx-font-weight: bold;-fx-text-fill: #ffffff; -fx-background-color: #c41826");
        }
        this.infoPanelLabels.get(1).setText(doctor.getFullName());
        this.infoPanelLabels.get(2).setText(doctor.getSpecialization());
    }

    private void resetInfoPanelFields() {
        this.infoPanelLabels.get(0).setStyle("-fx-text-fill: #000; -fx-background-color: transparent;");
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

        LocalTime timeToSet = LocalTime.now();
        timeToSet = timeToSet.plusHours(2);
        timeToSet = timeToSet.minusMinutes(timeToSet.getMinute());

        this.consultationDate.getInputField().setValue(LocalDate.now());
        this.consultationTime.getInputField().setValue(timeToSet);
    }

    private void showValidationMessage(String message, String messageStyles) {
        this.validationMessage.setStyle(messageStyles);
        this.validationMessage.setText(message);
        this.validationMessage.setVisible(true);
    }

    private boolean validateAvailabilityPanelInput() {
        boolean isValid;
        isValid = this.doctorMedLicNo.validateInput();
        isValid = this.consultationDate.validateInput() && isValid;
        isValid = this.consultationTime.validateInput() && isValid;
        isValid = this.consultationDurationInput.validateInput() && isValid;
        return isValid;
    }

    private GridPane createGeneralGridPane() {
        GridPane pane = new GridPane();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(50);
        pane.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        pane.setStyle("-fx-padding: 10px;");
        pane.setVgap(10);

        return pane;
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
}

// TODO: improve with bindings
