package Model;

public class User {
    int id;
    String phone, fullName, email, userName, passWord;
    int roleId;
    boolean status;
    // constructor
    public User()
    {
        this.status = true;
    }
    public User(int id, String phone, String fullName, String email, String userName, String passWord, int roleId, boolean status)
    {
        this.id = id;
        this.phone = phone;
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;
        this.passWord = passWord;
        this.roleId = roleId;
        this.status = status;
    }
    // getter;
    public String getUserName()
    {
        return this.userName;
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
    public String getPassWord()
    {
        return this.passWord;
    }
    public int getId()
    {
        return this.id;
    }
    public int getRoleId()
    {
        return this.roleId;
    }
    public boolean getStatus()
    {
        return this.status;
    }
    // setter
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setPassWord(String passWord)
    {
        this.passWord = passWord;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public void setRoleId(int roleId)
    {
        this.roleId = roleId;
    }
    public void setStatus(boolean status)
    {
        this.status = status;
    }
}
