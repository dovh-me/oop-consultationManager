package util;

import constants.Formats;
import constants.RegExp;
import javafx.scene.input.KeyEvent;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public abstract class Validator<T>{
    private final String validationMessage;
    public Validator(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public Validator() { this.validationMessage = null; }

    public String getValidationMessage() {
        return validationMessage;
    }

    public abstract boolean validate(T input);

    public static final Validator<KeyEvent> NUMBERS_ONLY = new Validator<KeyEvent>() {
        @Override
        public boolean validate(javafx.scene.input.KeyEvent input) {
            return input.getCharacter().matches(RegExp.RegExp_NUMBER_STRING);
        }
    };

    public static final Validator<String> ENGLISH_LETTERS_ONLY = new Validator<String>("Field value should only consist of english letters") {
        @Override
        public boolean validate(String input) {
            return input.matches(RegExp.RegExp_LETTER_STRING);
        }
    };

    public static final Validator<String> NOT_EMPTY = new Validator<String>("Field value cannot be empty.") {
        @Override
        public boolean validate(String input) {
            return !input.isEmpty();
        }
    };

    public static final Validator<String> MEDICAL_LICENSE_NO = new Validator<String>("Invalid Medical License number.") {
        @Override
        public boolean validate(String input) {
            return input.matches(RegExp.RegExp_MEDICAL_LICENSE_NO);
        }
    };

    public static final Validator<Object> NOT_NULL = new Validator<Object>("A value is required for this field") {
        @Override
        public boolean validate(Object input) {
            return input != null;
        }
    };

    public static final Validator<String> PHONE_NUMBER = new Validator<String>("Invalid Phone number") {
        @Override
        public boolean validate(String input) {
            return input.matches(RegExp.RegExp_NUMBER_STRING) && input.length() == 10;
        }
    };

    public static final Validator<String> DATE_STRING = new Validator<String>("Invalid date string. Does not match the format") {
        @Override
        public boolean validate(String input) {
            try {
                LocalDate.parse(input, Formats.DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                return false;
            }

            return true;
        }
    };
}
