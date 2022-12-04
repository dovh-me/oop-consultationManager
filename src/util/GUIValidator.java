package util;

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

    public static final GUIValidator<String> IS_EMPTY = new GUIValidator<String>("Field value cannot be empty.") {
        @Override
        public boolean validate(String input) {
            return input.isEmpty();
        }
    };

    public static final GUIValidator<String> MEDICAL_LICENSE_NO = new GUIValidator<String>("Invalid Medical License number.") {
        @Override
        public boolean validate(String input) {
            return Validator.MEDICAL_LICENSE_NO_STRING.validate(input);
        }
    };

    public static final GUIValidator<String> DATE_TIME_STRING = new GUIValidator<String>("Invalid date time string. Please follow the format (yyyy-MM-dd HH:mm).") {
        @Override
        public boolean validate(String input) {
            return Validator.DATE_TIME_STRING.validate(input);
        }
    };
}
