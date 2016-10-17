package isp.secrecy;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TASK:
 * Assuming Alice and Bob know a shared secret key in advance, secure the channel using a
 * AES in CBC mode
 * <p>
 * http://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#Cipher
 */
public class AgentCommunicationSymmetricCipher {
    public static String[] AES_CBC = {"AES", "AES/CBC/PKCS5Padding"};

    public static void main(String[] args) throws Exception {
        //STEP 1: Alice and Bob beforehand agree upon a cipher algorithm and a shared secret key
        final Key key = KeyGenerator.getInstance(AES_CBC[0]).generateKey();

        // STEP 2: Setup an insecure communication channels
        final BlockingQueue<byte[]> alice2bob = new LinkedBlockingQueue<>();
        final BlockingQueue<byte[]> bob2alice = new LinkedBlockingQueue<>();

        /* TODO STEP 3:
         * Alice creates, encrypts and sends a message
         *
         * Do not forget: In CBC mode, one has to also send the IV.
         * IV can be accessed via the cipher.getIV() call
         */
        final Agent alice = new Agent("alice", alice2bob, bob2alice, key, AES_CBC[1]) {
            @Override
            public void execute() throws Exception {
                final String message = "I love you Bob. Kisses, Alice.";
                final byte[] pt = message.getBytes("UTF-8");
                Cipher aes = Cipher.getInstance(AES_CBC[1]);
                aes.init(Cipher.ENCRYPT_MODE, cipherKey);

                final byte[] iv = aes.getIV();
                final byte[] ct = aes.doFinal(pt);

                print("Sending: '%s' (%s, %s)", message, hex(iv), hex(ct));
                outgoing.put(iv);
                outgoing.put(ct);
            }
        };

        /* TODO STEP 4
         * Bob receives, decrypts and displays a message.
         * Once you obtain the byte[] representation of cipher parameters, you can load them with
         *
         * IvParameterSpec ivSpec = new IvParameterSpec(iv);
         * aes.init(Cipher.DECRYPT_MODE, my_key, ivSpec);
         *
         * You then pass this object to the cipher init() method call.*
         */
        final Agent bob = new Agent("bob", bob2alice, alice2bob, key, AES_CBC[1]) {
            @Override
            public void execute() throws Exception {
                // TODO
                final byte[] iv = incoming.take();
                final byte[] ct = incoming.take();

                final Cipher aes = Cipher.getInstance(AES_CBC[1]);
                aes.init(Cipher.DECRYPT_MODE, cipherKey, new IvParameterSpec(iv));
                final byte[] pt = aes.doFinal(ct);

                final String message = new String(pt, "UTF-8");

                print("Received: '%s' (%s, %s)", message, hex(iv), hex(ct));
            }
        };

        alice.start();
        bob.start();
    }
}
