import javax.crypto.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.util.*;

public class Client {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8443;

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);

            sslSocket.startHandshake();

            PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

            double amount = 100.0;
            String merchantID = "12345";
            String customerID = "54321";

            String secretKeyString = "Bisrat";
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyString.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedData = cipher.doFinal((merchantID + "|" + customerID + "|" + amount).getBytes(StandardCharsets.UTF_8));

            String encodedData = Base64.getEncoder().encodeToString(encryptedData);

            out.println(encodedData);

            String response = in.readLine();
            System.out.println("Server response: " + response);

            out.close();
            in.close();
            sslSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
