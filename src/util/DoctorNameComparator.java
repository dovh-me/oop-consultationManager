package util;

import models.Doctor;

import java.util.Comparator;

public class DoctorNameComparator implements Comparator<Doctor> {
    @Override
    public int compare(Doctor o1, Doctor o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
