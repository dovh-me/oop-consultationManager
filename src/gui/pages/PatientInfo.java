package gui.pages;

import gui.components.CDatePickerInputGroup;
import gui.components.CTextFieldInputGroup;
import gui.components.Page;
import gui.models.Consultation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import main.GUIApplication;
import util.GUIValidator;
import util.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PatientInfo extends Page {
    private static final double prefMinWidth = 200.0;

    private VBox mainPanel;

    private Label doctorLabel;
    private Label consultationDateTimeLabel;

    private CTextFieldInputGroup firstName;
    private CTextFieldInputGroup surname;
    private CDatePickerInputGroup dateOfBirthDay;
    private CTextFieldInputGroup contactNumber;
    private Button addConsultationButton;

    public PatientInfo() {
        this.mainPanel = new VBox(5);
        this.mainPanel.setPrefWidth(450);
        this.mainPanel.setPrefHeight(450);
        initDoctorInfoPanel();
        initPatientInfoPanel();
        initAddConsultationButton();

        this.getChildren().add(new ScrollPane(mainPanel));
    }

    private void initDoctorInfoPanel() {
        GridPane pane = gridPanelApplyStyles(new GridPane());

        // Add doctor fields
        this.doctorLabel = new Label("--");
        pane.add(new Label("Doctor: "), 0,0);
        pane.add(this.doctorLabel, 1,0);

        // Add consultation date time
        this.consultationDateTimeLabel = new Label("--");
        pane.add(new Label("Consultation Date Time:"), 0,1);
        pane.add(this.consultationDateTimeLabel, 1,1);

        // Add pane to the page
        this.mainPanel.getChildren().add(pane);
    }

    private void initPatientInfoPanel() {
        // first name field
        this.firstName = new CTextFieldInputGroup("First Name:", new GUIValidator[]{}, new GUIValidator[]{
                GUIValidator.NOT_EMPTY, GUIValidator.LETTERS_ONLY
        });

        // surname field
        this.surname = new CTextFieldInputGroup("Surname:", new GUIValidator[]{}, new GUIValidator[]{
                GUIValidator.NOT_EMPTY, GUIValidator.LETTERS_ONLY
        });

        // date of birth field
        this.dateOfBirthDay = new CDatePickerInputGroup("Date of Birth:", new GUIValidator[]{GUIValidator.NOT_NULL});
        // only days until the current dates are allowed
        this.dateOfBirthDay.getInputField().setAllowedEnd(LocalDate.now());

        // contact number field
        this.contactNumber = new CTextFieldInputGroup("Contact Number:", new GUIValidator[] {GUIValidator.NUMBERS_ONLY}, new GUIValidator[] {
                GUIValidator.NOT_EMPTY,GUIValidator.PHONE_NUMBER});
        // notes

        // add the fields to the panel
        this.mainPanel.getChildren().addAll(
                this.firstName, this.surname, this.dateOfBirthDay, this.contactNumber
        );
        this.gridPanelApplyStyles(this.firstName);
        this.gridPanelApplyStyles(this.surname);
        this.gridPanelApplyStyles(this.dateOfBirthDay);
        this.gridPanelApplyStyles(this.contactNumber);
    }

    private void initAddConsultationButton() {
        FlowPane pane = new FlowPane();
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.setMinWidth(Double.MIN_VALUE);
        // Add the Enter patient info button
        this.addConsultationButton = new Button("Add Consultation");

        this.addConsultationButton.setOnAction((event -> {
            if(!validateInputFields()) return;

            GUIApplication.app.af.navigateTo(GUIApplication.app.getPatientInfo());
        }));

        pane.getChildren().add(this.addConsultationButton);
        this.mainPanel.getChildren().add(pane);
    }

    private boolean validateInputFields() {
        boolean isValid = this.firstName.validateInput();
        isValid = this.surname.validateInput() & isValid;
        isValid = this.contactNumber.validateInput() & isValid;
        isValid =this.dateOfBirthDay.validateInput() & isValid;
        return isValid;
    }

    private void loadDoctorInfoPanelData(Consultation consultation) {
        this.doctorLabel.setText(consultation.getDoctor().getFullName() + " / Med Lic No. " + consultation.getDoctor().getMedicalLicenseNo());

        // handle time date display
        LocalDateTime consultationDateTime = consultation.getConsultationDateTime();
        this.consultationDateTimeLabel.setText(
                LocalDate.from(consultationDateTime).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " / Approx.Time: " + LocalTime.from(consultationDateTime).format(DateTimeFormatter.ofPattern("hh:mm a"))
        );
    }

    private GridPane gridPanelApplyStyles(GridPane p) {
        p.setVgap(5);
        p.setHgap(5);
        p.getColumnConstraints().add(new ColumnConstraints(200));
        return p;
    }

    @Override
    public String getTitle() {
        return "Patient Information | Add Consultation";
    }

    @Override
    public void onNavigation() {
        super.onNavigation();
        loadDoctorInfoPanelData(CheckAvailability.consultation);
    }
}
