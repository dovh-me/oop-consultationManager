package util;

import models.Doctor;

import java.util.Comparator;

public class DoctorSurnameComparator implements Comparator<Doctor> {
    @Override
    public int compare(Doctor o1, Doctor o2) {
        return o1.getSurname().compareTo(o2.getSurname());
    }
}
