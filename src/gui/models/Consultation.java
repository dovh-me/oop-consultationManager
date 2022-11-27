package gui.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Consultation implements Serializable, Comparable<Consultation> {
    LocalDateTime dateTime;
    private float cost;
    private String notes;
    private Patient patient;
    private Doctor doctor;

    public Consultation() {
    }

    public Consultation(Patient p, Doctor d,String dateTime, String notes) throws DateTimeParseException {
        this.patient = p;
        this.doctor = d;
        this.dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.cost = 0;
        this.notes = notes;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int compareTo(Consultation o) {
        return this.getDateTime().compareTo(o.getDateTime());
    }
}
