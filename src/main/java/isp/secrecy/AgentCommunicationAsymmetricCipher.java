package isp.secrecy;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * EXERCISE
 * An agent communication example. Message Confidentiality is provided using asymmetric
 * cypher algorithm.
 * <p>
 * IMPORTANT: This is an insecure example. One should never encrypt with a TDF (such as RSA) directly.
 * Such construction is deterministic and many known attacks against it exist.
 */

public class AgentCommunicationAsymmetricCipher {

    public static void main(String[] args) throws Exception {
        final String encryptionAlg = "RSA";

        /**
         * STEP 1.
         * Bob creates his key pair and public and private key. Alice receives Bob's public key securely.
         */
        final KeyPair bobKP = KeyPairGenerator.getInstance(encryptionAlg).generateKeyPair();

        /**
         * STEP 2.
         * Setup an insecure communication channel.
         */
        final BlockingQueue<byte[]> alice2bob = new LinkedBlockingQueue<>();
        final BlockingQueue<byte[]> bob2alice = new LinkedBlockingQueue<>();

        /**
         * STEP 3.
         * Alice:
         * - creates a message,
         * - encrypts it using Bob's public key
         * - sends it to Bob
         */
        final Agent alice = new Agent("alice", alice2bob, bob2alice, bobKP.getPublic(), encryptionAlg) {
            @Override
            public void execute() throws Exception {
                // STEP 3.1:  Alice creates a message
                final String text = "I love you Bob. Kisses, Alice.";
                final byte[] pt = text.getBytes("UTF-8");

                // TODO STEP 3.2: Alice encrypts the text using Bob's public key.

                // TODO STEP 3.3: Alice logs the act of sending the message

                // TODO STEP 3.4: Send the message across the channel
            }
        };

        /**
         * STEP 4.
         * Bob:
         * - waits for a message from Alice
         * - upon receiving it, uses his private key to decrypt it
         */
        final Agent bob = new Agent("bob", bob2alice, alice2bob, bobKP.getPrivate(), encryptionAlg) {
            @Override
            public void execute() throws Exception {
                // STEP 4.1: Bob receives the cipher text
                final byte[] ct = incoming.take();

                // TODO STEP 4.2: Bob decrypts the cipher text

                // TODO STEP 4.3: Bob creates a string from the decrypted byte array

                // TODO STEP 4.4: Bob displays the text
            }
        };

        alice.start();
        bob.start();
    }
}
