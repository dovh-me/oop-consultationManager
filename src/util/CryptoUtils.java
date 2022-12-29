// src: https://www.codejava.net/coding/file-encryption-and-decryption-simple-example
package util;

import exceptions.CryptoException;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static void encrypt(SecretKey key, File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public static void encrypt(SecretKey key, String inputString, File outputFile) throws CryptoException {
        try {
            byte[] inputBytes = inputString.getBytes();
            byte[] outputBytes = getCryptoBytes(Cipher.ENCRYPT_MODE, key, inputBytes);
            writeBytesToFile(outputBytes, outputFile);
        }catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    public static void decrypt(SecretKey key, File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    /**
     * Decrypts the input file into the ./data/tmp/{input file name} path
     */
    public static File decrypt(SecretKey key, File inputFile) throws CryptoException, IOException {
        File outFile = new File(String.format("./data/tmp/%s", inputFile.getName()));
        outFile.getParentFile().mkdirs();
        outFile.createNewFile();

        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outFile);
        return outFile;
    }

    public static String getAlgorithm() {
        return CryptoUtils.ALGORITHM;
    }

    // TODO: refactor do crypto into a simpler function
    private static void doCrypto(int cipherMode, SecretKey key, File inputFile, File outputFile) throws CryptoException {
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
            inputStream.close();

            byte[] outputBytes = getCryptoBytes(cipherMode, key, inputBytes);
            writeBytesToFile(outputBytes, outputFile);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    public static byte[] getDecryptedBytes(SecretKey key, File inputFile) throws CryptoException {
        int cipherMode = Cipher.DECRYPT_MODE;
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
            inputStream.close();

            return getCryptoBytes(cipherMode, key, inputBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    private static void writeBytesToFile(byte[] outputBytes,File outputFile) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);
        outputStream.close();
    }

    private static byte[] getCryptoBytes(int cipherMode, SecretKey key, byte[] inputBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, key);

        return cipher.doFinal(inputBytes);
    }

    public static void clearEncryptedFiles() {
        try {
            boolean isSuccess = true;
            File[] files = new File("./data/tmp").listFiles((dir, name) -> name.endsWith(".encrypted"));
            ConsoleLog.info("Attempting delete the tmp files");
            if(files == null) return;
            ConsoleLog.info("Files list was not null...");
            for (File listFile : files) {
                ConsoleLog.info("Deleting file: " + listFile.getName() + "...");
                isSuccess &= listFile.delete();
            }
        }catch (SecurityException e) {
            ConsoleLog.error("There was an error deleting the tmp files\n" + e.getLocalizedMessage());
        }
    }
}
