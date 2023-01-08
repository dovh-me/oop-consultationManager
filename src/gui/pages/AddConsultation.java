package gui.pages;

import constants.Formats;
import exceptions.CryptoException;
import exceptions.IllegalConsultationException;
import gui.components.AddPatientFormPopup;
import gui.components.CNotesInputGroup;
import gui.components.Page;
import gui.components.PatientsTable;
import javafx.geometry.Insets;
import models.Consultation;
import models.Doctor;
import models.Patient;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import main.GUIApplication;
import util.AlertBox;
import util.ConsoleLog;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AddConsultation extends Page {
    private static final SimpleObjectProperty<Patient> patientProperty = new SimpleObjectProperty<>();
    private static final SimpleObjectProperty<Doctor> doctorProperty = new SimpleObjectProperty<>();
    private static final SimpleObjectProperty<LocalDateTime> consultationDateTimeProperty = new SimpleObjectProperty<>();
    private static final SimpleStringProperty validationMessageProperty = new SimpleStringProperty();
    private CNotesInputGroup notes;
    private PatientsTable patientsTable;


    public AddConsultation() {
        ColumnConstraints percentWidthConstraint = new ColumnConstraints();
        percentWidthConstraint.setPercentWidth(50);
        GridPane mainPanel = createGeneralGridPane();
        mainPanel.getColumnConstraints().addAll(percentWidthConstraint, percentWidthConstraint);
        mainPanel.setAlignment(Pos.CENTER);
        GridPane doctorPane = initDoctorInfoPanel();
        GridPane patientPane = initPatientInfoPanel();
        StackPane patientTablePane = initPatientTable();
        GridPane consultationInfoPane = initConsultationPane();
        FlowPane addConsultationButtonPane = initAddConsultationButton();

        mainPanel.setVgap(10);
        mainPanel.setStyle("-fx-background-color: #a5cafa;");
        mainPanel.setPadding(new Insets(5));

        mainPanel.addRow(0,doctorPane);
        mainPanel.add(new Separator(Orientation.HORIZONTAL),0,1,2,1);
        mainPanel.addRow(2,patientPane, patientTablePane);
        mainPanel.add(new Separator(Orientation.HORIZONTAL),0,3,2,1);
        mainPanel.addRow(4,consultationInfoPane);
        mainPanel.add(addConsultationButtonPane,0,5,2,1);

        this.setPadding(new Insets(5));
        ScrollPane scrollWrapper = new ScrollPane(mainPanel);
        scrollWrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollWrapper.setStyle("-fx-background-color: #a5cafa;");
        this.getChildren().add(scrollWrapper);
    }

    private GridPane initDoctorInfoPanel() {
        GridPane pane = createGeneralGridPane();
        Label titleLabel = createSectionTitleLabel("Doctor Information");
        Label doctorName = new Label("--");
        Label medLicNo = new Label("--");


        // Add doctor fields
        pane.add(titleLabel,0,0,2,1);
        pane.addRow(1,new Label("Doctor Name: "), doctorName);
        pane.addRow(2, new Label("Medical Licence No: "), medLicNo);

        AddConsultation.doctorProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                doctorName.setText("--");
                medLicNo.setText("--");
                return;
            }

            doctorName.setText(newValue.getFullName());
            medLicNo.setText(newValue.getMedicalLicenceNo());

        });

        // Add pane to the page
        return pane;
    }

    private GridPane initPatientInfoPanel() {
        GridPane innerGridPane = createGeneralGridPane();
        innerGridPane.setAlignment(Pos.TOP_LEFT);
        // initialize the labels
        Label titleLabel = createSectionTitleLabel("Patient Information");
        Label validationMessage = new Label();
        Label patientName = new Label();
        Label dateOfBirth = new Label();
        Label contactNo = new Label();

        // style the title and validation message labels
        validationMessage.setStyle("-fx-text-fill: red;");

        // set biding for the validation message
        validationMessageProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                validationMessage.setText("--");
                validationMessage.setVisible(false);
                return;
            }
            validationMessage.setText(newValue);
            validationMessage.setVisible(true);
        });

        // add the labels to the grid pane
        innerGridPane.addRow(0, titleLabel, validationMessage);
        innerGridPane.addRow(1, new Label("Patient Name: "), patientName);
        innerGridPane.addRow(2, new Label("Date of Birth: "), dateOfBirth);
        innerGridPane.addRow(3, new Label("Contact No: "), contactNo);
        innerGridPane.add(new Label("NOTE: Please select a patient from the table!"),0,4,2,1);

        // register the change listener on patient property
        patientProperty.addListener((observable,oldValue, newValue) -> {
            if(newValue == null) {
                patientName.setText("--");
                dateOfBirth.setText("--");
                contactNo.setText("--");
                return;
            }
            patientName.setText(newValue.getFullName());
            dateOfBirth.setText(newValue.getDob().format(Formats.DATE_FORMATTER));
            contactNo.setText(newValue.getContactNo());
        });

        return innerGridPane;
    }

    private StackPane initPatientTable() {
        StackPane pane = new StackPane();
        this.patientsTable = new PatientsTable();

        this.patientsTable.setTableRowSelectionListener((observable, oldValue, newValue) -> {
            AddConsultation.patientProperty.set(newValue);
        });

        this.patientsTable.setActionButtonOnClickListener(event -> {
            GUIApplication.primaryStage.getScene().getRoot().setDisable(true);
            new AddPatientFormPopup().showAndWait();
            this.patientsTable.populateTable();
            GUIApplication.primaryStage.getScene().getRoot().setDisable(false);
        });

        this.patientsTable.populateTable();
        pane.getChildren().add(this.patientsTable);
        return pane;
    }

    private GridPane initConsultationPane() {
        GridPane pane = createGeneralGridPane();
        Label titleLabel = createSectionTitleLabel("Consultation Information");
        Label consultationDateTimeLabel = new Label("--");
        this.notes = new CNotesInputGroup("Notes");

        // Add consultation date time
        pane.add(titleLabel, 0,0,2,1);
        pane.addRow(1,new Label("Consultation Date Time:"), consultationDateTimeLabel);
        pane.add(this.notes,0,2,2,1);

        AddConsultation.consultationDateTimeProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                consultationDateTimeLabel.setText("--");
                return;
            }

            consultationDateTimeLabel.setText(
                    LocalDate.from(newValue).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " / " + LocalTime.from(newValue).format(DateTimeFormatter.ofPattern("hh:mm a"))
            );
        });

        return pane;
    }

    private FlowPane initAddConsultationButton() {
        FlowPane pane = new FlowPane();
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.setMinWidth(250);
        // Add the Enter patient info button
        Button addConsultationButton = new Button("Add Consultation");

        addConsultationButton.setOnAction((event -> {
            try {
                validationMessageProperty.set(null);
                if( patientProperty.get() == null) throw new IllegalConsultationException("Patient not selected");
                if(doctorProperty.get() == null) throw new IllegalConsultationException("Doctor not selected");
                Consultation c = getPatientInfoData();
                if(c == null){
                    throw new IllegalConsultationException("Error creating consultation");
                }
                GUIApplication.app.manager.addConsultation(c);
                GUIApplication.app.af.navigateTo(GUIApplication.app.getMainMenu());
            } catch (Exception e) {
                validationMessageProperty.set(e.getLocalizedMessage());
                ConsoleLog.error(e.getLocalizedMessage());
            }

        }));

        pane.getChildren().add(addConsultationButton);
        return pane;
    }

    private GridPane createGeneralGridPane() {
        GridPane p = new GridPane();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(50);
        p.setVgap(5);
        p.setHgap(5);
        p.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        p.setAlignment(Pos.CENTER);
        return p;
    }

    private Label createSectionTitleLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-weight: bold;");
        return l;
    }

    private Consultation getPatientInfoData() {
        try{
            Consultation c = new Consultation(AddConsultation.patientProperty.get(), AddConsultation.doctorProperty.get(), AddConsultation.consultationDateTimeProperty.get());
            c.setTextNotes(this.notes.getTextNotes());
            c.setNoteImages(this.notes.getImageNotes());
            if(c.validateConsultation()) {
                // reset the consultation field in the Check availability page - move to caller
                CheckAvailability.doctor = null;
                CheckAvailability.consultationDateTime = null;
                return c;
            }
        } catch (IOException | NoSuchAlgorithmException | CryptoException e) {
            AlertBox.showErrorAlert("Error loading notes");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getTitle() {
        return "Patient Information | Add Consultation";
    }

    @Override
    public void onNavigation() {
        super.onNavigation();
        AddConsultation.doctorProperty.set(CheckAvailability.doctor);
        AddConsultation.consultationDateTimeProperty.set(CheckAvailability.consultationDateTime);
    }

    @Override
    public void onExit() {
        super.onExit();
        patientProperty.set(null);
        doctorProperty.set(null);
        consultationDateTimeProperty.set(null);
        validationMessageProperty.set(null);
        notes.resetFields();
        this.patientsTable.resetTableSelection();
    }
}
