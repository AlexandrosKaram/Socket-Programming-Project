import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Server extends Thread {
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private int portNumber = 5000;

    private int nextAuthToken = 1000;
    private Map<Integer, Account> tokenMap = new HashMap<>();

    private BufferedReader input;
    private PrintWriter output;

    public Server(int port) {
        portNumber = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server running on port " + portNumber);

            // accept multiple clients
            while (true) {
                clientSocket = serverSocket.accept();
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(clientSocket.getOutputStream());

                System.out.println("Client connected at port " + portNumber + " with IP " + clientSocket.getInetAddress());

                String request = input.readLine();
                if (request != null) {
                    String[] parts = request.split(" ", 2);
                    int functionID = Integer.parseInt(parts[0]);
                    String[] requestArgs = parts.length > 1 ? parts[1].split(" ") : new String[0];

                    // Debugging output
                    System.out.println("Function ID: " + functionID);
                    System.out.println("Request Arguments: " + String.join(", ", requestArgs));

                    handleRequest(functionID, requestArgs);
                }

                clientSocket.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void handleRequest(int functionID, String[] requestArgs) {
        try {
            switch (functionID) {
                case 1: // Create Account
                    if (requestArgs.length == 1) {
                        String username = requestArgs[0];
                        String result = createAccount(username);
                        output.println(result);
                    } else {
                        output.println("Invalid arguments for Create Account.");
                    }
                    break;

                case 2: // Show Accounts
                    if (requestArgs.length == 1) {
                        int authToken = Integer.parseInt(requestArgs[0]);
                        String result = showAccounts(authToken);
                        output.println(result);
                    } else {
                        output.println("Invalid arguments for Show Accounts.");
                    }
                    break;

                case 3: // Send Message
                    if (requestArgs.length >= 3) {
                        int authToken = Integer.parseInt(requestArgs[0]);
                        String recipient = requestArgs[1];
                        String body = String.join(" ", Arrays.copyOfRange(requestArgs, 2, requestArgs.length));
                        String result = sendMessage(authToken, recipient, body);
                        output.println(result);
                    } else {
                        output.println("Invalid arguments for Send Message.");
                    }
                    break;

                case 4: // Show Inbox
                    if (requestArgs.length == 1) {
                        int authToken = Integer.parseInt(requestArgs[0]);
                        String result = showInbox(authToken);
                        output.println(result);
                    } else {
                        output.println("Invalid arguments for Show Inbox.");
                    }
                    break;

                case 5: // Read Message
                    if (requestArgs.length == 2) {
                        int authToken = Integer.parseInt(requestArgs[0]);
                        int messageID = Integer.parseInt(requestArgs[1]);
                        String result = readMessage(authToken, messageID);
                        output.println(result);
                    } else {
                        output.println("Invalid arguments for Read Message.");
                    }
                    break;

                case 6: // Delete Message
                    if (requestArgs.length == 2) {
                        int authToken = Integer.parseInt(requestArgs[0]);
                        int messageID = Integer.parseInt(requestArgs[1]);
                        String result = deleteMessage(authToken, messageID);
                        output.println(result);
                    } else {
                        output.println("Invalid arguments for Delete Message.");
                    }
                    break;

                default:
                    output.println("Invalid Function ID.");
                    break;
            }
            output.flush();
        } catch (NumberFormatException e) {
            output.println("Invalid argument format.");
            output.flush();
        }
    }


    public static void main(String[] args) {
        if (!(args.length == 1)) {
            System.out.println("Retry with Server Port in arguments.");
            return;
        }

        try {
            int port = Integer.parseInt(args[0]);
            Server server = new Server(port);
            server.start();
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number, try again with integer.");
        }
    }

    private synchronized String createAccount(String username) {
        if (username == null || !username.matches("[a-zA-Z0-9_]+")) {
            return "Invalid username";
        }

        for (Account account : tokenMap.values()) {
            if (account.getUsername().equals(username)) {
                return "Username already exists, try again.";
            }
        }

        Account user = new Account(username, nextAuthToken);
        tokenMap.put(nextAuthToken, user);
        nextAuthToken++;
        return String.valueOf(nextAuthToken - 1);
    }

    private synchronized String showAccounts(int authToken) {
        if (!tokenMap.containsKey(authToken)) {
            return "Auth token not found. You must be a user to see accounts";
        }

        StringBuilder accounts = new StringBuilder("");
        int i = 1;
        for (Account account : tokenMap.values()) {
            accounts.append(i + ". " + account.getUsername() + "\n");
            i++;
        }

        return String.valueOf(accounts);
    }

    private synchronized String sendMessage(int authToken, String recipient, String body) {
        if (!tokenMap.containsKey(authToken)) {
            return "Auth token not found. You must be a user to see accounts";
        }

        Account recipientAccount = null;
        for (Account account : tokenMap.values()) {
            if (account.getUsername().equals(recipient)) {
                recipientAccount = account;
                break;
            }
        }
        if (recipientAccount == null) {
            return "User does not exist.";
        }

        Message message = new Message();
        message.setSender(tokenMap.get(authToken).getUsername());
        message.setReceiver(recipient);
        message.setBody(body);

        recipientAccount.getMessageBox().add(message);

        return "OK";
    }

    private synchronized String showInbox(int authToken) {
        if (!tokenMap.containsKey(authToken)) {
            return "Auth token not found. You must be a user to see accounts";
        }

        Account tempAcc = tokenMap.get(authToken);
        StringBuilder messageBox = new StringBuilder();
        for (Message message : tempAcc.getMessageBox()) {
            messageBox.append(message.printMessageStatus());
        }
        if (tempAcc.getMessageBox().isEmpty())
            messageBox.append("No messages.");

        return messageBox.toString();
    }

    private synchronized String readMessage(int authToken, int messageID) {
        if (!tokenMap.containsKey(authToken)) {
            return "Auth token not found. You must be a user to see accounts";
        }

        Account tempAcc = tokenMap.get(authToken);
        for (Message message : tempAcc.getMessageBox()) {
            if (message.getId() == messageID) {
                message.setRead(true);
                return message.printMessage();
            }
        }

        return "Message ID does not exist.";
    }

    private synchronized String deleteMessage(int authToken, int messageID) {
        if (!tokenMap.containsKey(authToken)) {
            return "Auth token not found. You must be a user to see accounts";
        }

        Account tempAcc = tokenMap.get(authToken);
        Message tempMessage = null;

        for (Message message : tempAcc.getMessageBox()) {
            if (message.getId() == messageID) {
                tempMessage = message;
                break;
            }
        }

        if (tempMessage == null) {
            return "Message ID does not exist.";
        }

        tempAcc.removeMessage(tempMessage);
        return "OK";
    }

}