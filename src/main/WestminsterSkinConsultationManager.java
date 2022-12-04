package main;

import constants.Formats;
import exceptions.DailyConsultationsFullException;
import gui.models.Consultation;
import gui.models.Doctor;
import gui.models.Patient;
import gui.pages.Application;
import util.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class WestminsterSkinConsultationManager implements SkinConsultationManager, Serializable {

    private ArrayList<Doctor> doctors;
    private ArrayList<Consultation> consultations;

    public WestminsterSkinConsultationManager() {
        this.doctors = new ArrayList<>(10);
        this.consultations = new ArrayList<>();
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
        st.addRow("Add Consultation", "AC/ac");
        st.addRow("View Consultations", "VC/vc");
        st.addRow("Cancel Consultation", "CC/cc");
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
            case "AC":
                addConsultation();
                break;
            case "VC":
                viewConsultations();
                break;
            case "CC":
                cancelConsultation();
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

    public Optional<Doctor> findDoctor(String medLicense) {
        return this.doctors.stream().filter((Doctor d) -> d.getMedicalLicenseNo().equals(medLicense)).findFirst();
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
            Doctor d = (Doctor) doctor;
            if(!d.getConsultations().isEmpty()) {
                ConsoleLog.error("Doctor you are trying to delete still has some consultations " +
                        "assigned on the system. Please cancel the consultations before deleting");
                return;
            }
            this.doctors.remove(doctor);
        }
        ConsoleLog.success(String.format("Doctor with license number %s has " +
                "been " +
                "removed from the system!", medLicense));
    }

    @Override
    public boolean getDoctorAvailability(Doctor doctor, LocalDate ldt) {
        return doctor.getAvailableDays().contains(ldt.getDayOfWeek())
                && doctor.findConsultationsByDate(ldt).size() < doctor.getPatientsPerDay();
    }

    @Override
    public Doctor getAvailableDoctor(String specialization, String dateTime) {
        return null;
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
                    String.format("Enter the date of the consultation (%s): ",
                            "yyyy-MM-dd"), Validator.CONSULTATION_DATE);
            String notes = InputPrompter.promptString("Enter notes (without line breaks): ");

            ConsoleLog.info("Doctor Information");
            String medLicense = InputPrompter.promptValidatedString("Enter the medical license of" +
                    " the doctor: ", new Validator<String>() {
                @Override
                public boolean validate(String input) {
                    if (!Validator.MEDICAL_LICENSE_NO_STRING.validate(input)) return false;

                    return findDoctor(input).isPresent();
                }
            });
            ConsoleLog.info("Checking availability of the doctor for the time slot...");

            Doctor doctor = findDoctor(medLicense).get();

            if (!this.getDoctorAvailability(doctor,
                    LocalDate.parse(dateTime, Formats.DATE_FORMAT))) {
                ConsoleLog.warning("Doctor you are trying to book is not available on the " +
                        "relevant date... a different doctor will be recommended by the system.");
                doctor = getAvailableDoctor(doctor.getSpecialization(), dateTime);

                // TODO: make the program run if no doctors available for the given date??
                //  Doesn't seem to be practical. Therefore, exiting add consultation for now.
                if (doctor == null) {
                    ConsoleLog.error(String.format("No doctors available on the %s. Please select" +
                            " a different date!", dateTime));
                    return;
                } else {
                    ConsoleLog.success(String.format("New doctor assigned. %s %s", doctor.getName(), doctor.getSurname()));
                    String confirmation = InputPrompter.promptValidatedString("Do you want to " +
                            "book consultation with the recommended doctor? (Y/N)", Validator.YES_NO_INPUT);

                    switch (confirmation) {
                        case "N":
                            return;
                        case "Y":
                            break;
                    }
                }
            }

            Patient p = new Patient(pData[0], pData[1], pData[2], pData[3]);
            Consultation c = new Consultation(p, doctor, dateTime, notes);
            assert doctor != null;
            doctor.addConsultation(c);
            this.consultations.add(c);

            ConsoleLog.logWithColors(ConsoleColors.GREEN, String.format(
                    "New consultation added successfully! Patient UID: %s\nPlease use the patient UID to access the consultation\nApproximate consultation date time: %s",
                    p.getUid(), c.getApproximateDateTime()
            ));

        } catch (DailyConsultationsFullException e) {
            ConsoleLog.error("Illegal operation: No more consultations allowed for the doctor being booked. Please select a different doctor");
        } catch (NoSuchElementException e) {
            ConsoleLog.error("There was an error capturing input");
            e.printStackTrace();
        }  catch (NullPointerException e) {
            ConsoleLog.error("Unexpected error occurred. Please try again! If the issue persists " +
                    "please inform developers");
            e.printStackTrace();
        }
    }

    @Override
    public void updateConsultation() {

    }

    @Override
    public void cancelConsultation() {
        ConsoleLog.logWithColors(ConsoleColors.RED, "Deleting consultation...");

        String patientUID = InputPrompter.promptValidatedString("Please enter the patient UID: ", Validator.PATIENT_UID);

        Optional<Consultation> toCancel = this.consultations.stream().filter(c -> c.getPatient().getUid().equals(patientUID)).findFirst();
        if (!toCancel.isPresent()) {
            ConsoleLog.error(String.format("No consultation found for the provided patient UID: %s", patientUID));
            return;
        }

        String confirmation = InputPrompter.promptValidatedString("Are you sure you want to " +
                "cancel the consultation? (Y/N): ", new Validator<String>() {
            @Override
            public boolean validate(String input) {
                List<String> allowed = Arrays.asList("Y", "N");
                return allowed.contains(input.toUpperCase());
            }
        });

        switch (confirmation.toUpperCase()) {
            case "N":
                ConsoleLog.info(String.format("Consultation of patient %s will not be cancelled!", toCancel.get().getPatient().getUid()));
                return;
            case "Y":
                ConsoleLog.success(String.format("Consultation of patient %s has been successfully cancelled!", toCancel.get().getPatient().getUid()));
                this.consultations.remove(toCancel.get());
                toCancel.get().getDoctor().getConsultations().remove(toCancel.get());
                break;
        }
    }

    @Override
    public void viewConsultations() {
        if (this.consultations.isEmpty()) {
            ConsoleLog.info("No consultation recorded in the system at the moment...");
            return;
        }

        ConsoleLog.logWithColors(ConsoleColors.CYAN_BOLD_BRIGHT, "Viewing consultations...");

        CommandLineTable ct = new CommandLineTable();
        ct.setShowVerticalLines(true);
        ct.setRightAlign(false);
        ct.setHeaders("Approx. Date Time", "Patient UID", "Patient Name", "Patient Contact No.",
                "Doctor", "Specialization", "Medical License No.");
        for (Consultation c : consultations) {
            ct.addRow(c.getApproximateDateTime(), c.getPatient().getUid(),
                    c.getPatient().getName() + " " + c.getPatient().getSurname(), c.getPatient().getContactNo(), "Dr." + c.getDoctor().getName() + " " + c.getDoctor().getSurname(), c.getDoctor().getSpecialization(), c.getDoctor().getMedicalLicenseNo());
        }
        ct.print();
    }

    public void launchGUI() {
        Application.start(this);
        System.out.println("GUI application started!!");
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

    public void start() {
        String promptMessage = "Please enter the letter of the action you want to perform: ";
        String mainMenuInput;
        boolean shouldQuit = false;

        while (!shouldQuit) {
            this.printMainMenu();
            mainMenuInput = InputPrompter.promptValidatedString(promptMessage, new Validator<String>() {
                @Override
                public boolean validate(String input) {
                    ArrayList<String> allowed = new ArrayList<>(Arrays.asList("A", "D", "V", "AC", "VC", "CC", "S", "L", "G", "Q"));
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
