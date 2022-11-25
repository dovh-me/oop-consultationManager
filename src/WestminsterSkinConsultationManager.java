import models.Consultation;
import models.Doctor;
import util.ConsoleLog;

import javax.print.Doc;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WestminsterSkinConsultationManager implements SkinConsultationManager, Serializable {

    private ArrayList<Doctor> doctors;
    private ArrayList<Consultation> consultations;

    public WestminsterSkinConsultationManager() {
        this.doctors = new ArrayList<>(10);
        this.consultations = new ArrayList<>();
    }

    @Override
    public void addDoctor(Doctor d) {
        if(this.doctors.size() > 10) ConsoleLog.error("Capacity of doctors reached. Please delete a doctor record before adding anymore... 10/10");
        else this.doctors.add(d);
    }

    @Override
    public void updateDoctor() {

    }

    @Override
    public void removeDoctor(String medLicense) {
        Object[] doctorsToRemove = this.doctors.stream().filter((Doctor d) -> d.getMedicalLicenseNo().equals(medLicense)).toArray();
        for (Object doctor : doctorsToRemove) {
            this.doctors.remove(doctor);
        }
    }

    @Override
    public boolean getDoctorAvailability() {
        return false;
    }

    @Override
    public void viewDoctors() {

    }

    @Override
    public void addConsultation() {

    }

    @Override
    public void updateConsultation() {

    }

    @Override
    public void cancelConsultation() {

    }

    @Override
    public void viewConsultations() {

    }

    public void runCommandLineInterface() {

    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }

    public ArrayList<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(ArrayList<Consultation> consultations) {
        this.consultations = consultations;
    }
}
