package gui.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import util.GUIValidator;

public abstract class InputGroup<T extends Node, S> extends GridPane {
    private final T inputField;
    private final Label inputLabel;
    private final Label inputValidationLabel;
    private final GridPane fieldsContainer;
    private final GUIValidator<S>[] finalValidators;

    public abstract S getInput();

    public InputGroup(String title,T inputField, GUIValidator<S>[] finalValidators) {
        this.finalValidators = finalValidators;

        this.inputLabel = new Label(title);

        // Fields
        this.inputField = inputField;
        this.inputValidationLabel = new Label();
        this.inputValidationLabel.setStyle("-fx-text-fill: #f00");
        // Create the fields container
        this.fieldsContainer = new GridPane();
        this.fieldsContainer.setVgap(1);
        this.fieldsContainer.setHgap(5);
        // Add the components to the fields container
        fieldsContainer.add(inputValidationLabel, 0,0);
        fieldsContainer.add(inputField, 0, 1);

        // Add the label and the field container to the component container
        this.add(this.inputLabel, 0, 0);
        this.add(fieldsContainer, 1, 0);
    }

    public boolean validateInput() {
        boolean isValid = false;
        for(GUIValidator<S> validator: this.getFinalValidators()) {
            isValid = validator.validate(this.getInput());

            if(!isValid  && validator.getValidationMessage() != null) {
                this.getInputValidationLabel().setText(validator.getValidationMessage());
                this.getInputValidationLabel().setVisible(true);
                break;
            } else {
                this.getInputValidationLabel().setVisible(false);
            }
        }
        return isValid;
    }

    public T getInputField() {
        return inputField;
    }

    public Label getInputValidationLabel() {
        return inputValidationLabel;
    }

    public GUIValidator<S>[] getFinalValidators() {
        return finalValidators;
    }
}
