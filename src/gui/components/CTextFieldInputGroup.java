package gui.components;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import util.Validator;

public class CTextFieldInputGroup extends InputGroup<TextField, String> {

    public CTextFieldInputGroup(String title, Validator<KeyEvent>[] onChangeValidators, Validator<String>[] finalValidators) {
        super(title, new TextField(), finalValidators);
        // Registering on change validators
        // TODO: handle with TextFormatter class
        this.registerOnChangeValidators(onChangeValidators);
    }

    public void registerOnChangeValidators(Validator<KeyEvent>[] onChangeValidators) {
        this.getInputField().setOnKeyTyped(event -> {
            boolean isValid;
            for(Validator<KeyEvent> validator: onChangeValidators) {
                isValid = validator.validate(event);

                if(!isValid) {
                    event.consume();
                    break;
                }
            }
        });
    }

    @Override
    public String getInput() {
        return this.getInputField().getText();
    }
}
