package gui.models;

import exceptions.DailyConsultationsFullException;
import util.ConsoleLog;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Doctor extends Person implements Serializable {
    private String medicalLicenseNo;
    private String specialization;
    private TreeSet<Consultation> consultations;
    private int patientsPerDay = 5;
    private ArrayList<DayOfWeek> availableDays = new ArrayList<>(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY,DayOfWeek.SATURDAY));
    private LocalTime consultationStart = LocalTime.of(17,0);

    public Doctor(String name, String surname, String dob, String contactNo) throws DateTimeParseException {
        super(name, surname, dob, contactNo);
    }

    public Doctor(String name, String surname, String dob, String contactNo, String medicalLicenseNo, String specialization) throws DateTimeParseException {
        super(name, surname, dob, contactNo);
        this.medicalLicenseNo = medicalLicenseNo;
        this.specialization = specialization;
    }

    public void addConsultation(Consultation c) throws DailyConsultationsFullException {
        List<Consultation> consultationList = this.findConsultationsByDate(c.getDateTime());

        if(consultationList.size() >= this.getPatientsPerDay()) throw new DailyConsultationsFullException(this.getPatientsPerDay());
        this.consultations.remove(c);
        this.consultations.add(c);
    }

    public List<Consultation> findConsultationsByDate(LocalDateTime dateTime) {
        Stream<Consultation> consultationStream =  this.consultations.stream().filter(consultation -> {
            LocalDateTime dt = consultation.getDateTime();
            return dt.getYear() == dateTime.getDayOfYear() && dt.getMonth() == dateTime.getMonth() && dt.getDayOfMonth() == dateTime.getDayOfMonth();
        });

        return consultationStream.collect(Collectors.toList());
    }

    // TODO: Throw error if the object being updated does not exist in the set?
    public void updateConsultation(Consultation c) {
        // Removes the object if already present in the set
        this.consultations.remove(c);
        // Adds back the updated object to the set. This is done to make sure the set is sorted at all times
        this.consultations.add(c);
    }

    public String getMedicalLicenseNo() {
        return medicalLicenseNo;
    }

    public void setMedicalLicenseNo(String medicalLicenseNo) {
        this.medicalLicenseNo = medicalLicenseNo;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public TreeSet<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(TreeSet<Consultation> consultations) {
        this.consultations = consultations;
    }

    public int getPatientsPerDay() {
        return patientsPerDay;
    }

    public void setPatientsPerDay(int patientsPerDay) {
        this.patientsPerDay = patientsPerDay;
    }

    public ArrayList<DayOfWeek> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(ArrayList<DayOfWeek> availableDays) {
        this.availableDays = availableDays;
    }

    public LocalTime getConsultationStart() {
        return consultationStart;
    }

    public void setConsultationStart(LocalTime consultationStart) {
        this.consultationStart = consultationStart;
    }
}
