package isp.secrecy;

import fri.isp.Agent;
import fri.isp.Environment;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

import java.security.Key;

/**
 * TASK:
 * Assuming Alice and Bob know a shared secret key in advance, secure the channel using
 * AES in CBC mode. Then exchange ten messages between Alice and Bob.
 * <p>
 * https://docs.oracle.com/en/java/javase/11/docs/api/java.base/javax/crypto/Cipher.html
 */
public class A1AESInCBCMode {

    private static byte[] cipherText;  // Class-level variable to hold cipher text
    private static byte[] iv;  // Class-level variable to hold IV

    public static void main(String[] args) throws Exception {
        // STEP 1: Alice and Bob beforehand agree upon a cipher algorithm and a shared secret key
        // This key may be accessed as a global variable by both agents
        final Key key = KeyGenerator.getInstance("AES").generateKey();

        // STEP 2: Setup communication
        final Environment env = new Environment();

        env.add(new Agent("alice") {
            @Override
            public void task() throws Exception {
                final String message = "I love you Bob. Kisses, Alice.";
                /* TODO STEP 3:
                 * Alice creates, encrypts and sends a message to Bob. Bob replies to the message.
                 * Such exchange repeats 10 times.
                 *
                 * Do not forget: In CBC (and CTR mode), you have to also
                 * send the IV. The IV can be accessed via the
                 * cipher.getIV() call
                 */
                final byte[] pt = message.getBytes();
                
                for (int i = 0; i < 10; i++) {
                    final Cipher encrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    encrypt.init(Cipher.ENCRYPT_MODE, key);

                    cipherText = encrypt.doFinal(pt);
                    iv = encrypt.getIV();
                    //System.out.println("[iv] " + Agent.hex(iv));
                
                    send("bob", iv);
                    send("bob", cipherText);

                    byte[] iv = receive("bob");
                    byte[] cipherText = receive("bob");

                    final Cipher decrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    IvParameterSpec ivSpec = new IvParameterSpec(iv);
                    decrypt.init(Cipher.DECRYPT_MODE, key, ivSpec);
                    final byte[] dt1 = decrypt.doFinal(cipherText);

                    System.out.println("[Message]" + new String(dt1));
                }

                

            }
        });

        env.add(new Agent("bob") {
            @Override
            public void task() throws Exception {
                /* TODO STEP 4
                 * Bob receives, decrypts and displays a message.
                 * Once you obtain the byte[] representation of cipher parameters,
                 * you can load them with:
                 *
                 *   IvParameterSpec ivSpec = new IvParameterSpec(iv);
                 *   aes.init(Cipher.DECRYPT_MODE, my_key, ivSpec);
                 *
                 * You then pass this object to the cipher init() method call.*
                 */
                final String message1 = "I love you too";
                final byte[] pt1 = message1.getBytes();
                
                for (int i = 0; i < 10; i++) {

                    byte[] iv = receive("alice");
                    byte[] cipherText = receive("alice");

                    final Cipher decrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    IvParameterSpec ivSpec = new IvParameterSpec(iv);
                    decrypt.init(Cipher.DECRYPT_MODE, key, ivSpec);
                    final byte[] dt = decrypt.doFinal(cipherText);

                    System.out.println("[Message]" + new String(dt));

                    final Cipher encrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    encrypt.init(Cipher.ENCRYPT_MODE, key);

                    cipherText = encrypt.doFinal(pt1);
                    iv = encrypt.getIV();
                    //System.out.println("[iv] " + Agent.hex(iv));
                
                    send("alice", iv);
                    send("alice", cipherText);


                }
            }
        });

        env.connect("alice", "bob");
        env.start();
    }
}
