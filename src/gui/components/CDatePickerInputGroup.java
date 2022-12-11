package gui.components;

import util.GUIValidator;

import java.time.LocalDate;

public class CDatePickerInputGroup extends InputGroup<CDatePicker, LocalDate> {

    public CDatePickerInputGroup(String title, GUIValidator<LocalDate>[] finalValidators) {
        super(title, new CDatePicker(), finalValidators);
    }

    @Override
    public LocalDate getInput() {
        return this.getInputField().getValue();
    }
}
