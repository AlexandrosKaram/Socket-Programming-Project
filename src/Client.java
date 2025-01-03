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

    private BufferedReader input;
    private PrintWriter output;

    public Client(String ipAddress, int portNumber, String[] requestArgs) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.requestArgs = requestArgs;
    }

    public void run() {
        try {
            socket = new Socket(ipAddress, portNumber);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Successful connection at port " + portNumber);
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
            String[] requestArguments = Arrays.copyOfRange(args, 2, args.length);

            Client client = new Client(ipAddress, portNumber, requestArguments);
            client.start();
        } catch (NumberFormatException e) {
            System.out.println("Provide valid integer for port number");
        }
    }
}