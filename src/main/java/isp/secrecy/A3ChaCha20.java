package isp.secrecy;

import fri.isp.Agent;
import fri.isp.Environment;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.ChaCha20ParameterSpec;
import java.nio.ByteBuffer;

import java.security.Key;
import java.security.SecureRandom;

/**
 * TASK:
 * Assuming Alice and Bob know a shared secret key in advance, secure the channel using
 * ChaCha20 stream cipher. Then exchange ten messages between Alice and Bob.
 * <p>
 * https://docs.oracle.com/en/java/javase/11/docs/api/java.base/javax/crypto/Cipher.html
 */
public class A3ChaCha20 {

    private static byte[] cipherText;
          // Class-level variable to hold cipher text
    
    public static void main(String[] args) throws Exception {
        // STEP 1: Alice and Bob beforehand agree upon a cipher algorithm and a shared secret key
        // This key may be accessed as a global variable by both agents
        final Key key = KeyGenerator.getInstance("ChaCha20").generateKey();

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
                 * Recall, ChaCha2 requires that you specify the nonce and the counter explicitly.
                 */

                final byte[] pt = message.getBytes();

                //create nonce


                for (int i = 0; i < 10; i++) {
                    // Alice sends message
                    final Cipher encrypt = Cipher.getInstance("ChaCha20");

                    final byte[] nonce = new byte [12];
                    new SecureRandom().nextBytes(nonce);
                    
                    final int counter = i;
                    byte[] CounterArray = ByteBuffer.allocate(4).putInt(counter).array();
            
                    encrypt.init(Cipher.ENCRYPT_MODE, key, new ChaCha20ParameterSpec(nonce, counter));

                    cipherText = encrypt.doFinal(pt);

                    send("bob", cipherText);
                    send("bob", nonce);
                    send("bob", CounterArray);

                    //Alice receives message
                    byte[] cipherTextB = receive("bob");
                    byte[] nonceB = receive("bob");
                    byte[] CounterArrayB = receive("bob");

                    int counterB = ByteBuffer.wrap(CounterArrayB).getInt();

                    final Cipher decrypt = Cipher.getInstance("ChaCha20");
                    decrypt.init(Cipher.DECRYPT_MODE, key, new ChaCha20ParameterSpec(nonceB, counterB));
                    final byte[] dt1 = decrypt.doFinal(cipherTextB);

                    System.out.println("[MessageB]" + new String(dt1));

                    
                }
            }
    });

        env.add(new Agent("bob") {
            @Override
            public void task() throws Exception {
                // TODO
                final String message1 = "I love you too";
                final byte[] pt1 = message1.getBytes();
                
                for (int i = 0; i < 10; i++) {
                    // Bob receives message
                    byte[] cipherText = receive("alice");
                    byte[] nonce = receive("alice");
                    byte[] CounterArray = receive("alice");

                    int counter = ByteBuffer.wrap(CounterArray).getInt();

                    final Cipher decrypt = Cipher.getInstance("ChaCha20");
                    decrypt.init(Cipher.DECRYPT_MODE, key, new ChaCha20ParameterSpec(nonce, counter));
                    final byte[] dt = decrypt.doFinal(cipherText);

                    System.out.println("[Message]" + new String(dt));

                    // Bob send message
                    final Cipher encrypt = Cipher.getInstance("ChaCha20");

                    final byte[] nonceB = new byte [12];
                    new SecureRandom().nextBytes(nonceB);
                    
                    final int counterB = i;
                    byte[] CounterArrayB = ByteBuffer.allocate(4).putInt(counterB).array();
            
                    encrypt.init(Cipher.ENCRYPT_MODE, key, new ChaCha20ParameterSpec(nonceB, counterB));

                    cipherText = encrypt.doFinal(pt1);
                    

                    send("alice", cipherText);
                    send("alice", nonceB);
                    send("alice", CounterArrayB);
                
                }
            }
        });

        env.connect("alice", "bob");
        env.start();
    }
}
