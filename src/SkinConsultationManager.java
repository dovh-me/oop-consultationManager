import models.Doctor;

public interface SkinConsultationManager {
    void addDoctor(Doctor d);
    void updateDoctor();
    void removeDoctor(String medLicense);
    boolean getDoctorAvailability();
    void viewDoctors();

    void addConsultation();
    void updateConsultation();
    void cancelConsultation();
    void viewConsultations();
}
