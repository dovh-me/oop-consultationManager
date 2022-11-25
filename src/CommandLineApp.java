import models.Doctor;
import util.*;

import java.io.*;
import java.util.*;

public class CommandLineApp {
    WestminsterSkinConsultationManager consultationManager;
    String promptMessage = "Please enter the letter of the action you want to perform: ";

    public CommandLineApp() {
        this.consultationManager = new WestminsterSkinConsultationManager();
    }

    public void printMainMenu() {
        ConsoleLog.logWithColors(ConsoleColors.BLUE_BACKGROUND,"!WELCOME TO WESTMINSTER SKIN CONSULTATION MANAGER!", true);
        System.out.println();
        CommandLineTable st = new CommandLineTable();
        st.setShowVerticalLines(true);
        st.setHeaders("Action", "Input Key");
        st.addRow("Add Doctor", "A");
        st.addRow("Delete Doctor", "D");
        st.addRow("View Doctors", "V");
        st.addRow("Save in File", "S");
        st.addRow("Load from File", "L");
        st.addRow("Launch in GUI", "G");
        st.addRow("Quit", "Q");
        st.print();
    }

    public void mainMenuInputHandler(String option) {
        switch (option) {
            case "A":
                addDoctor();
                break;
            case "D":
                deleteDoctor();
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
        }
    }

    public void addDoctor() {
        try {
            ConsoleLog.logWithColors(ConsoleColors.GREEN_BOLD_BRIGHT, "Adding doctor...");
            String[] pData = this.promptPerson();
            String medLicense = InputPrompter.promptString("Enter medical license number: ");
            String specialization = InputPrompter.promptString("Enter specialization: ");

            this.consultationManager.addDoctor(
                    new Doctor(pData[0], pData[1], pData[2], pData[3], medLicense, specialization)
            );
            ConsoleLog.logWithColors(ConsoleColors.GREEN, String.format("New doctor added successfully! Med License No: %s", medLicense));
        } catch (IOException e) {
            ConsoleLog.error("There was an error capturing input");
            e.printStackTrace();
        }
    }

    public void deleteDoctor() {
        ConsoleLog.logWithColors(ConsoleColors.RED_BOLD_BRIGHT, "Deleting doctor...");
        String medLicense = InputPrompter.promptString("Enter medical license number: ");
        this.consultationManager.removeDoctor(medLicense);
        ConsoleLog.logWithColors(ConsoleColors.RED, String.format("Doctor with license number %s has been removed from the system!", medLicense));
    }

    public String[] promptPerson() throws IOException {
        String name = InputPrompter.promptString("Enter name: ");
        String surname = InputPrompter.promptString("Enter surname: ");
        String dob = InputPrompter.promptString("Enter date of birth (format: yyyy-mm-dd): ");
        String contactNo = InputPrompter.promptString("Enter contact number: ");

        return new String[]{name, surname, dob, contactNo};
    }

    public void viewDoctors() {
        ConsoleLog.logWithColors(ConsoleColors.CYAN_BOLD_BRIGHT, "Viewing doctors...");
        ArrayList<Doctor> doctors = this.consultationManager.getDoctors();
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

    public void saveToFile() {
        try {
            FileOutputStream fileOut = new FileOutputStream("./data/consultationManager.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.consultationManager);
            out.close();
            fileOut.close();
            ConsoleLog.success("Serialized data is saved in /data/consultationManager.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void loadFromFile() {
        try {
            FileInputStream fileIn = new FileInputStream("./data/consultationManager.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.consultationManager = (WestminsterSkinConsultationManager) in.readObject();
            ConsoleLog.success("Deserialized data loaded from /data/consultationManager.ser");
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("WestminsterSkinConsultationManager class not found");
            c.printStackTrace();
        }
    }

    public void launchGUI() {

    }

    public void start() {
        String mainMenuInput = "";
        while(!mainMenuInput.equals("Q")){
            this.printMainMenu();
            mainMenuInput = InputPrompter.promptString(this.promptMessage, Arrays.asList("A", "D", "V", "S", "L", "G", "Q"));
            mainMenuInputHandler(mainMenuInput);
        }
    }

    public static void main(String[] args) {
        CommandLineApp app = new CommandLineApp();
        app.start();
    }
}
