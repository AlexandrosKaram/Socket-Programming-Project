/**
 * Represents a message in the system with details about the sender, receiver, body, and read status.
 */
public class Message {
    private boolean isRead;
    private String sender;
    private String receiver;
    private String body;

    /**
     * Default constructor that initializes the message with empty fields and unread status.
     */
    public Message() {
        isRead = false;
        sender = "";
        receiver = "";
        body = "";
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
     * Returns a string representation of the message, including sender, receiver, and body.
     *
     * @return a formatted string representing the message
     */
    @Override
    public String toString() {
        return "From: " + sender + "\nTo: " + receiver + "\n" + body;
    }
}
