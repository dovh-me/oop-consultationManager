package models;

import exceptions.IllegalConsultationException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Doctor extends Person implements Serializable {
    public static String[] tableColumns = new String[] {"Medical Licence No." ,"Full Name", "Specialisation", "Contact No.", "Available From(24h)", "Available To(24h)"};
    public static String[] tableFieldNames = new String[] {"medicalLicenceNo", "fullName", "specialization", "contactNo", "consultationStart", "consultationEnd"};
    private String medicalLicenceNo;
    private String specialization;
    private TreeSet<Consultation> consultations = new TreeSet<>();
    private LocalTime consultationStart = LocalTime.of(8,0);
    private LocalTime consultationEnd = LocalTime.of(21, 0);

    public Doctor(String name, String surname, String dob, String contactNo) throws DateTimeParseException {
        super(name, surname, dob, contactNo);
    }

    public Doctor(String name, String surname, String dob, String contactNo, String medicalLicenceNo, String specialization) throws DateTimeParseException {
        super(name, surname, dob, contactNo);
        this.medicalLicenceNo = medicalLicenceNo;
        this.specialization = specialization;
    }

    public void addConsultation(Consultation c) throws IllegalConsultationException {
        // Don't allow consultations with date times before the time at which the consultation is being created
        if(c.getConsultationDateTime().isBefore(LocalDateTime.now())) {
            String errorMessage = "Illegal operation. Consultations can only be booked for future dates and times";
            throw new IllegalConsultationException(errorMessage);
        }
        this.consultations.remove(c);
        this.consultations.add(c);
    }

    public List<Consultation> findConsultationsByDate(LocalDate date) {
        Stream<Consultation> consultationStream =  this.consultations.stream().filter(consultation -> {
            LocalDate dt = LocalDate.from(consultation.getConsultationDateTime());
            return dt.getYear() == date.getYear() && dt.getMonth() == date.getMonth() && dt.getDayOfMonth() == date.getDayOfMonth();
        });

        return consultationStream.collect(Collectors.toList());
    }

    public void updateConsultation(Consultation c) {
        // Removes the object if already present in the set
        this.consultations.remove(c);
        // Adds back the updated object to the set. This is done to make sure the set is sorted at all times
        this.consultations.add(c);
    }

    public boolean getAvailability(LocalDateTime ldt) {
        List<Consultation> consultations = this.findConsultationsByDate(LocalDate.from(ldt));

        // Return false if the time slot is before the current time
        if(LocalDateTime.now().isAfter(ldt)) return false;
        // Return false if the time slot is not within the consultation times
        if(this.consultationEnd.isBefore(LocalTime.from(ldt))) return false;
        if(this.consultationStart.isAfter(LocalTime.from(ldt))) return false;

        // Check if the new consultation is clashing with existing ones
        for (Consultation consultation : consultations) {
            LocalDateTime currDateTime = LocalDateTime.of(LocalDate.from(consultation.getConsultationDateTime()), LocalTime.from(consultation.getConsultationDateTime()));
            // Return false if consultation being checked is not one hour before or after the current consultation being placed
            if(currDateTime.isEqual(ldt)) return false;
            if(currDateTime.isAfter(ldt) && !currDateTime.isAfter(ldt.plusMinutes(58))) return false;
            if(currDateTime.isBefore(ldt) && !currDateTime.isBefore(ldt.minusMinutes(58))) return false;
        }

        return true;
    }

    // Getters and setters are mostly for table view models
    public String getMedicalLicenceNo() {
        return medicalLicenceNo;
    }

    public void setMedicalLicenceNo(String medicalLicenceNo) {
        this.medicalLicenceNo = medicalLicenceNo;
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


    public LocalTime getConsultationStart() {
        return consultationStart;
    }

    public void setConsultationStart(LocalTime consultationStart) {
        this.consultationStart = consultationStart;
    }

    public LocalTime getConsultationEnd() {
        return consultationEnd;
    }

    public void setConsultationEnd(LocalTime consultationEnd) {
        this.consultationEnd = consultationEnd;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Doctor) {
            return ((Doctor) obj).getMedicalLicenceNo().equals(this.getMedicalLicenceNo());
        }
        return super.equals(obj);
    }
}
