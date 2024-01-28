package models;

import exceptions.IllegalConsultationException;
import main.GUIApplication;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Patient extends Person implements Serializable {
    public static String[] tableColumns = new String[] {"Patient UID." ,"Full Name", "Date of Birth", "Contact No."};
    public static String[] tableFieldNames = new String[] {"UID", "fullName", "dob", "contactNo"};
    private final String UID;

    public Patient(String name, String surname, LocalDate dob, String contactNo) {
        super(name, surname, dob, contactNo);
        this.UID = generateUID();
    }
    public Patient(String name, String surname, String dob, String contactNo) throws DateTimeParseException {
        super(name, surname, dob, contactNo);
        this.UID = generateUID();
    }

    public String getUID() {
        return UID;
    }

    private String generateUID() {
        return "p" + String.valueOf(UUID.randomUUID());
    }

    public double getConsultationRate(LocalDateTime consultationDateTime) throws IllegalConsultationException {
        final double FIRST_CONSULTATION_PRICE = 15;
        final double CONSULTATION_PRICE = 25;

        double price = FIRST_CONSULTATION_PRICE;
        // Check if current consultation is the first consultation of the patient
        List<Consultation> consultations = GUIApplication.app.manager.getConsultations().stream().filter((e) -> e.getPatient().equals(this)).collect(Collectors.toList());


            if (consultations.size() > 0) {
                price = CONSULTATION_PRICE;
            }

        return price;
    }

    @Override
    public boolean equals(Object obj) {
        // If the object is compared with itself then return true   
        if (obj == this) {
            return true;
        }

        // makes sure the passed object is an instance of class Patient
        if (!(obj instanceof Patient)) {
            return false;
        }

        // casting
        Patient p = (Patient) obj;

        // Return boolean by checking UIDs
        return this.getUID().equals(p.getUID());
    }
}