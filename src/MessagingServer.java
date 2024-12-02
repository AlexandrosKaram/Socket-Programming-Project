import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Represents a multi-threaded server for managing user accounts and messages.
 */
public class MessagingServer {
    private int port;
    private Map<Integer, Account> accounts; // Map with authToken as key and Account as value
    private int tokenCounter = 1000; // Initial token counter

    /**
     * Constructor to initialize the server on a given port.
     *
     * @param port the port number for the server
     */
    public MessagingServer(int port) {
        this.port = port;
        this.accounts = new HashMap<>();
    }

    /**
     * Starts the server to accept and handle client connections.
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Messaging Server started on port " + port);

            while (true) {
                System.out.println("Waiting for a client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                // Handle client connection in a separate thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a unique auth token for new accounts.
     *
     * @return a unique auth token
     */
    private synchronized int generateAuthToken() {
        return tokenCounter++;
    }

    /**
     * Handles client requests in a separate thread.
     */
    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                while (true) {
                    String functionId = input.readUTF(); // Read the function ID
                    switch (functionId) {
                        case "1": // Create Account
                            String username = input.readUTF();
                            if (isValidUsername(username)) {
                                if (getAccountByUsername(username) != null) {
                                    output.writeUTF("Sorry, the user already exists");
                                } else {
                                    int authToken = generateAuthToken();
                                    Account newAccount = new Account(username);
                                    accounts.put(authToken, newAccount);
                                    output.writeUTF(String.valueOf(authToken));
                                }
                            } else {
                                output.writeUTF("Invalid Username");
                            }
                            break;

                        case "2": // Show Accounts
                            int authToken = Integer.parseInt(input.readUTF());
                            if (accounts.containsKey(authToken)) {
                                StringBuilder accountList = new StringBuilder();
                                int i = 1;
                                for (Account account : accounts.values()) {
                                    accountList.append(i++).append(". ").append(account.getUsername()).append("\n");
                                }
                                output.writeUTF(accountList.toString());
                            } else {
                                output.writeUTF("Invalid Auth Token");
                            }
                            break;

                        case "3": // Send Message
                            authToken = Integer.parseInt(input.readUTF());
                            if (accounts.containsKey(authToken)) {
                                String recipient = input.readUTF();
                                String messageBody = input.readUTF();
                                Account recipientAccount = getAccountByUsername(recipient);
                                if (recipientAccount != null) {
                                    Message message = new Message(accounts.get(authToken).getUsername(), recipient, messageBody);
                                    recipientAccount.addMessage(message);
                                    output.writeUTF("OK");
                                } else {
                                    output.writeUTF("User does not exist");
                                }
                            } else {
                                output.writeUTF("Invalid Auth Token");
                            }
                            break;

                        case "4": // Show Inbox
                            authToken = Integer.parseInt(input.readUTF());
                            if (accounts.containsKey(authToken)) {
                                Account account = accounts.get(authToken);
                                List<Message> messages = account.getMessageBox();
                                StringBuilder inbox = new StringBuilder();
                                for (Message message : messages) {
                                    inbox.append(message.getSender()).append(": ").append(message.getBody()).append("\n");
                                }
                                output.writeUTF(inbox.toString());
                            } else {
                                output.writeUTF("Invalid Auth Token");
                            }
                            break;

                        case "0": // Exit
                            output.writeUTF("Goodbye!");
                            return;

                        default:
                            output.writeUTF("Invalid Function ID");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Validates if the username is valid.
         *
         * @param username the username to validate
         * @return true if valid, false otherwise
         */
        private boolean isValidUsername(String username) {
            return username.matches("[a-zA-Z0-9_]+");
        }

        /**
         * Retrieves an account by username.
         *
         * @param username the username to search for
         * @return the account if found, null otherwise
         */
        private Account getAccountByUsername(String username) {
            for (Account account : accounts.values()) {
                if (account.getUsername().equals(username)) {
                    return account;
                }
            }
            return null;
        }
    }

    /**
     * Entry point to start the messaging server.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        MessagingServer server = new MessagingServer(5000);
        server.start();
    }
}
