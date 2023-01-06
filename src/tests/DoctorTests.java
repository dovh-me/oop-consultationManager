package tests;

import constants.Formats;
import main.WestminsterSkinConsultationManager;
import models.Doctor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.InputPrompter;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;

public class DoctorTests {
    static WestminsterSkinConsultationManager manager;
    static Doctor doctor1;
    static Doctor doctor2;

    @BeforeClass
    public static void setupTestData() {
        doctor1 = new Doctor("James", "Butler", "2000-12-12","0717896541", "a123123", "Cosmetic Dermatology");
        doctor2 = new Doctor("Brad", "Cooper", "1990-11-10","0711115544", "a321321", "Paediatric Dermatology");
    }

    @Before
    public void resetManager() {
        // reset manager
        manager = new WestminsterSkinConsultationManager();
    }

    @Test
    public void addDoctor_verifyAbilityToCheckAddADoctor() {
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor1)));
        manager.mainMenuInputHandler("a");

        // verify the size of the array list
        assertEquals(1,manager.getDoctors().size());
        Doctor addedDoctor = manager.getDoctors().get(0);
        verifyDoctorEquals(addedDoctor, doctor1);
    }

    @Test
    public void addDoctor_verifyMultipleDoctorsWithTheSameMedicalLicenceAreNotAllowed() {
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor1)));
        manager.mainMenuInputHandler("a");

        // using the same object does not have an effect here since the string value of the object field values are being used
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor1)));
        manager.mainMenuInputHandler("a");
        // verify the size of the array list
        assertEquals(1,manager.getDoctors().size());
        // check if the added doctor matches doctor1
        Doctor addedDoctor = manager.getDoctors().get(0);
        verifyDoctorEquals(addedDoctor, doctor1);
    }

    @Test
    public void deleteDoctor_verifyCorrectDoctorGetsRemoved() {
        manager.mainMenuInputHandler("v");

        // add the first doctor
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor1)));
        manager.mainMenuInputHandler("a");

        // add the second doctor
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor2)));
        manager.mainMenuInputHandler("a");

        // remove the first doctor
        InputPrompter.setScanner(new Scanner(doctor1.getMedicalLicenceNo() + "\n"));
        manager.mainMenuInputHandler("d");

        // verify the doctors array list size decreased to 1
        assertEquals(1,manager.getDoctors().size());
        // verify the doctor2 is still in the list
        verifyDoctorEquals(doctor2,manager.getDoctors().get(0));
    }

    @Test
    public void deleteDoctor_verifyApplicationDoesNotCrashWhenADoctorIsRemovedWhenDoctorsListIsEmpty() {
        // verify the doctors list is empty
        assertEquals(0, manager.getDoctors().size());
        // try to delete a doctor with no doctors added to the system
        InputPrompter.setScanner(new Scanner(doctor1.getMedicalLicenceNo() + "\n"));
        manager.mainMenuInputHandler("d");
    }

    @Test
    public void saveToFile_VerifyTheDoctorsThatAreAddedToTheSystemCanBeSavedIntoAFile() {
        // add doctor1 to the system
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor1)));
        manager.mainMenuInputHandler("a");
        // add doctor2 to the system
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor2)));
        manager.mainMenuInputHandler("a");

        // save the data to a file
        manager.mainMenuInputHandler("s");

        try (
                FileInputStream fileIn = new FileInputStream("./data/consultationManager.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn)
        ) {
            // read the data from deserialized object
            WestminsterSkinConsultationManager oldState = (WestminsterSkinConsultationManager) in.readObject();

            assertEquals(2,oldState.getDoctors().size());
            verifyDoctorEquals(oldState.getDoctors().get(0), doctor1);
            verifyDoctorEquals(oldState.getDoctors().get(1), doctor2);

        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("main.WestminsterSkinConsultationManager class not found");
            c.printStackTrace();
        }
    }

    @Test
    public void loadFromFile_verifyTheDoctorsSavedInFileUsingSaveToFileCanBeLoadedToTheSystem() {
        // add doctor1 to the system
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor1)));
        manager.mainMenuInputHandler("a");
        // add doctor2 to the system
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor2)));
        manager.mainMenuInputHandler("a");

        // save the data to a file
        manager.mainMenuInputHandler("s");

        // reset the manager to mock the resetting of application upon restart
        manager = new WestminsterSkinConsultationManager();

        // load the saved data from the file
        manager.mainMenuInputHandler("l");

        verifyDoctorEquals(manager.getDoctors().get(0), doctor1);
        verifyDoctorEquals(manager.getDoctors().get(1), doctor2);
    }

    @Test
    public void viewDoctors_verifyTheDoctorsAddedToTheSystemCanBeViewedSorted(){
        // add doctor1 to the system
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor1)));
        manager.mainMenuInputHandler("a");
        // add doctor2 to the system
        InputPrompter.setScanner(new Scanner(doctorToInputString(doctor2)));
        manager.mainMenuInputHandler("a");


        // verify the terminal output to match with pre-stored output
        String terminalOutput = getTerminalOutput(() -> {
            manager.mainMenuInputHandler("v");
            return null;
        });

        // verify the correct output is displayed - The output is already sorted by the names
        assertEquals("\u001B[1;96mViewing doctors...\n" +
                "\u001B[0m\n" +
                "+---------------------+-------+---------+---------------+-------------+------------------------+\n" +
                "| Medical Licence No. | Name  | Surname | Date of Birth | Contact No. | Specialization         |\n" +
                "+---------------------+-------+---------+---------------+-------------+------------------------+\n" +
                "| a321321             | Brad  | Cooper  | 1990-11-10    | 0711115544  | Paediatric Dermatology |\n" +
                "| a123123             | James | Butler  | 2000-12-12    | 0717896541  | Cosmetic Dermatology   |\n" +
                "+---------------------+-------+---------+---------------+-------------+------------------------+\n",terminalOutput);
    }


    private void verifyDoctorEquals(Doctor expected, Doctor actual) {
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getSurname(), expected.getSurname());
        assertEquals(actual.getDob(), expected.getDob());
        assertEquals(actual.getContactNo(), expected.getContactNo());
        assertEquals(actual.getMedicalLicenceNo(), expected.getMedicalLicenceNo());
        assertEquals(actual.getSpecialization(), expected.getSpecialization());
    }

    private String doctorToInputString(Doctor d) {
        return  d.getName() +"\n"
                + d.getSurname() + "\n"
                + d.getDob().format(Formats.DATE_FORMATTER) + "\n"
                + d.getContactNo() + "\n"
                + d.getMedicalLicenceNo() + "\n"
                + d.getSpecialization() + "\n";
    }

    private String getTerminalOutput(Callable<Void> callback) {
        // Create a stream to hold the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);

        // verify the terminal output to match with pre-stored output
        manager.mainMenuInputHandler("v");
        // Put things back
        System.out.flush();
        System.setOut(old);
        try {
            callback.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return outputStream.toString();
    }
}
