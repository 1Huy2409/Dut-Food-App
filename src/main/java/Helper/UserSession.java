package Helper;

public class UserSession {
    private static UserSession instance;
    private int id;
    private String phone;
    private String fullName;
    private String email;
    private String userName;
    private boolean status;
    private int roleId;
    private UserSession() {};
    public static UserSession getInstance()
    {
        if (instance == null)
        {
            instance = new UserSession();
        }
        return instance;
    }
    public void setUser(int id, String phone, String fullName, String email, String userName, boolean status, int roleId)
    {
        this.id = id;
        this.phone = phone;
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;
        this.status = status;
        this.roleId = roleId;
    }
    public int getId(){return this.id;}
    public String getPhone()
    {
        return this.phone;
    }
    public boolean getStatus()
    {
        return this.status;
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
