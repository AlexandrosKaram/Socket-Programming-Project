import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Represents a client for the messaging system.
 * Allows the user to interact with the messaging server.
 */
public class MessagingClient {
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    /**
     * Constructor to initialize the client with server details.
     *
     * @param serverAddress the IP address of the server
     * @param serverPort    the port of the server
     */
    public MessagingClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    /**
     * Starts the client and handles user interaction.
     */
    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            // Connect to the server
            socket = new Socket(serverAddress, serverPort);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected to the server.");

            // Main interaction loop
            while (true) {
                System.out.println("\nAvailable Functions:");
                System.out.println("1: Create Account");
                System.out.println("2: Show Accounts");
                System.out.println("3: Send Message");
                System.out.println("4: Show Inbox");
                System.out.println("0: Exit");

                System.out.print("Enter function ID: ");
                String functionId = scanner.nextLine();

                if ("0".equals(functionId)) {
                    System.out.println("Exiting...");
                    output.writeUTF(functionId);
                    break;
                }

                // Process the selected function
                processFunction(functionId, scanner);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    /**
     * Processes the user-selected function and communicates with the server.
     *
     * @param functionId the ID of the function
     * @param scanner    the Scanner for user input
     * @throws IOException if communication with the server fails
     */
    private void processFunction(String functionId, Scanner scanner) throws IOException {
        output.writeUTF(functionId); // Send function ID to server

        switch (functionId) {
            case "1": // Create Account
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                output.writeUTF(username);
                System.out.println("Server Response: " + input.readUTF());
                break;

            case "2": // Show Accounts
                System.out.print("Enter your auth token: ");
                String authToken = scanner.nextLine();
                output.writeUTF(authToken);
                System.out.println("Server Response:\n" + input.readUTF());
                break;

            case "3": // Send Message
                System.out.print("Enter your auth token: ");
                authToken = scanner.nextLine();
                output.writeUTF(authToken);
                System.out.print("Enter recipient username: ");
                String recipient = scanner.nextLine();
                output.writeUTF(recipient);
                System.out.print("Enter message body: ");
                String messageBody = scanner.nextLine();
                output.writeUTF(messageBody);
                System.out.println("Server Response: " + input.readUTF());
                break;

            case "4": // Show Inbox
                System.out.print("Enter your auth token: ");
                authToken = scanner.nextLine();
                output.writeUTF(authToken);
                System.out.println("Server Response:\n" + input.readUTF());
                break;

            default:
                System.out.println("Invalid Function ID.");
        }
    }

    /**
     * Closes the socket and streams.
     */
    private void closeConnections() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Entry point for the MessagingClient.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        MessagingClient client = new MessagingClient("127.0.0.1", 5000);
        client.start();
    }
}
