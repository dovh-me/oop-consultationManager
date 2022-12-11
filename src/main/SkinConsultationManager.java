package main;

import gui.models.Doctor;

import java.time.LocalDateTime;
import java.util.List;

public interface SkinConsultationManager {
    void addDoctor();
    void updateDoctor();
    void removeDoctor();
    Doctor getAvailableDoctor(String specialization, LocalDateTime dateTime);
    void viewDoctors();

    void addConsultation();
    void updateConsultation();
    void cancelConsultation();
    void viewConsultations();
}
