package gui.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class Patient extends Person implements Serializable {
    public static String[] tableColumns = new String[] {"Patient UID." ,"Full Name", "Date of Birth", "Contact No."};
    public static String[] tableFieldNames = new String[] {"uid", "fullName", "dob", "contactNo"};
    private final String uid;

    public Patient(String name, String surname, LocalDate dob, String contactNo) {
        super(name, surname, dob, contactNo);
        this.uid = generateUID();
    }
    public Patient(String name, String surname, String dob, String contactNo) throws DateTimeParseException {
        super(name, surname, dob, contactNo);
        this.uid = generateUID();
    }

    public String getUid() {
        return uid;
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
        return this.getUid().equals(p.getUid());
    }
}