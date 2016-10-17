package isp.secrecy;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Random;

/**
 * Implement a brute force key search (exhaustive key search) if you know that the
 * message is:
 * "I would like to keep this text confidential Bob. Kind regards, Alice."
 * <p>
 * Assume the message was encrypted with "DES/ECB/PKCS5Padding".
 * Also assume, the the key has be very poorly chosen. In particular, as an attacker,
 * you are certain that all bytes in the key, with the exception of th last three bytes,
 * have been set to 0.
 * <p>
 * The length of DES key is 8 bytes.
 * <p>
 * To manually specify a key, use the class {@link javax.crypto.spec.SecretKeySpec})
 */
public class ExhaustiveSearch {
    public static void main(String[] args) throws Exception {
        final String message = "I would like to keep this text confidential Bob. Kind regards, Alice.";
        System.out.println("[MESSAGE] " + message);

        final byte[] keyBytes = new byte[8];
        final Random rnd = new Random();
        keyBytes[0] = (byte) rnd.nextInt(256);
        keyBytes[1] = (byte) rnd.nextInt(256);
        keyBytes[2] = (byte) rnd.nextInt(256);
        final SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");

        final byte[] pt = message.getBytes("UTF-8");
        System.out.println("[PT] " + DatatypeConverter.printHexBinary(pt));

        final Cipher encryption = Cipher.getInstance("DES/ECB/PKCS5Padding");
        encryption.init(Cipher.ENCRYPT_MODE, key);
        final byte[] ct = encryption.doFinal(pt);

        System.out.println("[CT] " + DatatypeConverter.printHexBinary(ct));

        final byte[] foundKey = bruteforceKey(ct, message);

        if (foundKey != null) {
            System.out.println(DatatypeConverter.printHexBinary(foundKey));
        }
    }

    public static byte[] bruteforceKey(byte[] ct, String message) throws Exception {
        // TODO
        final byte[] attempt = new byte[8];
        final Cipher oscar = Cipher.getInstance("DES/ECB/PKCS5Padding");

        for (int k = -128; k <= 127; k++) {
            for (int j = -128; j <= 127; j++) {
                for (int i = -128; i <= 127; i++) {

                    attempt[0] = (byte) i;
                    attempt[1] = (byte) j;
                    attempt[2] = (byte) k;

                    oscar.init(Cipher.DECRYPT_MODE, new SecretKeySpec(attempt, "DES"));
                    try {
                        if (Arrays.equals(oscar.doFinal(ct), message.getBytes("UTF-8"))) {
                            return attempt;
                        }
                    } catch (Exception e) {
                    }

                }
            }
        }

        return null;
    }
}
