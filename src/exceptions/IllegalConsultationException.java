package exceptions;

public class IllegalConsultationException extends Exception{
    public IllegalConsultationException() {
        super(String.format("Consultation you are trying to add is illegal. Please check the consultation parameters"));
    }

    public IllegalConsultationException(String message) {
        super(message);
    }
}
