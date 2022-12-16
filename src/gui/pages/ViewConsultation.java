package gui.pages;

import constants.Formats;
import exceptions.CryptoException;
import gui.components.CVerticalImageGallery;
import gui.components.Page;
import gui.models.Consultation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import main.GUIApplication;
import util.AlertBox;
import util.ConsoleLog;
import util.CryptoUtils;
import util.GUIValidator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ViewConsultation extends Page {
    public static Consultation consultation;

    private final Label doctorLbl;
    private final Label patientLbl;
    private final Label consultationDateTime;

    private final Label dateOfBirthLbl;
    private final Label contactNoLbl;
    private final TextArea textNotesArea;
    private final CVerticalImageGallery noteImages;

    public ViewConsultation() {
        doctorLbl = new Label();
        patientLbl = new Label();
        consultationDateTime = new Label();
        dateOfBirthLbl = new Label();
        contactNoLbl = new Label();
        textNotesArea = new TextArea();
        noteImages = new CVerticalImageGallery();
        VBox mainPane = new VBox();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setSpacing(10);

        mainPane.getChildren().add(new Label("Summary"));
        GridPane doctorInfoPane = new GridPane();
        doctorInfoPane.add(new Label("Doctor Name/Med Lic No."), 0,0);
        doctorInfoPane.add(doctorLbl, 1,0);
        doctorInfoPane.add(new Label("Patient Name/Patient UID"), 0,1);
        doctorInfoPane.add(patientLbl, 1,1);
        doctorInfoPane.add(new Label("Consultation Date Time"), 0,2);
        doctorInfoPane.add(consultationDateTime, 1,2);
        doctorInfoPane.setVgap(10);
        doctorInfoPane.setAlignment(Pos.CENTER);
        mainPane.getChildren().add(doctorInfoPane);

        GridPane patientInfoPane = new GridPane();
        ScrollPane patientScrollableWrapper = new ScrollPane();
        patientInfoPane.add(new Label("Date Of Birth"), 0,0);
        patientInfoPane.add(dateOfBirthLbl, 1,0);
        patientInfoPane.add(new Label("Contact No"), 0,1);
        patientInfoPane.add(contactNoLbl, 1,1);
        patientInfoPane.add(new Label("Notes"), 0,2);
        patientInfoPane.add(createGallery(), 1,2);
        patientInfoPane.setVgap(10);
        patientInfoPane.getRowConstraints().add(new RowConstraints(Control.USE_COMPUTED_SIZE));
        patientInfoPane.setAlignment(Pos.CENTER);
        patientScrollableWrapper.setContent(patientInfoPane);
        patientScrollableWrapper.setHmax(800);
        mainPane.getChildren().add(patientScrollableWrapper);

        mainPane.getChildren().add(createActionPane());
        this.getChildren().add(mainPane);
    }

    public void loadConsultation(Consultation consultation) {
        doctorLbl.setText(String.format("%s / %s",consultation.getDoctor().getFullName(), consultation.getDoctor().getMedicalLicenseNo()));
        patientLbl.setText(String.format("%s / %s", consultation.getPatient().getFullName(), consultation.getPatient().getUid()));
        consultationDateTime.setText(consultation.getConsultationDateTime().format(Formats.DATE_TIME_OUTPUT_FORMAT));

        dateOfBirthLbl.setText(consultation.getPatient().getDob().format(Formats.DATE_FORMATTER));
        contactNoLbl.setText(consultation.getPatient().getContactNo());
        try {
            textNotesArea.setText(consultation.getTextNotes());
            noteImages.loadContent(consultation.getNoteImages());
        } catch (NoSuchAlgorithmException | IOException | CryptoException e) {
            ConsoleLog.error("There was an error loading notes");
            e.printStackTrace();
        }
    }

    private HBox createActionPane() {
        HBox pane = new HBox();
        pane.setAlignment(Pos.CENTER_RIGHT);

        Button updateConsultation = new Button("Update Consultation");
        Button cancelConsultation =  new Button("Cancel Consultation");

        updateConsultation.setOnAction((actionEvent) -> {});
        cancelConsultation.setOnAction((actionEvent) -> {});

        pane.getChildren().addAll(updateConsultation, cancelConsultation);
        return pane;
    }

    private VBox createGallery() {
        VBox pane = new VBox();
        pane.setSpacing(5);
        pane.getChildren().add(textNotesArea);
        pane.getChildren().add(noteImages);

        return pane;
    }

    @Override
    public String getTitle() {
        return "View Consultation";
    }

    @Override
    public void onNavigation() {
        if(ViewConsultation.consultation == null || !ViewConsultation.consultation.validateConsultation()) {
            AlertBox.showErrorAlert("There was an error loading the consultation info. Navigating back to main page!");
            GUIApplication.app.af.navigateTo(GUIApplication.app.getMainMenu());
            return;
        }

        loadConsultation(ViewConsultation.consultation);
        super.onNavigation();
    }
}
