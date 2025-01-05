import java.util.ArrayList;
import java.util.List;

public class Account {
    private String username = null;
    private int authToken;
    private List<Message> messageBox = new ArrayList<>();

    public Account(String username, int authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public void setUsername(String username) {this.username = username;}
    public String getUsername() {return username;}

    public void setAuthToken(int authToken) {this.authToken = authToken;}
    public int getAuthToken() {return authToken;}

    public void setMessageBox(List<Message> messageBox) {this.messageBox = messageBox;}
    public List<Message> getMessageBox() {return messageBox;}

    public void addMessage(Message message) {
        messageBox.add(message);
    }

    public void clearMessages() {
        messageBox.clear();
    }

    public void removeMessage(Message message) {
        messageBox.remove(message);
    }
}