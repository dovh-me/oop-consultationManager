package gui.models;

import java.io.Serializable;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class Patient extends Person implements Serializable {
    private final String uid;

    public Patient(String name, String surname, String dob, String contactNo) throws DateTimeParseException {
        super(name, surname, dob, contactNo);
        this.uid = "p" + String.valueOf(UUID.randomUUID());
    }

    public String getUid() {
        return uid;
    }
}