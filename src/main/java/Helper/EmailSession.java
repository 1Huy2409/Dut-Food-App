package Helper;

public class EmailSession {
    private String email;
    private static EmailSession _instance;

    private EmailSession() {} // 🔹 Private constructor để ngăn tạo đối tượng bên ngoài

    public static EmailSession getInstance() {
        if (_instance == null) {
            _instance = new EmailSession(); // ✅ Gán _instance để dùng chung
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
