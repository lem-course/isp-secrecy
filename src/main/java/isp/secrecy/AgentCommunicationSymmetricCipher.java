package isp.secrecy;

import fri.isp.Agent;
import fri.isp.Environment;

import javax.crypto.KeyGenerator;
import java.security.Key;

/**
 * TASK:
 * Assuming Alice and Bob know a shared secret key in advance, secure the channel using a
 * AES in CBC mode
 * <p>
 * http://docs.oracle.com/javase/10/docs/technotes/guides/security/crypto/CryptoSpec.html#Cipher
 */
public class AgentCommunicationSymmetricCipher {
    public static String AES_CBC = "AES/CBC/PKCS5Padding";

    public static void main(String[] args) throws Exception {
        //STEP 1: Alice and Bob beforehand agree upon a cipher algorithm and a shared secret key
        final Key key = KeyGenerator.getInstance("AES").generateKey();

        // STEP 2: Setup a communication channels
        final Environment env = new Environment();

        env.add(new Agent("alice") {
            @Override
            public void run() {
                final String message = "I love you Bob. Kisses, Alice.";
                /* TODO STEP 3:
                 * Alice creates, encrypts and sends a message
                 *
                 * Do not forget: In CBC mode, one has to also send the IV.
                 * IV can be accessed via the cipher.getIV() call
                 */
            }
        });

        env.add(new Agent("bob") {
            @Override
            public void run() {
                /* TODO STEP 4
                 * Bob receives, decrypts and displays a message.
                 * Once you obtain the byte[] representation of cipher parameters, you can load them with
                 *
                 * IvParameterSpec ivSpec = new IvParameterSpec(iv);
                 * aes.init(Cipher.DECRYPT_MODE, my_key, ivSpec);
                 *
                 * You then pass this object to the cipher init() method call.*
                 */
            }
        });

        env.connect("alice", "bob");
        env.start();
    }
}
