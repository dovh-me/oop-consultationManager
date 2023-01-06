package main;

import models.Consultation;
import models.Doctor;

import java.time.LocalDateTime;

public interface SkinConsultationManager {
    void addDoctor();
    void updateDoctor();
    void removeDoctor();
    Doctor getAvailableDoctor(String specialization, LocalDateTime dateTime);
    void viewDoctors();

    void addConsultation(Consultation consultation);
    void cancelConsultation(Consultation consultation);
}
