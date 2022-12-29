package gui.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import util.GUIValidator;

public abstract class InputGroup<T extends Node, S> extends GridPane {
    private final T inputField;
    private final Label inputValidationLabel;
    private final GUIValidator<S>[] finalValidators;

    public abstract S getInput();

    public InputGroup(String title,T inputField, GUIValidator<S>[] finalValidators) {
        this.finalValidators = finalValidators;

        Label inputLabel = new Label(title);

        // Fields
        this.inputField = inputField;
        this.inputValidationLabel = new Label();
        this.inputValidationLabel.setStyle("-fx-text-fill: #f00");
        // Add the label and the field container to the component container
        this.add(inputLabel, 0, 0);
        this.add(inputValidationLabel, 1, 0);
        this.add(inputField, 1, 1);
    }

    public boolean validateInput() {
        boolean isValid = true;
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

    public void setGroupPaneColumnConstraints(ColumnConstraints ...constraints) {
        this.getColumnConstraints().addAll(constraints);
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
