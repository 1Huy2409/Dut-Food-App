package Helper;

public class EmailSession {
    private String email;
    private static EmailSession _instance;

    private EmailSession() {}

    public static EmailSession getInstance() {
        if (_instance == null) {
            _instance = new EmailSession();
        }
        return _instance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
