package gui.components;

import util.GUIValidator;
import util.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public class TextFieldInputGroup extends JPanel implements InputGroup<String> {
    JTextField inputField;
    JLabel inputLabel;
    JLabel inputValidationLabel;
    JPanel fieldsContainer;
    GUIValidator<String>[] finalValidators;

    public TextFieldInputGroup(String title, GUIValidator<KeyEvent>[] onChangeValidators, GUIValidator<String>[] finalValidators) {
        this.setLayout(new GridLayout(1,2));
        this.finalValidators = finalValidators;

        this.inputLabel = new JLabel(title);

        // Fields
        this.inputField = new JTextField();
        this.registerOnChangeValidators(onChangeValidators);
        this.inputValidationLabel.setForeground(Color.RED);
        this.inputValidationLabel = new JLabel(title);
        // Create the fields container
        this.fieldsContainer = new JPanel(new GridLayout(2,1,5,1));
        // Add the components to the fields container
        fieldsContainer.add(inputValidationLabel);
        fieldsContainer.add(inputField);

        // Add the label and the field container to the component container
        this.add(this.inputLabel);
        this.add(fieldsContainer);
    }

    public void registerOnChangeValidators(GUIValidator<KeyEvent>[] onChangeValidators) {
        this.inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                boolean isValid;
                for(GUIValidator<KeyEvent> validator: onChangeValidators) {
                    isValid = validator.validate(e);

                    if(!isValid && validator.getValidationMessage() != null) {
                        inputValidationLabel.setText(validator.getValidationMessage());
                        inputValidationLabel.setVisible(true);
                        e.consume();
                        break;
                    } else {
                        inputValidationLabel.setVisible(false);
                    }
                }
            }
        });
    }

    @Override
    public boolean validateInput() {
        boolean isValid = false;
        for(GUIValidator<String> validator: this.finalValidators) {
            isValid = validator.validate(this.getInput());

            if(!isValid  && validator.getValidationMessage() != null) {
                inputValidationLabel.setText(validator.getValidationMessage());
                inputValidationLabel.setVisible(true);
                break;
            } else {
                inputValidationLabel.setVisible(false);
            }
        }
        return isValid;
    }

    @Override
    public String getInput() {
        return this.inputField.getText();
    }
}
