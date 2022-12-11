package gui.components;

import util.GUIValidator;

import java.time.LocalTime;

public class CTimeFieldInputGroup extends InputGroup<CTimeField, LocalTime>{
    public CTimeFieldInputGroup(String title, GUIValidator<LocalTime>[] finalValidators) {
        super(title, new CTimeField(), finalValidators);
    }

    @Override
    public LocalTime getInput() {
        return this.getInputField().getValue();
    }
}
