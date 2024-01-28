package exceptions;

public class IllegalConsultationException extends Exception{
    public IllegalConsultationException() {
        super("An illegal operation has been performed on an incomplete/invalid consultation.");
    }

    public IllegalConsultationException(String message) {
        super(message);
    }
}
