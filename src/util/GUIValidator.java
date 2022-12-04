package util;

public abstract class GUIValidator <T>{
    private final String validationMessage;
    public GUIValidator (String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public abstract boolean validate(T input);

    public static final GUIValidator<String> MEDICAL_LICENSE_NO = new GUIValidator<String>("Invalid Medical License number.") {
        @Override
        public boolean validate(String input) {
            return Validator.MEDICAL_LICENSE_NO_STRING.validate(input);
        }
    };
}
