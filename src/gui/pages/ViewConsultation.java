package gui.pages;

import exceptions.CryptoException;
import gui.components.CVerticalImageGallery;
import gui.components.Page;
import gui.components.UpdateConsultationPopup;
import javafx.geometry.Insets;
import models.Consultation;
import models.Doctor;
import models.Patient;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import main.GUIApplication;
import util.AlertBox;
import util.ConsoleLog;
import util.CryptoUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class ViewConsultation extends Page {
    public static SimpleObjectProperty<Consultation> consultationSimpleObjectProperty = new SimpleObjectProperty<>();

    public ViewConsultation() {
        GridPane mainPane = createGeneralGridPane();
        mainPane.setVgap(10);
        mainPane.setHgap(5);
        mainPane.setStyle("-fx-padding: 10px;");

        mainPane.add(initPatientPane(),0,0);
        mainPane.add(initDoctorPane(),1,0);
        mainPane.add(initConsultationPane(),0,1,2,1);
        mainPane.add(createActionPane(),0,2,2,1);

        this.setPadding(new Insets(5));
        this.getChildren().add(mainPane);
    }
    private GridPane initDoctorPane() {
        GridPane pane = this.createGeneralGridPane();

        Label doctorName = new Label("--");
        Label medicalLicense = new Label("--");
        Label specialisation = new Label("--");

        pane.add(createSectionTitleLabel("Doctor Information"),0,0,2,1);
        pane.addRow(1, new Label("Doctor Name:"), doctorName);
        pane.addRow(2, new Label("Medical License No:"), medicalLicense);
        pane.addRow(3, new Label("Specialisation:"), specialisation);
        pane.add(new Separator(Orientation.HORIZONTAL),0,4,2,1);

        // add the binding
        ViewConsultation.consultationSimpleObjectProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue == null || newValue.getDoctor() == null){
                doctorName.setText("--");
                medicalLicense.setText("--");
                specialisation.setText("--");
                return;
            }
            Doctor d = newValue.getDoctor();
            doctorName.setText("Dr. " + d.getFullName());
            medicalLicense.setText(d.getMedicalLicenceNo());
            specialisation.setText(d.getSpecialization());

        });

        return pane;
    }

    private GridPane initPatientPane() {
        GridPane pane = this.createGeneralGridPane();

        Label patientUID = new Label("--");
        Label patientName = new Label("--");
        Label patientContactNo = new Label("--");

        pane.add(createSectionTitleLabel("Patient Information"),0,0,2,1);
        pane.addRow(1, new Label("Patient UID: "), patientUID);
        pane.addRow(2, new Label("Patient Name; "), patientName);
        pane.addRow(3, new Label("Contact No: "), patientContactNo);
        pane.add(new Separator(Orientation.HORIZONTAL),0,4,2,1);

        // add the binding
        ViewConsultation.consultationSimpleObjectProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue == null || newValue.getPatient() == null){
                patientUID.setText("--");
                patientName.setText("--");
                patientContactNo.setText("--");
                return;
            }
            Patient p = newValue.getPatient();
            patientUID.setText(p.getUID());
            patientName.setText(p.getFullName());
            patientContactNo.setText(p.getContactNo());

        });

        return pane;
    }

    private GridPane initConsultationPane() {
        GridPane consultationPane = createGeneralGridPane();
        GridPane summaryFieldsPane = createGeneralGridPane();
        ScrollPane scrollableWrapper = new ScrollPane();
        Label costLabel = new Label("0.00");
        Label consultationDateTime = new Label("--");
        TextArea textNotes = new TextArea("No notes");
        CVerticalImageGallery noteImages = new CVerticalImageGallery();

        scrollableWrapper.setContent(noteImages);
        scrollableWrapper.setHmax(800);
        consultationPane.setHgap(5);
        textNotes.setEditable(false);

        consultationPane.add(createSectionTitleLabel("Consultation Information"), 0,0,2,1);
        summaryFieldsPane.addRow(0,new Label("Consultation Cost: "), costLabel);
        summaryFieldsPane.addRow(1,new Label("Consultation Date/Time: "), consultationDateTime);
        summaryFieldsPane.addRow(2,new Label("Consultation Notes: "), textNotes);

        consultationPane.add(summaryFieldsPane, 0,1);
        consultationPane.add(scrollableWrapper, 1,1);
        consultationPane.add(new Separator(Orientation.HORIZONTAL),0,2,2,1);

        // add the binding
        ViewConsultation.consultationSimpleObjectProperty.addListener((observable, oldValue, newValue) -> {
            try {
                if(newValue == null) {
                    costLabel.setText("0.00");
                    consultationDateTime.setText("--");
                    textNotes.setText("--");
                    noteImages.loadContent(Collections.emptyList());
                    return;
                }
                Consultation c = newValue;
                String notesString = c.getTextNotes();

                costLabel.setText(Float.toString(c.getCost()));
                consultationDateTime.setText(c.getConsultationDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd / kk:mm a")));
                textNotes.setText(notesString.isEmpty()? "--No Notes--": notesString);
                noteImages.loadContent(c.getNoteImageBytes());
            } catch (NoSuchAlgorithmException | IOException | CryptoException e) {
                String errorMessage = "There was an error loading notes. Please try again.";
                ConsoleLog.error(errorMessage + "\n" +  e.getLocalizedMessage());
                AlertBox.showErrorAlert(errorMessage);
                GUIApplication.app.af.navigateTo(GUIApplication.app.getMainMenu());
            } catch (Exception e) {
                String errorMessage = "Unexpected error occurred please report an issue";
                ConsoleLog.error(errorMessage + "\n" +  e.getLocalizedMessage());
                AlertBox.showErrorAlert(errorMessage);
                GUIApplication.app.af.navigateTo(GUIApplication.app.getMainMenu());
            }
        });

        return consultationPane;
    }

    private GridPane createGeneralGridPane() {
        GridPane pane = new GridPane();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(50);
        pane.setVgap(10);
        pane.getColumnConstraints().addAll(columnConstraints,columnConstraints);
        return pane;
    }

    private HBox createActionPane() {
        HBox pane = new HBox();
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.setSpacing(5);

        Button backButton = new Button("Back");
        Button updateConsultation = new Button("Update Consultation");
        Button cancelConsultation =  new Button("Cancel Consultation");


        updateConsultation.setOnAction((actionEvent) -> {
            GUIApplication.primaryStage.getScene().getRoot().setDisable(true);
            new UpdateConsultationPopup(ViewConsultation.consultationSimpleObjectProperty.get()).showAndWait();
            GUIApplication.primaryStage.getScene().getRoot().setDisable(false);
            Consultation c = ViewConsultation.consultationSimpleObjectProperty.get();
            ViewConsultation.consultationSimpleObjectProperty.set(null);
            ViewConsultation.consultationSimpleObjectProperty.set(c);
        });
        cancelConsultation.setStyle("-fx-background-color: #f00; -fx-text-fill: #fff;");
        cancelConsultation.setOnAction((actionEvent) -> {
            GUIApplication.app.manager.cancelConsultation(
                    ViewConsultation.consultationSimpleObjectProperty.get()
            );
            GUIApplication.app.af.navigateToPrev();
        });
        backButton.setDefaultButton(true);
        backButton.setOnAction((actionEvent) -> {
            GUIApplication.app.af.navigateToPrev();
        });

        pane.getChildren().addAll(updateConsultation, cancelConsultation);
        return pane;
    }

    private Label createSectionTitleLabel(String labelText) {
        Label  l = new Label(labelText);
        l.setStyle("-fx-font-weight: bold;");
        return  l;
    }

    @Override
    public String getTitle() {
        return "View Consultation";
    }

    @Override
    public void onNavigation() {
        if(ViewConsultation.consultationSimpleObjectProperty.get() == null || !ViewConsultation.consultationSimpleObjectProperty.get().validateConsultation()) {
            AlertBox.showErrorAlert("There was an error loading the consultation info. Navigating back to main page!");
            GUIApplication.app.af.navigateTo(GUIApplication.app.getMainMenu());
            return;
        }
        super.onNavigation();
    }

    @Override
    public void onExit() {
        super.onExit();
        CryptoUtils.clearEncryptedFiles();
    }
}
