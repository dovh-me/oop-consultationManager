package gui.components;

import constants.Formats;
import exceptions.CryptoException;
import exceptions.IllegalConsultationException;
import gui.models.Consultation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.AlertBox;
import util.ConsoleLog;
import util.GUIValidator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UpdateConsultationPopup extends Stage {
    private final Consultation consultation;
    private Label costLabel;
    private CTextFieldInputGroup calculateCost;
    private CNotesInputGroup notes;

    public UpdateConsultationPopup(Consultation consultationToUpdate) {
        super();
        this.consultation = consultationToUpdate;
        VBox root = new VBox();
        root.setStyle("-fx-padding: 5px;");
        root.getChildren().addAll(
                initUpdateConsultationForm(),
                initActionButtonPane()
        );

        this.setTitle("Update Consultation | " + this.consultation.getConsultationUID());
        this.setAlwaysOnTop(true);
        this.setResizable(true);
        this.setScene(new Scene(root, 700,600));
    }

    private GridPane initUpdateConsultationForm() {
        GridPane pane = new GridPane();
        Label titleLabel = new Label("Update Consultation");
        this.costLabel = new Label(Float.toString(this.consultation.getCost()));
        this.calculateCost = new CTextFieldInputGroup("Calculate Cost", new GUIValidator[]{ GUIValidator.NUMBERS_ONLY }, new GUIValidator[]{});
        this.notes = new CNotesInputGroup("Notes");
        try {
            this.notes.setImageNotes(this.consultation.getNoteImages());
            this.notes.setTextNotes(this.consultation.getTextNotes());
        } catch (NoSuchAlgorithmException | IOException | CryptoException e) {
            AlertBox.showErrorAlert("There was an error loading the consultation notes/content");
        }

        titleLabel.setStyle("-fx-font-weight: bold;");
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setMaxWidth(280);
        columnConstraints.setMinWidth(280);
        pane.setVgap(10);
        pane.setHgap(10);
        pane.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        this.notes.getColumnConstraints().removeIf((e) -> true);
        this.notes.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        this.calculateCost.getColumnConstraints().addAll(columnConstraints, columnConstraints, new ColumnConstraints(100));

        this.calculateCost.getInputField().setPromptText("No. of hours");
        Button calculateCostButton = new Button("Get Cost");
        calculateCostButton.setOnAction(event -> {
            try {
                if(!validateInputs()) return;
                double hourlyRate = consultation.getHourlyCost();
                int cost = this.calculateCost.getInput().isEmpty()? 0: Integer.parseInt(this.calculateCost.getInput());
                this.costLabel.setText(String.valueOf(hourlyRate * cost));
            } catch (IllegalConsultationException e) {
                ConsoleLog.error(e.getLocalizedMessage());
                AlertBox.showErrorAlert(e.getLocalizedMessage());
            }
        });
        this.calculateCost.add(calculateCostButton,2,1);

        pane.add(titleLabel,0,0,2,1);
        pane.addRow(1, new Label("Consultation Date/Time: "), new Label(this.consultation.getConsultationDateTime().format(Formats.DATE_TIME_OUTPUT_FORMAT)));
        pane.addRow(2, new Label("Cost"), this.costLabel);
        pane.add(this.calculateCost,0,3,2,1);
        pane.add(this.notes, 0,4,2,1);

        return pane;
    }

    private FlowPane initActionButtonPane() {
        FlowPane pane = new FlowPane();
        pane.setAlignment(Pos.CENTER_RIGHT);

        Button cancelButton = new Button("Cancel");
        Button saveChangesButton = new Button("Save Changes");

        cancelButton.setOnAction(event -> this.close());

        saveChangesButton.setOnAction(event -> {
            try {
                // inputs are not validated again

                // using wrapper classes to pass null to the update function if required
                // getting the value from the cost label
                Float cost = Float.parseFloat(this.costLabel.getText());

                // updating hte consultation object alone will update the
                // consultation details in all places as data structures only hold the reference of the objects
                this.consultation.updateConsultation(cost,this.notes.getTextNotes(), this.notes.getImageNotes());
                // manually updating the consultation in doctors (since the tree needs to be kept sorted although consultation times cannot be updated as for now)
                this.consultation.getDoctor().updateConsultation(this.consultation);
                this.close();
                AlertBox.showInformationAlert("Successfully updated the consultation");
            } catch (IllegalConsultationException e) {
                ConsoleLog.error(e.getLocalizedMessage());
                AlertBox.showErrorAlert(e.getLocalizedMessage());
            }
        });

        pane.getChildren().addAll(
                saveChangesButton,
                cancelButton
        );

        return pane;
    }

    private boolean validateInputs() {
        // notes inputs are already validated internally
        return  this.calculateCost.validateInput();
    }
}
