package gui.models;

import exceptions.DailyConsultationsFullException;
import gui.components.TabularModel;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Doctor extends Person implements Serializable, TabularModel {
    public static String[] tableColumns = new String[] {"Medical License No." ,"Full Name", "Specialisation", "Contact No."};
    private String medicalLicenseNo;
    private String specialization;
    private TreeSet<Consultation> consultations = new TreeSet<>();
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
        List<Consultation> consultationList = this.findConsultationsByDate(c.getDate());

        if(consultationList.size() >= this.getPatientsPerDay()) throw new DailyConsultationsFullException(this.getPatientsPerDay());
        this.consultations.remove(c);
        this.consultations.add(c);
    }

    public List<Consultation> findConsultationsByDate(LocalDate date) {
        Stream<Consultation> consultationStream =  this.consultations.stream().filter(consultation -> {
            LocalDate dt = consultation.getDate();
            return dt.getYear() == date.getYear() && dt.getMonth() == date.getMonth() && dt.getDayOfMonth() == date.getDayOfMonth();
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

    @Override
    public String[] getTableRowData() {
        return new String[] {this.getMedicalLicenseNo() ,this.getName() + " " + this.getSurname(), this.getSpecialization(), this.getContactNo()};
    }
}
