package main;

import constants.Formats;
import exceptions.IllegalConsultationException;
import gui.models.Consultation;
import gui.models.Doctor;
import gui.models.Patient;
import util.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class WestminsterSkinConsultationManager implements SkinConsultationManager, Serializable {
    private ArrayList<Doctor> doctors;
    private ArrayList<Consultation> consultations;
    private ArrayList<Patient> patients;
    private int consultationIdIndex;

    public WestminsterSkinConsultationManager() {
        this.doctors = new ArrayList<>(10);
        this.consultations = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.consultationIdIndex = 1;
    }

    private void printMainMenu() {
        ConsoleLog.logWithColors(ConsoleColors.BLUE_BACKGROUND, "!WELCOME TO WESTMINSTER SKIN CONSULTATION MANAGER COMMAND LINE INTERFACE!", true);
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
        st.addRow("--", "--");
        st.addRow("View Consultations", "VC/vc");
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

    public Optional<Doctor> findDoctor(String medLicence) {
        return this.doctors.stream().filter((Doctor d) -> d.getMedicalLicenceNo().equals(medLicence)).findFirst();
    }

    @Override
    public void addDoctor() {
        try {
            if (this.doctors.size() >= 10) {
                ConsoleLog.error("Maximum doctors count of 10 is reached. Please remove an existing record to add more.");
                return;
            }

            ConsoleLog.logWithColors(ConsoleColors.GREEN_BOLD_BRIGHT, "Adding doctor...");
            String[] pData = this.promptPerson();
            String medLicence = InputPrompter.promptValidatedString("Enter medical licence number: ", Validator.MEDICAL_LICENSE_NO_STRING);
            String specialization = InputPrompter.promptString("Enter specialization: ");

            this.doctors.add(new Doctor(pData[0], pData[1], pData[2], pData[3], medLicence, specialization));

            ConsoleLog.logWithColors(ConsoleColors.GREEN, String.format("New doctor was added successfully! Med Licence No: %s", medLicence));
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
        String medLicence = InputPrompter.promptValidatedString("Enter medical licence number: ", Validator.MEDICAL_LICENSE_NO_STRING);
        Object[] doctorsToRemove = this.doctors.stream().filter((Doctor d) -> d.getMedicalLicenceNo().equals(medLicence)).toArray();
        for (Object doctor : doctorsToRemove) {
            Doctor d = (Doctor) doctor;
            if(!d.getConsultations().isEmpty()) {
                ConsoleLog.error("Doctor you are trying to delete still has some consultations " +
                        "assigned on the system. Please cancel the consultations before deleting");
                return;
            }
            this.doctors.remove(doctor);
        }
        ConsoleLog.success(String.format("Doctor with licence number %s has " +
                "been " +
                "removed from the system!", medLicence));
        ConsoleLog.info(String.format("No. of available doctors in the system: %d",this.getDoctors().size()));
    }

    @Override
    public Doctor getAvailableDoctor(String specialization, LocalDateTime dateTime) {
        List<Doctor> availableDoctors =  this.doctors.stream().filter(doctor -> doctor.getSpecialization().equals(specialization) && doctor.getAvailability(dateTime)).collect(Collectors.toList());
        if(availableDoctors.isEmpty()) return null;
        // Generate a random integer to select the doctor
        int randomIndex = (int) Math.round(Math.random() * (availableDoctors.size() - 1));
        return availableDoctors.get(randomIndex);
    }

    @Override
    public void viewDoctors() {
        if (this.doctors.isEmpty()) {
            ConsoleLog.info("Doctors list is empty! Please add some doctors before viewing...");
            return;
        }

        ConsoleLog.logWithColors(ConsoleColors.CYAN_BOLD_BRIGHT, "Viewing doctors...");
        ArrayList<Doctor> doctors = (ArrayList<Doctor>) this.getDoctors().clone();
        doctors.sort(new DoctorNameComparator());

        CommandLineTable ct = new CommandLineTable();
        ct.setShowVerticalLines(true);
        ct.setRightAlign(false);
        ct.setHeaders("Medical Licence No.", "Name", "Surname", "Date of Birth", "Contact No.", "Specialization");
        for (Doctor d : doctors) {
            ct.addRow(d.getMedicalLicenceNo(), d.getName(), d.getSurname(), d.getDob().toString(), d.getContactNo(), d.getSpecialization());
        }
        ct.print();
    }

    public void launchGUI() {
        GUIApplication.start(this);
    }

    public void addPatient(Patient patientToAdd) {
        this.patients.add(patientToAdd);
    }

    @Override
    public void addConsultation(Consultation consultation) {
        try {
            consultation.getDoctor().addConsultation(consultation);
            this.consultations.add(consultation);

            AlertBox.showInformationAlert(String.format(
                "New consultation added successfully! Patient UID: %s\nPlease use the patient UID to access the consultation\nApproximate consultation date time: %s",
                consultation.getPatient().getUid(), consultation.getConsultationDateTime().format(Formats.DATE_TIME_OUTPUT_FORMAT)
            ));

        } catch (IllegalConsultationException e) {
            AlertBox.showErrorAlert(e.getLocalizedMessage());
        } catch (Exception e) {
            AlertBox.showErrorAlert("Unexpected error occurred. Please try again! Report an issue if error persists");
            e.printStackTrace();
        }
    }

    @Override
    public void cancelConsultation(Consultation consultation) {
        if (consultation == null) return;
        boolean shouldCancel = AlertBox.showConfirmationAlert(String.format("Are you sure you want to " +
                "cancel the consultation of patient %s?", consultation.getPatient().getUid()));

        if (!shouldCancel) {
            AlertBox.showWarningAlert(String.format("Consultation of patient %s will not be cancelled!", consultation.getPatient().getUid()));
        } else {
            ConsoleLog.success(String.format("Consultation of patient %s has been successfully cancelled!", consultation.getPatient().getUid()));
            this.consultations.remove(consultation);
            consultation.getDoctor().getConsultations().remove(consultation);
            // clear asset files if any available
            consultation.clearAssets();
        }
    }

    public void saveToFile() {
        try (
                FileOutputStream fileOut = new FileOutputStream("./data/consultationManager.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
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
            this.patients = oldState.patients;
            this.consultationIdIndex = oldState.consultationIdIndex; // directly accessing the property since otherwise the index will increment

            ConsoleLog.success("Deserialized data loaded from /data/consultationManager.ser");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("main.WestminsterSkinConsultationManager class not found");
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

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }

    public int getConsultationIdIndex() {
        // auto increments upon returning
        return consultationIdIndex++;
    }

    public void start() {
        String promptMessage = "Please enter the letter of the action you want to perform: ";
        String mainMenuInput;
        boolean shouldQuit = false;

        while (!shouldQuit) {
            this.printMainMenu();
            mainMenuInput = InputPrompter.promptValidatedString(promptMessage, new Validator<String>() {
                @Override
                public boolean validate(String input) {
                    ArrayList<String> allowed = new ArrayList<>(Arrays.asList("A", "D", "V","VC", "S", "L", "G", "Q"));
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
