package models;

import java.io.Serializable;
import java.util.Date;

public class Patient extends Person implements Serializable {
    private String uid;

    public Patient(String uid) {
        this.uid = uid;
    }

    public Patient(String name, String surname, String dob, String contactNo, String uid) {
        super(name, surname, dob, contactNo);
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
