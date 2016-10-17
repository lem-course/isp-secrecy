package isp.secrecy;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AgentCommunicationMITM {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        final Key keyDavid2Smtp = KeyGenerator.getInstance("RC4").generateKey();

        final BlockingQueue<byte[]> david2mitm = new LinkedBlockingQueue<>();
        final BlockingQueue<byte[]> mitm2smtp = new LinkedBlockingQueue<>();

        final Agent david = new Agent("DAVID", david2mitm, null, keyDavid2Smtp, "RC4") {
            @Override
            public void execute() throws Exception {
                final String message = "from: ta.david@fri.uni-lj.si\n" +
                        "to: prof.denis@fri.uni-lj.si\n\n" +
                        "Hi! Find attached <some secret stuff>!";

                final Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(Cipher.ENCRYPT_MODE, keyDavid2Smtp);
                final byte[] ct = rc4.doFinal(message.getBytes("UTF-8"));
                print("sending: '%s' (%s)", message, hex(ct));
                outgoing.put(ct);
            }
        };

        final Agent mitm = new Agent("MITM (router)", mitm2smtp, david2mitm, null, null) {
            @Override
            public void execute() throws Exception {
                final byte[] bytes = incoming.take();
                print(" IN: %s", hex(bytes));
                print("OUT: %s", hex(bytes));
                outgoing.put(bytes);
            }
        };

        final Agent smtp = new Agent("SMTP SERVER", null, mitm2smtp, keyDavid2Smtp, "RC4") {
            @Override
            public void execute() throws Exception {
                final byte[] ct = incoming.take();
                final Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(Cipher.DECRYPT_MODE, keyDavid2Smtp);
                final byte[] pt = rc4.doFinal(ct);
                final String message = new String(pt, "UTF-8");

                print("got: '%s' (%s)", message, hex(ct));
            }
        };

        david.start();
        mitm.start();
        smtp.start();
    }
}
