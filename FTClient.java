import java.io.*;
import java.net.*;

public class FileTransferClient {

    public static void main(String[] args) {
        String serverAddress = "localhost";  // IP address of the server
        int port = 1234;  // Port number for the server
        String savePath = "file_to_send.txt";  // Path where to save the received file

        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Connected to server.");

            // Create input and output streams
            try (InputStream serverInputStream = socket.getInputStream();
                 DataInputStream dataInputStream = new DataInputStream(serverInputStream);
                 FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {

                // Receive the file size
                long fileLength = dataInputStream.readLong();
                System.out.println("Receiving file of size: " + fileLength + " bytes.");

                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytesRead = 0;

                // Read the file in chunks and save it to the local file
                while ((bytesRead = serverInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;

                    if (totalBytesRead >= fileLength) {
                        break;
                    }
                }

                System.out.println("File received and saved successfully.");

            } catch (IOException e) {
                System.err.println("Error while receiving the file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
