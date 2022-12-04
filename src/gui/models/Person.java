package gui.models;

import constants.Formats;
import gui.components.TabularModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Person implements Serializable, TabularModel {
    private String name;
    private String surname;
    private LocalDate dob;
    private String contactNo;

    public Person(String name, String surname, String dob, String contactNo) throws DateTimeParseException {
        this.name = name;
        this.surname = surname;
        this.contactNo = contactNo;
        this.dob = LocalDate.parse(dob);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Override
    public String[] getTableColumnNames() {
        return new String[]{"Name", "Date of Birth", "Contact No."};
    }

    @Override
    public String[] getTableRowData() {
        return new String[]{this.name + this.surname, this.dob.format(Formats.DATE_FORMAT), this.contactNo};
    }
}
