import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server extends Thread {
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private int portNumber = 5000;

    private int nextAuthToken = 0;
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
}