package gui.models;

import constants.Formats;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Consultation implements Serializable, Comparable<Consultation> {
    LocalDate date;
    private float cost;
    private String notes;
    private Patient patient;
    private Doctor doctor;

    public Consultation() {
    }

    public Consultation(Patient p, Doctor d, String date, String notes) throws DateTimeParseException {
        this.patient = p;
        this.doctor = d;
        this.date = LocalDate.parse(date, Formats.DATE_FORMAT);
        this.cost = 0;
        this.notes = notes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getApproximateDateTime() {
        List<Consultation> consultationList = this.getDoctor().findConsultationsByDate(this.getDate());
        int indexOfCurrent = consultationList.indexOf(this);
        LocalDateTime ldt = LocalDateTime.of(this.getDate(),
                LocalTime.of(17,0).plusMinutes(30 * (indexOfCurrent + 1)));
        return ldt.format(Formats.DATE_TIME_FORMAT);
    }

    @Override
    public int compareTo(Consultation o) {
        return this.getDate().compareTo(o.getDate());
    }

}
