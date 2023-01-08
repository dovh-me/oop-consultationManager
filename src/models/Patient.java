package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

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