package gui.components;

import gui.models.Patient;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.GUIApplication;
import util.GUIValidator;

import java.time.LocalDate;

public class AddPatientFormPopup extends Stage {
    private CTextFieldInputGroup firstName;
    private CTextFieldInputGroup surname;
    private CDatePickerInputGroup dateOfBirthDay;
    private CTextFieldInputGroup contactNumber;
    private final VBox root;

    public AddPatientFormPopup() {
        super();
        this.root = new VBox();
        this.root.setStyle("-fx-padding: 5px;");
        initPatientInfoForm();

        this.setTitle("Add new Patient");
        this.setAlwaysOnTop(true);
        this.setScene(new Scene(this.root, 420,250));
    }

    private void initPatientInfoForm() {
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

        Button addPatientButton = new Button("Add Patient");
        addPatientButton.setOnAction(event -> this.addNewPatient());

        this.root.getChildren().addAll(
                this.gridPanelApplyStyles(this.firstName),
                this.gridPanelApplyStyles(this.surname),
                this.gridPanelApplyStyles(this.dateOfBirthDay),
                this.gridPanelApplyStyles(this.contactNumber),
                addPatientButton
        );


    }

    private GridPane gridPanelApplyStyles(GridPane p) {
        p.setVgap(5);
        p.setHgap(5);
        p.getColumnConstraints().addAll(new ColumnConstraints(200),new ColumnConstraints(200));
        p.setAlignment(Pos.CENTER);
        return p;
    }


    private boolean validateInputFields() {
        boolean isValid = this.firstName.validateInput();
        isValid = this.surname.validateInput() & isValid;
        isValid = this.contactNumber.validateInput() & isValid;
        isValid =this.dateOfBirthDay.validateInput() & isValid;
        return isValid;
    }

    private void addNewPatient() {
        if(!validateInputFields()) return;

        GUIApplication.app.manager.addPatient(
                new Patient(this.firstName.getInput(), this.surname.getInput(), this.dateOfBirthDay.getInput(), this.contactNumber.getInput())
        );
        this.close();
    }


}
