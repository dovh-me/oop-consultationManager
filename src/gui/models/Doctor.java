package gui.models;

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
    public static String[] tableColumns = new String[] {"Medical License No." ,"Full Name", "Specialisation", "Contact No."};
    public static String[] tableFieldNames = new String[] {"medicalLicenseNo", "fullName", "specialization", "contactNo"};
    private String medicalLicenseNo;
    private String specialization;
    private TreeSet<Consultation> consultations = new TreeSet<>();
    private LocalTime consultationStart = LocalTime.of(8,0);
    private LocalTime consultationEnd = LocalTime.of(21, 0);

    public Doctor(String name, String surname, String dob, String contactNo) throws DateTimeParseException {
        super(name, surname, dob, contactNo);
    }

    public Doctor(String name, String surname, String dob, String contactNo, String medicalLicenseNo, String specialization) throws DateTimeParseException {
        super(name, surname, dob, contactNo);
        this.medicalLicenseNo = medicalLicenseNo;
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

    // TODO: Throw error if the object being updated does not exist in the set?
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
            if(currDateTime.isAfter(ldt) && !currDateTime.isAfter(ldt.plusHours(1))) return false;
            if(currDateTime.isBefore(ldt) && !currDateTime.isBefore(ldt.minusHours(1))) return false;
        }

        return true;
    }

    // Getters and setters are mostly for table view models
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


    public LocalTime getConsultationStart() {
        return consultationStart;
    }

    public void setConsultationStart(LocalTime consultationStart) {
        this.consultationStart = consultationStart;
    }
}
