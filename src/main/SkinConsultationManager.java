package main;

import gui.models.Doctor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SkinConsultationManager {
    void addDoctor();
    void updateDoctor();
    void removeDoctor();
    boolean getDoctorAvailability(Doctor doctor, LocalDate ldt);
    Doctor getAvailableDoctor(String specialization, String dateTime);
    void viewDoctors();

    void addConsultation();
    void updateConsultation();
    void cancelConsultation();
    void viewConsultations();
}
