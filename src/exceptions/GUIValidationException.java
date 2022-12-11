package exceptions;

public class GUIValidationException extends Exception{

    public GUIValidationException(int dailyConsultationLimit) {
        super(String.format("Daily consultation limit of %d has reached. Please pick a different date!", dailyConsultationLimit));
    }

    public GUIValidationException(String message) {
        super(message);
    }
}
