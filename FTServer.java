import java.io.*;
import java.net.*;

public class FileTransferServer {

    public static void main(String[] args) {
        int port = 1234;  // Port number for the server
        String filePath = "karthik.txt";  // Path to the file you want to send

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started, waiting for client...");

            // Accept client connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Create input and output streams
            try (DataInputStream fileInputStream = new DataInputStream(new FileInputStream(filePath));
                 OutputStream clientOutputStream = clientSocket.getOutputStream()) {

                // Send the file size first
                File file = new File(filePath);
                long fileLength = file.length();
                DataOutputStream dataOutputStream = new DataOutputStream(clientOutputStream);
                dataOutputStream.writeLong(fileLength);
                dataOutputStream.flush();

                byte[] buffer = new byte[4096];
                int bytesRead;

                // Send the file contents
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    clientOutputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("File sent successfully.");

            } catch (IOException e) {
                System.err.println("Error while sending the file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
