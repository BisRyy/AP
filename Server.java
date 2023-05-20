import javax.crypto.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.util.Base64;

public class Server {
    public static void main(String[] args) {
        int port = 8443;

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null;
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
            System.out.println("Server started. Listening on port " + port + "...");

            while (true) {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
\
                String encodedData = in.readLine();
                byte[] decodedData = Base64.getDecoder().decode(encodedData);

                String secretKeyString = "Bisrat";
                SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyString.getBytes(), "AES");

                Cipher decipher = Cipher.getInstance("AES");
                decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                byte[] decryptedData = decipher.doFinal(decodedData);

                String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
                String[] decryptedPaymentDetails = decryptedString.split("\\|");

                String decryptedMerchantID = decryptedPaymentDetails[0];
                String decryptedCustomerID = decryptedPaymentDetails[1];
                double decryptedAmount = Double.parseDouble(decryptedPaymentDetails[2]);

                String merchantID = "12344";
                String customerID = "54321";
                double amount = 100.0;

                String response;
                if (merchantID.equals(decryptedMerchantID) && customerID.equals(decryptedCustomerID) && amount == decryptedAmount) {
                    response = "Payment verification successful.";
                } else {
                    response = "Payment verification failed.";
                }

                out.println(response);

                out.close();
                in.close();
                sslSocket.close();
            }
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException |
                IOException | UnrecoverableKeyException | KeyManagementException |
                NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
