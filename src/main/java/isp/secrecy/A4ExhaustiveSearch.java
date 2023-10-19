package isp.secrecy;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Implement a brute force key search (exhaustive key search) if you know that the
 * message is:
 * "I would like to keep this text confidential Bob. Kind regards, Alice."
 * <p>
 * Assume the message was encrypted with "DES/ECB/PKCS5Padding".
 * Also assume that the key was poorly chosen. In particular, as an attacker,
 * you are certain that all bytes in the key, with the exception of th last three bytes,
 * have been set to 0.
 * <p>
 * The length of DES key is 8 bytes.
 * <p>
 * To manually specify a key, use the class {@link javax.crypto.spec.SecretKeySpec})
 */



public class A4ExhaustiveSearch {
    public static void main(String[] args) throws Exception {
        final String message = "I would like to keep this text confidential Bob. Kind regards, Alice.";
        System.out.println("[MESSAGE] " + message);

        // TODO
        byte[] keyData = { 100,70, 20, 0, 0, 0, 0, 0};

        // Create a SecretKeySpec object from the key data
        SecretKeySpec secretKey = new SecretKeySpec(keyData, "DES");

        final byte[] pt = message.getBytes();

        final Cipher encrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        final byte[] cipherText = encrypt.doFinal(pt);

        bruteForceKey(cipherText, message);
    }

    public static byte[] bruteForceKey(byte[] ct, String message) throws Exception {
        //Task: You know the plain text of the message, as well as the last 5 bytes out of 8 that were used as keydata 
        // in the secretKeySpec. Write an algoritihm that tries to encrypt the ciphertext using all possible key combinations until the 
        // ciphertext is encrypted to the known plain text
         

        for (int byte1 = 0; byte1 < 256; byte1++) {
            for (int byte2 = 0; byte2 < 256; byte2++) {
                for (int byte3 = 0; byte3 < 256; byte3++) {

                    byte[] TrialData = { (byte) byte1, (byte) byte2, (byte) byte3, 0, 0, 0, 0, 0 };
                    SecretKeySpec TrialKey = new SecretKeySpec(TrialData, "DES");

                    final Cipher decrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
                    decrypt.init(Cipher.DECRYPT_MODE, TrialKey);
                    
                    try {
                        final byte[] dt = decrypt.doFinal(ct);
                        final byte[] pt = message.getBytes();

                    if (Arrays.equals(dt, pt)) {
                        System.out.println("Found key:");
                        System.out.println(Arrays.toString(TrialData));
                     // Key found, so we can exit the loop
                        return(TrialData);
                    }
                } catch (javax.crypto.BadPaddingException e) {
                    // BadPaddingException occurs for incorrect keys; continue with the next key
                }
                }
            }
        }
        System.out.println("No key found");
        return null;
    
}}

