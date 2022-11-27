import constants.Formats;
import exceptions.DailyConsultationsFullException;
import gui.models.Consultation;
import gui.models.Doctor;
import gui.models.Patient;
import util.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class WestminsterSkinConsultationManager implements SkinConsultationManager, Serializable {

    private ArrayList<Doctor> doctors;
    private ArrayList<Consultation> consultations;

    public WestminsterSkinConsultationManager() {
        this.doctors = new ArrayList<>(10);
        this.consultations = new ArrayList<>();
    }

    private void printMainMenu() {
        ConsoleLog.logWithColors(ConsoleColors.BLUE_BACKGROUND,"!WELCOME TO WESTMINSTER SKIN CONSULTATION MANAGER COMMAND LINE INTERFACE!", true);
        System.out.println();
        CommandLineTable st = new CommandLineTable();
        st.setShowVerticalLines(true);
        st.setHeaders("Action", "Key");
        st.addRow("Add Doctor", "A/a");
        st.addRow("Delete Doctor", "D/d");
        st.addRow("View Doctors", "V/v");
        st.addRow("Save in File", "S/s");
        st.addRow("Load from File", "L/l");
        st.addRow("Launch in GUI", "G/g");
        st.addRow("Quit", "Q/q");
        st.print();
    }

    public boolean mainMenuInputHandler(String option) {
        switch (option.toUpperCase()) {
            case "A":
                addDoctor();
                break;
            case "D":
                removeDoctor();
                break;
            case "V":
                viewDoctors();
                break;
            case "S":
                saveToFile();
                break;
            case "L":
                loadFromFile();
                break;
            case "G":
                launchGUI();
                break;
            case "Q":
                return true;
        }
        return false;
    }

    private String[] promptPerson() throws NoSuchElementException {
        String name = InputPrompter.promptValidatedString("Enter name: ", Validator.LETTER_STRING);
        String surname = InputPrompter.promptValidatedString("Enter surname: ", Validator.LETTER_STRING);
        String dob = InputPrompter.promptValidatedString("Enter date of birth (format: yyyy-mm-dd): ", Validator.DATE_STRING);
        String contactNo = InputPrompter.promptValidatedString("Enter contact number: ", Validator.PHONE_NUMBER_STRING);

        return new String[]{name, surname, dob, contactNo};
    }

    public Doctor findDoctor(String medLicense) {
        return (Doctor) this.doctors.stream().filter((Doctor d) -> d.getMedicalLicenseNo().equals(medLicense)).toArray()[0];
    }

    @Override
    public void addDoctor() {
        try {
            if(this.doctors.size() >= 10) {
                ConsoleLog.error("Maximum doctors count of 10 is reached. Please remove an existing record to add more.");
                return;
            }

            ConsoleLog.logWithColors(ConsoleColors.GREEN_BOLD_BRIGHT, "Adding doctor...");
            String[] pData = this.promptPerson();
            String medLicense = InputPrompter.promptValidatedString("Enter medical license number: ", Validator.MEDICAL_LICENSE_NO_STRING);
            String specialization = InputPrompter.promptString("Enter specialization: ");

            this.doctors.add(new Doctor(pData[0], pData[1], pData[2], pData[3], medLicense, specialization));

            ConsoleLog.logWithColors(ConsoleColors.GREEN, String.format("New doctor added successfully! Med License No: %s", medLicense));
        } catch (NoSuchElementException e) {
            ConsoleLog.error("There was an error capturing input");
            e.printStackTrace();
        }
    }

    @Override
    public void updateDoctor() {

    }

    @Override
    public void removeDoctor() {
        ConsoleLog.logWithColors(ConsoleColors.RED_BOLD_BRIGHT, "Deleting doctor...");
        String medLicense = InputPrompter.promptValidatedString("Enter medical license number: ", Validator.MEDICAL_LICENSE_NO_STRING);
        Object[] doctorsToRemove = this.doctors.stream().filter((Doctor d) -> d.getMedicalLicenseNo().equals(medLicense)).toArray();
        for (Object doctor : doctorsToRemove) {
            this.doctors.remove(doctor);
        }
        ConsoleLog.logWithColors(ConsoleColors.RED, String.format("Doctor with license number %s has been removed from the system!", medLicense));
    }

    @Override
    public boolean getDoctorAvailability(Doctor doctor, LocalDateTime ldt) {
        return doctor.getAvailableDays().contains(ldt.getDayOfWeek())
                && doctor.findConsultationsByDate(ldt).size() < doctor.getPatientsPerDay();
    }

    @Override
    public Doctor getAvailableDoctor(String specialization, String dateTime) {
        return null;
    }

    @Override
    public void viewDoctors() {
        if(this.doctors.isEmpty()) {
            ConsoleLog.info("Doctors list is empty! Please add some doctors before viewing...");
            return;
        }

        ConsoleLog.logWithColors(ConsoleColors.CYAN_BOLD_BRIGHT, "Viewing doctors...");
        ArrayList<Doctor> doctors = (ArrayList<Doctor>) this.getDoctors().clone();
        doctors.sort(new DoctorNameComparator());

        CommandLineTable ct = new CommandLineTable();
        ct.setShowVerticalLines(true);
        ct.setRightAlign(false);
        ct.setHeaders("Medical License No.", "Name", "Surname", "Date of Birth", "Contact No.", "Specialization");
        for (Doctor d : doctors) {
            ct.addRow(d.getMedicalLicenseNo(), d.getName(), d.getSurname(), d.getDob().toString(), d.getContactNo(), d.getSpecialization());
        }
        ct.print();
    }

    @Override
    public void addConsultation() {
        try {
            ConsoleLog.logWithColors(ConsoleColors.GREEN_BOLD_BRIGHT, "Adding consultation...");
            ConsoleLog.info("Patient Information");
            String[] pData = this.promptPerson();
            String dateTime = InputPrompter.promptValidatedString(
                    String.format("Enter the date and time of the consultation (%s): ", Formats.DATE_TIME_FORMAT.toString()), Validator.DATE_TIME_STRING);
            String notes = InputPrompter.promptString("Enter notes (without line breaks): ");

            ConsoleLog.info("Doctor Information");
            String medLicense = InputPrompter.promptValidatedString("Enter the medical license of the doctor: ", Validator.MEDICAL_LICENSE_NO_STRING);
            ConsoleLog.info("Checking availability of the doctor for the time slot...");

            Doctor doctor = findDoctor(medLicense);
            if(!this.getDoctorAvailability(doctor,LocalDateTime.parse(dateTime, Formats.DATE_TIME_FORMAT))) {
                ConsoleLog.warning("Doctor you are trying to book is not available on the relevant date... a separate doctor will be recommended by the system.");
                doctor = getAvailableDoctor(doctor.getSpecialization(), dateTime);
            }
            if(doctor == null) {
                ConsoleLog.error(String.format("No doctors available on the %s", dateTime));
            } else {
                ConsoleLog.success(String.format("New doctor assigned. %s %s", doctor.getName(), doctor.getSurname()));
                String confirmation = InputPrompter.promptValidatedString("Do you want to book consultation with the recommended doctor? (Y/N)", new Validator<String>() {
                    @Override
                    public boolean validate(String input) {
                        List<String> allowed = Arrays.asList("Y","N");
                        return allowed.contains(input.toUpperCase());
                    }
                });

                switch (confirmation) {
                    case "N":
                        return;
                    case "Y":
                        break;
                }
            }

            Patient p = new Patient(pData[0], pData[1], pData[2], pData[3]);
            Consultation c = new Consultation(p, doctor, dateTime, notes);
            doctor.addConsultation(c);
            this.consultations.add(c);

            ConsoleLog.logWithColors(ConsoleColors.GREEN, String.format("New consultation added successfully! Patient UID: %s\nPlease use the patient UID to access the consultation", p.getUid()));

        } catch (NoSuchElementException e) {
            ConsoleLog.error("There was an error capturing input");
            e.printStackTrace();
        } catch (DailyConsultationsFullException e) {
            ConsoleLog.error("Illegal operation: No more consultations allowed for the doctor being booked. Please select a different doctor");
        }
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

    public void launchGUI() {

    }

    public void saveToFile() {
        try (
                FileOutputStream fileOut = new FileOutputStream("./data/consultationManager.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut)
        ) {
            out.writeObject(this);
            ConsoleLog.success("Serialized data is saved in /data/consultationManager.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void loadFromFile() {
        try (
                FileInputStream fileIn = new FileInputStream("./data/consultationManager.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn)
        ) {
            WestminsterSkinConsultationManager oldState = (WestminsterSkinConsultationManager) in.readObject();
            this.doctors = oldState.doctors;
            this.consultations = oldState.consultations;

            ConsoleLog.success("Deserialized data loaded from /data/consultationManager.ser");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("WestminsterSkinConsultationManager class not found");
            c.printStackTrace();
        }
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

    public void start() {
        String promptMessage = "Please enter the letter of the action you want to perform: ";
        String mainMenuInput;
        boolean shouldQuit = false;

        while(!shouldQuit){
            this.printMainMenu();
            mainMenuInput = InputPrompter.promptValidatedString(promptMessage, new Validator<String>() {
                @Override
                public boolean validate(String input) {
                    ArrayList<String> allowed = new ArrayList<>(Arrays.asList("A", "D", "V", "S", "L", "G", "Q"));
                    return allowed.contains(input.toUpperCase());
                }
            });
            shouldQuit = mainMenuInputHandler(mainMenuInput);
        }
    }

    public static void main(String[] args) {
        WestminsterSkinConsultationManager consultationManager = new WestminsterSkinConsultationManager();
        consultationManager.start();
    }
}
