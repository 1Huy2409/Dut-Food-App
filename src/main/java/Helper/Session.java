package Helper;

public class Session {
    private static Session instance;
    private int id;
    private String phone;
    private String fullName;
    private String email;
    private String userName;
    private int roleId;
    private Session() {};
    public static Session getInstance()
    {
        if (instance == null)
        {
            instance = new Session();
        }
        return instance;
    }
    public void setUser(String phone, String fullName, String email, String userName, int roleId)
    {
        this.phone = phone;
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;
        this.roleId = roleId;
    }
    public String getPhone()
    {
        return this.phone;
    }
    public String getFullName()
    {
        return this.fullName;
    }
    public String getEmail()
    {
        return this.email;
    }
    public String getUserName()
    {
        return this.userName;
    }
    public int getRoleId()
    {
        return this.roleId;
    }
    public void clearSession()
    {
        instance = null;
    }
}
