package main;

import models.Consultation;
import models.Patient;

public interface SkinConsultationManager {
    void addDoctor();
    void removeDoctor();
    void viewDoctors();
    void addConsultation(Consultation consultation);
    void cancelConsultation(Consultation consultation);
    void addPatient(Patient patientToAdd);
    void launchGUI();
    void saveToFile();
    void loadFromFile();
}
