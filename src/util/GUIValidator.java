package util;

import constants.RegExp;

public abstract class GUIValidator <T>{
    private final String validationMessage;
    public GUIValidator (String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public GUIValidator() { this.validationMessage = null; }

    public String getValidationMessage() {
        return validationMessage;
    }

    public abstract boolean validate(T input);

    public static final GUIValidator<javafx.scene.input.KeyEvent> NUMBERS_ONLY = new GUIValidator<javafx.scene.input.KeyEvent>() {
        @Override
        public boolean validate(javafx.scene.input.KeyEvent input) {
            return input.getCharacter().matches(RegExp.RegExp_NUMBER_STRING);
        }
    };

    public static final GUIValidator<String> NOT_EMPTY = new GUIValidator<String>("Field value cannot be empty.") {
        @Override
        public boolean validate(String input) {
            return !input.isEmpty();
        }
    };

    public static final GUIValidator<String> MEDICAL_LICENSE_NO = new GUIValidator<String>("Invalid Medical License number.") {
        @Override
        public boolean validate(String input) {
            return Validator.MEDICAL_LICENSE_NO_STRING.validate(input);
        }
    };

    public static final GUIValidator<Object> NOT_NULL = new GUIValidator<Object>("A value is required for this field") {
        @Override
        public boolean validate(Object input) {
            return input != null;
        }
    };

    public static final GUIValidator<String> PHONE_NUMBER = new GUIValidator<String>("Invalid Phone number") {
        @Override
        public boolean validate(String input) {
            return Validator.PHONE_NUMBER_STRING.validate(input);
        }
    };

    public static final GUIValidator<String> LETTERS_ONLY = new GUIValidator<String>() {
        @Override
        public boolean validate(String input) {
            return Validator.LETTER_STRING.validate(input);
        }
    };
}
