package isp.secrecy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A communication channel is implemented with thread-safe blocking queue.
 * <p/>
 * Both agents are implemented by extending the Agents class,
 * creating anonymous class and overriding #execute().
 * <p/>
 * Both agents are started at the end of the main method definition below.
 */
public class AgentCommunication {
    public static void main(String[] args) {
        final BlockingQueue<byte[]> alice2bob = new LinkedBlockingQueue<>();
        final BlockingQueue<byte[]> bob2alice = new LinkedBlockingQueue<>();

        final Agent alice = new Agent("alice", alice2bob, bob2alice, null, null) {
            @Override
            public void execute() throws Exception {
                final String message = "I love you Bob. Kisses, Alice.";
                final byte[] bytes = message.getBytes("UTF-8");

                print("Sending: '%s' (HEX: %s)", message, hex(bytes));
                outgoing.put(bytes);
            }
        };

        final Agent bob = new Agent("bob", bob2alice, alice2bob, null, null) {
            @Override
            public void execute() throws Exception {
                final byte[] bytes = incoming.take();
                final String message = new String(bytes, "UTF-8");
                print("Received: '%s' (HEX: %s)", message, hex(bytes));
            }
        };

        // start both threads
        bob.start();
        alice.start();
    }
}
