import gui.models.Doctor;

import java.time.LocalDateTime;

public interface SkinConsultationManager {
    void addDoctor();
    void updateDoctor();
    void removeDoctor();
    boolean getDoctorAvailability(Doctor doctor, LocalDateTime ldt);
    Doctor getAvailableDoctor(String specialization, String dateTime);
    void viewDoctors();

    void addConsultation();
    void updateConsultation();
    void cancelConsultation();
    void viewConsultations();
}
