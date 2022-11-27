package exceptions;

public class DailyConsultationsFullException extends Exception{
    public DailyConsultationsFullException(int dailyConsultationLimit) {
        super(String.format("Daily consultation limit of %d has reached. Please pick a different date!", dailyConsultationLimit));
    }

    public DailyConsultationsFullException(String message) {
        super(message);
    }
}
