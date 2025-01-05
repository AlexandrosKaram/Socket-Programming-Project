/**
 * Represents a message in the system with details about the sender, receiver, body, and read status.
 */
public class Message {
    private boolean isRead;
    private String sender;
    private String receiver;
    private String body;

    private static int idCounter = 0; // Static counter for unique IDs
    private final int id;            // Unique ID for each message

    /**
     * Default constructor that initializes the message with empty fields and unread status.
     */
    public Message() {
        isRead = false;
        sender = "";
        receiver = "";
        body = "";
        this.id = idCounter++;
    }

    /**
     * Constructs a message with specified sender, receiver, and body.
     * The message is initially marked as unread.
     *
     * @param sender   the sender of the message
     * @param receiver the receiver of the message
     * @param body     the body of the message
     */
    public Message(String sender, String receiver, String body) {
        isRead = false;
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
        this.id = idCounter++;
    }

    /**
     * Checks if the message has been read.
     *
     * @return true if the message is read, false otherwise
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * Sets the read status of the message.
     *
     * @param isRead true to mark the message as read, false to mark it as unread
     */
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * Gets the sender of the message.
     *
     * @return the sender of the message
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the sender of the message.
     *
     * @param sender the sender of the message
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Gets the receiver of the message.
     *
     * @return the receiver of the message
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Sets the receiver of the message.
     *
     * @param receiver the receiver of the message
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * Gets the body of the message.
     *
     * @return the body of the message
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body of the message.
     *
     * @param body the body of the message
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Gets the id of the message.
     *
     * @return the id of the message
     */
    public int getId() {return id;}

    public String printMessageStatus() {
        StringBuilder message = new StringBuilder();
        message.append(id + ". from: ");
        message.append(sender);
        message.append(!isRead ? "*" : "");

        return message.toString();
    }

    public String printMessage() {
        return ("(" + sender + ")" + " " + body);
    }
}
