package isp.secrecy;

import fri.isp.Agent;
import fri.isp.Environment;
import java.nio.charset.StandardCharsets;

/**
 * A communication channel is implemented with a thread-safe blocking queue.
 * <p/>
 * Both agents are implemented as anonymous subclasses of Agent,
 * overriding task().
 * <p/>
 * Both agents are started at the end of the main method definition below.
 */
public class AgentCommunication {
    public static void main(String[] args) {
        final Environment env = new Environment();

        env.add(new Agent("alice") {
            @Override
            public void task() {
                final byte[] payload = "Hi, Bob, this is Alice.".getBytes(StandardCharsets.UTF_8);
                send("bob", payload);
                final byte[] received = receive("bob");
                print("Got '%s', converted to string: '%s'", hex(received), new String(received, StandardCharsets.UTF_8));
            }
        });

        env.add(new Agent("bob") {
            @Override
            public void task() {
                send("alice", "Hey Alice, Bob here.".getBytes(StandardCharsets.UTF_8));
                print("Got '%s'", new String(receive("alice"), StandardCharsets.UTF_8));
            }
        });

        env.connect("alice", "bob");
        env.start();
    }
}
