package util;

import constants.Formats;
import constants.RegExp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class Validator<T> {

    public static final Validator<String> LETTER_STRING = new Validator<String>() {
        @Override
        public boolean validate(String input) {
            return input.matches(RegExp.RegExp_LETTER_STRING);
        }
    };

    public static final Validator<String> PHONE_NUMBER_STRING = new Validator<String>() {
        @Override
        public boolean validate(String input) {
            return input.matches(RegExp.RegExp_NUMBER_STRING);
        }
    };

    public static final Validator<String> DATE_STRING = new Validator<String>() {
        @Override
        public boolean validate(String input) {
            try {
                LocalDate.parse(input, Formats.DATE_FORMAT);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                ConsoleLog.error("Invalid date format");
                return false;
            }

            return true;
        }
    };

    public static final Validator<String> DATE_TIME_STRING = new Validator<String>() {
        @Override
        public boolean validate(String input) {
            try {
                LocalDateTime.parse(input, Formats.DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                ConsoleLog.error("Invalid date time format");
                return false;
            }

            return true;
        }
    };

    public static final Validator<String> MEDICAL_LICENSE_NO_STRING = new Validator<String>() {
        @Override
        public boolean validate(String input) {
//            return input.matches(RegExp.RegExp_MEDICAL_LICENSE_NO);
            return input.matches("^[a-z]\\d{6}$"
            );
        }
    };

    public abstract boolean validate(T input);
}
