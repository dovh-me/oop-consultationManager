package gui.models;

import constants.Formats;
import gui.components.TabularModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Consultation implements Serializable, Comparable<Consultation>, TabularModel {
    public static String[] tableColumns = new String[]{"Date Time","Patient UID","Patient Name", "Doctor Name", "Cost"};

    public static String[] tableFieldNames = new String[] {"date", "patientUID", "specialization", "contactNo", "doctorName", "cost"};

    LocalDateTime consultationDateTime; // create a new constructor with this field
    LocalDate date;
    private float cost;
    private String notes;
    private Patient patient;
    private Doctor doctor;

    private LocalTime time;

    public Consultation() {
    }

    public Consultation(Patient p, Doctor d, String date, String notes) throws DateTimeParseException {
        this.patient = p;
        this.doctor = d;
        this.date = LocalDate.parse(date, Formats.DATE_FORMATTER);
        this.cost = 0;
        this.notes = notes;
    }

    public Consultation(Doctor d, LocalDateTime consultationDateTime) {
        this.setDoctor(d);
        this.consultationDateTime = consultationDateTime;
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
        return ldt.format(Formats.DATE_TIME_FORMATTER);
    }

    public String getPatientUID() {
        return this.patient.getUid();
    }

    public String getDoctorName() {
        return this.getDoctor().getFullName();
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDateTime getConsultationDateTime() {
        return consultationDateTime;
    }

    @Override
    public int compareTo(Consultation o) {
        return this.getDate().compareTo(o.getDate());
    }

    @Override
    public String[] getTableColumnNames() {
        return new String[]{"Date Time","Patient UID","Patient Name", "Doctor Name", "Cost"};
    }

    @Override
    public String[] getTableRowData() {
        return new String[]{date.format(Formats.DATE_FORMATTER), patient.getUid(),patient.getName() + patient.getSurname(), doctor.getName() + doctor.getSurname(),Float.toString(cost) };
    }
}
