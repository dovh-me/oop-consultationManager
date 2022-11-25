package models;

import java.io.Serializable;
import java.util.Date;

public class Doctor extends Person implements Serializable {
    private String medicalLicenseNo;
    private String specialization;

    public Doctor() {
    }

    public Doctor(String name, String surname, String dob, String contactNo) {
        super(name, surname, dob, contactNo);
    }

    public Doctor(Person p, String medicalLicenseNo, String specialization) {
        this.medicalLicenseNo = medicalLicenseNo;
        this.specialization = specialization;
    }

    public Doctor(String name, String surname, String dob, String contactNo, String medicalLicenseNo, String specialization) {
        super(name, surname, dob, contactNo);
        this.medicalLicenseNo = medicalLicenseNo;
        this.specialization = specialization;
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
}
