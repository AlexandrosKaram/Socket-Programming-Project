import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user account with a unique username and authentication token.
 * Each account maintains a message box for storing messages.
 */
public class Account {
    private String username;
    private int authToken;
    private List<Message> messageBox;

    private static int tokenCounter = 0;

    /**
     * Default constructor that creates an account with a default username.
     */
    public Account() {
        this("default_user_" + (tokenCounter + 1));
    }

    /**
     * Constructor to create an account with a specific username.
     *
     * @param username the username of the account
     */
    public Account(String username) {
        this.username = username;
        this.authToken = generateAuthToken();
        this.messageBox = new ArrayList<>();
    }

    /**
     * Generates a unique authentication token for each account.
     *
     * @return a unique authentication token
     */
    private static synchronized int generateAuthToken() {
        return ++tokenCounter;
    }

    /**
     * Gets the username of the account.
     *
     * @return the username of the account
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the authentication token of the account.
     *
     * @return the authentication token of the account
     */
    public int getAuthToken() {
        return authToken;
    }

    /**
     * Gets the message box of the account.
     *
     * @return the message box of the account
     */
    public List<Message> getMessageBox() {
        return messageBox;
    }

    /**
     * Adds a message to the message box.
     *
     * @param message the message to be added
     */
    public void addMessage(Message message) {
        messageBox.add(message);
    }

    /**
     * Validates the username to ensure it contains only alphanumeric characters and underscores.
     *
     * @param username the username to be validated
     * @return true if the username is valid (contains only letters, digits, and underscores),
     *         false otherwise
     */
    private boolean isValidUsername(String username) {
        return username.matches("[a-zA-Z0-9_]+");
    }

}
