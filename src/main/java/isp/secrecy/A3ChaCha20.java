package isp.secrecy;

import java.security.Key;

import javax.crypto.KeyGenerator;

import fri.isp.Agent;
import fri.isp.Environment;

/**
 * TASK:
 * Assuming Alice and Bob know a shared secret key in advance, encrypt their messages
 * using the ChaCha20 stream cipher to provide confidentiality. Perform ten request-reply rounds:
 * Alice sends one encrypted message and Bob sends one encrypted reply in each round
 * (twenty encrypted messages in total). Both agents must decrypt and display received messages.
 * This exercise does not provide message authentication.
 * <p>
 * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/javax/crypto/Cipher.html
 * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/javax/crypto/spec/ChaCha20ParameterSpec.html
 */
public class A3ChaCha20 {
    public static void main(String[] args) throws Exception {
        // STEP 1: Alice and Bob beforehand agree upon a cipher algorithm and a shared secret key
        // Both agents can access this shared key from the enclosing method
        final Key key = KeyGenerator.getInstance("ChaCha20").generateKey();

        // STEP 2: Setup communication
        final Environment env = new Environment();

        env.add(new Agent("alice") {
            @Override
            public void task() throws Exception {
                final String message = "I love you Bob. Kisses, Alice.";
                /* TODO STEP 3:
                 * Alice creates, encrypts and sends a message to Bob. Bob replies to the message.
                 * Repeat for ten request-reply rounds.
                 *
                 * For this exercise, explicitly specify a 12-byte nonce and an initial block
                 * counter using ChaCha20ParameterSpec. Use a fresh nonce for every encryption,
                 * including Bob's replies: nonces must not repeat under the shared key.
                 * Make the nonce and initial counter available to the receiver; neither is secret.
                 */
            }
        });

        env.add(new Agent("bob") {
            @Override
            public void task() throws Exception {
                // TODO: Receive, decrypt and display Alice's message, then encrypt and send
                // a reply with a fresh nonce. Repeat for ten request-reply rounds.
            }
        });

        env.connect("alice", "bob");
        env.start();
    }
}
