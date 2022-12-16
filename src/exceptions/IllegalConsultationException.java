package exceptions;

public class IllegalConsultationException extends Exception{
    public IllegalConsultationException() {
        super("An operation has been performed on an incomplete consultation. Please make sure all the fields are populated");
    }

    public IllegalConsultationException(String message) {
        super(message);
    }
}
