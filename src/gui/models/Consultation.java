package gui.models;

import constants.Formats;
import exceptions.CryptoException;
import exceptions.IllegalConsultationException;
import gui.components.TabularModel;
import main.GUIApplication;
import org.apache.commons.io.FileUtils;
import util.AlertBox;
import util.ConsoleLog;
import util.CryptoUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Consultation implements Serializable, Comparable<Consultation>, TabularModel {
    public static String[] tableColumns = new String[]{"Date Time","Patient UID", "Patient Name", "Doctor Name","Specialization", "Cost"};
    public static String[] tableFieldNames = new String[] {"consultationDateTime", "patientUID", "patientName", "doctorName","specialization", "cost"};
    LocalDateTime consultationDateTime; // create a new constructor with this field
    private float cost;
    private File textNotes;
    private LinkedList<File> noteImages = new LinkedList<>();
    private Patient patient;
    private Doctor doctor;
    private SecretKey encryptKey;

    public Consultation() {
    }

    public Consultation(Patient p, Doctor d, String dateTime) throws DateTimeParseException {
        this.patient = p;
        this.doctor = d;
        this.consultationDateTime = LocalDateTime.parse(dateTime, Formats.DATE_TIME_FORMATTER);
        this.cost = 0;
    }

    public Consultation(Doctor d, LocalDateTime consultationDateTime) {
        this.setDoctor(d);
        this.consultationDateTime = consultationDateTime;
    }

    public boolean validateConsultation() {
        return this.patient != null && this.doctor != null && this.consultationDateTime != null;
    }

    public void clearAssets() {
        if(this.noteImages == null || this.noteImages.size() == 0 || !this.validateConsultation()) return;

        for (File noteImage : noteImages) {
            File outputFile = new File(String.format("./data/%s/%s",this.getPatient().getUid(), noteImage.getName()));
            if(outputFile.delete()) ConsoleLog.info(String.format("File deleted successfully! %s", noteImage.getPath()));
            else ConsoleLog.error(String.format("There was an error deleting the file. Please delete manually %s", noteImage.getPath()));
        }
    }

    private SecretKey getEncryptKey() throws NoSuchAlgorithmException {
        if(encryptKey == null) { // generate new key when key upon accessing if already doesn't exist
            // generate encryption key
            KeyGenerator generator = KeyGenerator.getInstance(CryptoUtils.getAlgorithm());
            generator.init(128); // The AES key size in number of bits
            this.encryptKey = generator.generateKey();
        }
        return encryptKey;
    }

    // Getters and setters are mostly for the table view models
    public List<File> getNoteImages() throws NoSuchAlgorithmException, IOException, CryptoException {
        List<File> decryptedFiles = new ArrayList<>();
        for (File noteImage : this.noteImages) {
            File decrypted = CryptoUtils.decrypt(this.getEncryptKey(), noteImage);
            decryptedFiles.add(decrypted);
        }
        return decryptedFiles;
    }

    public void setNoteImages(LinkedList<File> noteImages) throws IOException {
        LinkedList<File> tmp = new LinkedList<>();
        File outputFolder = new File(String.format("./data/%s",this.getPatient().getUid()));

        if(outputFolder.mkdirs()) ConsoleLog.info("Patient assets folder created");
        else ConsoleLog.info("Patients assets folder already exists");

        for (File noteImage : noteImages) {
            File outputFile = new File(outputFolder.getAbsolutePath() + String.format("/%s.encrypted", noteImage.getName()));
            if(!outputFile.createNewFile()) {
                ConsoleLog.error("A file exists in the provided path. Old file is being overwritten");
                outputFile.delete(); // tries again to delete the file
                if(!outputFile.createNewFile()) continue; // tries to create the file again
            }

            try {
                CryptoUtils.encrypt(this.getEncryptKey(), noteImage, outputFile);
                tmp.add(outputFile);
            } catch (CryptoException | NoSuchAlgorithmException e) {
                ConsoleLog.error("There was an error encrypting the file");
                e.printStackTrace();
            }
        }
        this.noteImages = tmp;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(int hours) throws IllegalConsultationException {
        final float FIRST_CONSULTATION_PRICE = 15;
        final float CONSULTATION_PRICE = 25;

        float price = FIRST_CONSULTATION_PRICE;
        if(this.getPatient() == null) throw new IllegalConsultationException("Patient not found");
        // Check if current consultation is the first consultation of the patient
        List<Consultation> consultations = GUIApplication.app.manager.getConsultations().stream().filter((e) -> e.getPatient().equals(this.getPatient())).collect(Collectors.toList());

        for (Consultation consultation : consultations) {
            if(this.getConsultationDateTime().isAfter(consultation.getConsultationDateTime())) {
                price = CONSULTATION_PRICE; break;
            }
        }
        this.cost = hours * price;
    }

    public String getTextNotes() throws NoSuchAlgorithmException, IOException, CryptoException{
        File textNotesFile = new File(String.format("./data/%s/tn.txt.encrypted",this.getPatient().getUid()));
        // Decrypting the content
        File outFile = CryptoUtils.decrypt(this.getEncryptKey(), textNotesFile);
        FileInputStream inputStream = new FileInputStream(outFile);
        byte[] inputBytes = new byte[(int) outFile.length()];
        inputStream.read(inputBytes);

        return new String(inputBytes);
    }

    public void setTextNotes(String textNotes) throws IOException, NoSuchAlgorithmException, CryptoException {
        File textNotesFile = new File(String.format("./data/%s/tn.txt.encrypted",this.getPatient().getUid()));
        if(!textNotesFile.exists()) {
            textNotesFile.getParentFile().mkdirs();
            textNotesFile.createNewFile();
        }
        CryptoUtils.encrypt(this.getEncryptKey(), textNotes, textNotesFile);
        this.textNotes = textNotesFile;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getPatientUID() {
        return this.patient.getUid();
    }

    public void setPatientUID(String uid) { } // just as a placeholder

    public String getDoctorName() {
        return this.getDoctor().getFullName();
    }

    public LocalDateTime getConsultationDateTime() {
        return consultationDateTime;
    }

    public void setConsultationDateTime(LocalDateTime localDateTime) {}

    public String getSpecialization() { return this.doctor.getSpecialization(); }

    public void setSpecialization(String s) {}

    public String getPatientName() { return this.patient.getFullName(); }

    public void setPatientName(String name) {}

    @Override
    public int compareTo(Consultation o) {
        return this.getConsultationDateTime().compareTo(o.getConsultationDateTime());
    }
}
