public class Message {
    private boolean isRead;
    private String sender;
    private String receiver;
    private String body;

    public Message() {
        isRead = false;
        sender = "";
        receiver = "";
        body = "";
    }

    public Message(String sender, String receiver, String body) {
        isRead = false;
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return "From: " + sender + "\nTo: " + receiver + "\n" + body;
    }
}
