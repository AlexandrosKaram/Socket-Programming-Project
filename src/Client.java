import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Client extends Thread {
    private String ipAddress;
    private int portNumber;
    private Socket socket;
    private String[] requestArgs;
    private int functionID;

    private BufferedReader input;
    private PrintWriter output;

    public Client(String ipAddress, int portNumber, int functionID, String[] requestArgs) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.functionID = functionID;
        this.requestArgs = requestArgs;
    }

    public void run() {
        try {
            socket = new Socket(ipAddress, portNumber);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Successful connection at port " + portNumber);
            sendRequest();
            String response = receiveResponse();
            System.out.println("Server response:\n" + response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Unable to connect, enter valid arguments");
            return;
        }

        try {
            String ipAddress = args[0];
            int portNumber = Integer.parseInt(args[1]);
            int functionID = Integer.parseInt(args[2]);
            String[] requestArguments = Arrays.copyOfRange(args, 3, args.length);

            Client client = new Client(ipAddress, portNumber, functionID, requestArguments);
            client.start();
        } catch (NumberFormatException e) {
            System.out.println("Provide valid integer for port number");
        }
    }

    private void sendRequest() {
        try {
            output.println(functionID + " " + String.join(" ", requestArgs));
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String receiveResponse() {
        StringBuilder response = new StringBuilder();
        String line;

        try {
            // Read data from the input stream
            while ((line = input.readLine()) != null) {
                response.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}