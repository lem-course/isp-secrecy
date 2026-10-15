# Symmetric encryption lab

This Java lab explores encrypted communication, weak-key recovery, and ciphertext
modification. The introductory examples are complete; assignments A1–A5 contain
TODOs for you to implement.

## Setup and running

Install JDK 21 and Maven 3. Check that `java -version` and `mvn -version` both
report the intended JDK. The first build needs network access to download Maven
plugins and the communication library from JitPack.

```sh
mvn compile
```

Open the repository as a Maven project in your IDE and run the `main()` method
of the desired class under `src/main/java/isp/secrecy/`. Each class runs independently.
To run an example from a POSIX terminal instead:

```sh
mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
java -cp "target/classes:$(cat target/classpath.txt)" isp.secrecy.AgentCommunication
```

On Windows, use your IDE
or adapt the classpath separator and file-reading syntax to your shell.
Replace the class name to run another example or exercise. Re-run `mvn compile`
after editing Java files. A successful compilation does not mean the assignments
are complete: the starter TODOs also compile.

## Examples and assignments

| Class | Goal and expected outcome |
| --- | --- |
| `AgentCommunication` | Study agent setup, `send()`, `receive()`, and channel connections. Each agent prints the other's message. |
| `SymmetricCipherExample` | Study key generation and the `Cipher` lifecycle. The final decrypted message matches the original. RC4 is used only as a legacy teaching example. |
| `A1AESInCBCMode` | Complete ten encrypted request–reply rounds using AES-CBC, with fresh unpredictable IVs. |
| `A2AESInCTRMode` | Complete ten encrypted request–reply rounds using AES-CTR, without repeating counter blocks under the shared key. |
| `A3ChaCha20` | Complete ten encrypted request–reply rounds using ChaCha20, with a fresh nonce for every encrypted message. |
| `A4ExhaustiveSearch` | Generate a restricted DES key and demonstration ciphertext, then recover an equivalent key using only the ciphertext, known plaintext, and stated key restriction. |
| `A5ActiveMITM` | Modify intercepted ciphertext so the decrypted recipient line, after trimming, is `isp@gmail.com`; preserve all subsequent message contents. Edit only the student agent's task. |

Read each class's instructions for the full requirements. A1–A3 require twenty
encrypted messages in total: one request and one reply in each of ten rounds.
Both agents must decrypt and display the messages they receive. Parameter
requirements apply across both directions because the key is shared.

## Working and checking your implementation

- Use the two introductory examples as references for communication and crypto API usage.
- Use UTF-8 explicitly when converting between text and bytes. Ciphertext is arbitrary
  binary data; use hexadecimal for logging rather than treating it as text.
- Check that decrypted messages match the originals and that all required rounds finish.
- If an agent appears stuck, inspect which `receive()` is waiting and whether its peer
  sends the expected data in the same order. IVs and nonces need not be secret, but the
  receiver must use the parameters belonging to the ciphertext it decrypts.
- For A4, verify the complete decrypted message. DES parity bits mean equivalent keys
  can have different byte representations; matching the original key bytes is not required.
- For A5, inspect the server's decrypted output. The example simulates message delivery;
  it does not parse FMTP or send real email.

The lab deliberately includes obsolete algorithms and encryption without authentication.
RC4 and DES are not suitable for new applications. CBC, CTR, and plain ChaCha20 do
not authenticate messages; A5 explores why this matters. Complete the assigned
algorithms here rather than substituting a different protocol.

## Java 21 references

- [Cipher](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/javax/crypto/Cipher.html)
- [KeyGenerator](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/javax/crypto/KeyGenerator.html)
- [IvParameterSpec](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/javax/crypto/spec/IvParameterSpec.html)
- [ChaCha20ParameterSpec](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/javax/crypto/spec/ChaCha20ParameterSpec.html)
- [SecretKeySpec](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/javax/crypto/spec/SecretKeySpec.html)
- [Communication library source](https://github.com/lem-course/isp-communication-model)
