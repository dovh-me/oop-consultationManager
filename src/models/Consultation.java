package models;

import constants.Formats;
import exceptions.CryptoException;
import exceptions.IllegalConsultationException;
import gui.components.TabularModel;
import main.GUIApplication;
import util.AlertBox;
import util.ConsoleLog;
import util.CryptoUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Consultation implements Serializable, Comparable<Consultation>, TabularModel {
    public static String[] tableColumns = new String[]{"Date Time", "Consultation UID", "Patient UID", "Patient Name", "Doctor Name", "Specialization", "Cost"};
    public static String[] tableFieldNames = new String[]{"consultationDateTime", "consultationUID", "patientUID", "patientName", "doctorName", "specialization", "cost"};
    private String consultationUID;
    private LocalDateTime consultationDateTime; // create a new constructor with this field
    private float cost;
    private File textNotes;
    private ArrayList<File> noteImages = new ArrayList<>();
    private Patient patient;
    private Doctor doctor;
    private SecretKey encryptKey;
    private int noteImageIndex;

    public Consultation() {
    }

    public Consultation(Patient p, Doctor d, String dateTime) throws DateTimeParseException {
        this.patient = p;
        this.doctor = d;
        this.consultationDateTime = LocalDateTime.parse(dateTime, Formats.DATE_TIME_FORMATTER);
        this.cost = 0;
    }

    public Consultation(Patient p, Doctor d, LocalDateTime consultationDateTime) {
        this.setPatient(p);
        this.setDoctor(d);
        this.consultationDateTime = consultationDateTime;
    }

    public boolean validateConsultation() {
        return this.patient != null && this.doctor != null && this.consultationDateTime != null;
    }

    public void updateConsultation(Float cost, String textNotes, ArrayList<File> notesImages) throws IllegalConsultationException {
        try {
            if (cost != null)
                this.setCost(cost);

            this.setTextNotes(textNotes);
            // delete unwanted assets
            this.deleteUnusedAssets(notesImages);
            this.setNoteImages(notesImages);
        } catch (IOException | CryptoException | NoSuchAlgorithmException e) {
            throw new IllegalConsultationException("Error updating consultation");
        }
    }

    public void clearAssets() {
        try {
            if (this.noteImages == null || this.noteImages.size() == 0 || !this.validateConsultation()) return;

            for (File noteImage : noteImages) {
                if (noteImage.delete())
                    ConsoleLog.info(String.format("File deleted successfully! %s", noteImage.getPath()));
                else
                    ConsoleLog.error(String.format("There was an error deleting the file. Please delete manually %s", noteImage.getPath()));
            }
            // delete the text notes
            if (this.textNotes.delete())
                ConsoleLog.log("Successfully deleted the text notes file for the consultation: " + this.consultationUID);
            else
                ConsoleLog.error("There was an error deleting text notes for the consultation: " + this.consultationUID +
                        ". Please delete manually at: " + this.textNotes.getPath());
        } catch (Exception e) {
            AlertBox.showErrorAlert("There was an error deleting the notes");
            ConsoleLog.error(e.getLocalizedMessage());
        }
    }

    private SecretKey getEncryptKey() throws NoSuchAlgorithmException {
        if (encryptKey == null) { // generate new key when key upon accessing if already doesn't exist
            // generate encryption key
            KeyGenerator generator = KeyGenerator.getInstance(CryptoUtils.getAlgorithm());
            generator.init(128); // The AES key size in number of bits
            this.encryptKey = generator.generateKey();
        }
        return encryptKey;
    }

    // Getters and setters are mostly for the table view models

    public ArrayList<File> getNoteImages() {
        return this.noteImages;
    }

    public List<byte[]> getNoteImageBytes() throws NoSuchAlgorithmException, IOException, CryptoException {
        List<byte[]> decryptedFiles = new ArrayList<>();
        for (File noteImage : this.noteImages) {
            byte[] decrypted = CryptoUtils.getDecryptedBytes(this.getEncryptKey(), noteImage);
            decryptedFiles.add(decrypted);
        }
        return decryptedFiles;
    }

    public void setNoteImages(ArrayList<File> noteImages) throws IOException {
        ArrayList<File> tmp = new ArrayList<>();
        File outputFolder = new File(String.format("./data/%s/%s", this.getPatient().getUID(), this.getConsultationUID()));

        if (outputFolder.mkdirs()) ConsoleLog.info("Patient assets folder created");
        else ConsoleLog.info("Patients assets folder already exists");

        for (File noteImage : noteImages) {
            if(this.noteImages.contains(noteImage)) {
                tmp.add(noteImage);
                continue;
            }
            try {
                String fileName = noteImage.getName().endsWith(".encrypted") ? String.format("/%s",noteImage.getName()) : String.format("/%s.encrypted", noteImage.getName());
                File outputFile = new File(outputFolder + fileName);
                 if (!outputFile.createNewFile()) {
                    ConsoleLog.error("A file exists in the provided path. Old file is being overwritten");
                    if (!outputFile.createNewFile()) throw new Exception("Unable to create encrypted file"); // skips if failed
                }


                CryptoUtils.encrypt(this.getEncryptKey(), noteImage, outputFile);
                tmp.add(outputFile);
            } catch (CryptoException | NoSuchAlgorithmException e) {
                ConsoleLog.error("There was an error encrypting the file");
                e.printStackTrace();
            } catch(Exception e) {
                ConsoleLog.error(e.getLocalizedMessage());
            }
        }
        this.noteImages = tmp;
    }

    public void deleteUnusedAssets(ArrayList<File> newFilesList) {
        for (File file : this.noteImages) {
            if(!newFilesList.contains(file)) file.delete();
        }
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public double getHourlyCost() throws IllegalConsultationException {
        final double FIRST_CONSULTATION_PRICE = 15;
        final double CONSULTATION_PRICE = 25;

        double price = FIRST_CONSULTATION_PRICE;
        if (this.getPatient() == null) throw new IllegalConsultationException("Patient not found");
        // Check if current consultation is the first consultation of the patient
        List<Consultation> consultations = GUIApplication.app.manager.getConsultations().stream().filter((e) -> e.getPatient().equals(this.getPatient())).collect(Collectors.toList());

        for (Consultation consultation : consultations) {
            if (this.getConsultationDateTime().isAfter(consultation.getConsultationDateTime())) {
                price = CONSULTATION_PRICE;
                break;
            }
        }

        return price;
    }

    public String getTextNotes() throws NoSuchAlgorithmException, IOException, CryptoException {
        File textNotesFile = this.textNotes;
        // Decrypting the content
        byte[] textBytes = CryptoUtils.getDecryptedBytes(this.getEncryptKey(), textNotesFile);

        return new String(textBytes);
    }

    public void setTextNotes(String textNotes) throws IOException, NoSuchAlgorithmException, CryptoException {
        this.textNotes = new File(String.format("./data/%s/%s/tn.txt.encrypted", this.getPatient().getUID(), this.getConsultationUID()));

        if (!this.textNotes.exists()) {
            this.textNotes.getParentFile().mkdirs();
            this.textNotes.createNewFile();
        }
        CryptoUtils.encrypt(this.getEncryptKey(), textNotes, this.textNotes);
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
        return this.patient.getUID();
    }


    public String getDoctorName() {
        return this.getDoctor().getFullName();
    }

    public LocalDateTime getConsultationDateTime() {
        return consultationDateTime;
    }

    public String getSpecialization() {
        return this.doctor.getSpecialization();
    }

    public String getPatientName() {
        return this.patient.getFullName();
    }

    public String getConsultationUID() {
        if (consultationUID == null) {
            consultationUID = String.format("c%5d", GUIApplication.app.manager.getConsultationIdIndex()).replace(' ', '0');
        }
        return consultationUID;
    }

    @Override
    public int compareTo(Consultation o) {
        return this.getConsultationDateTime().compareTo(o.getConsultationDateTime());
    }
}
