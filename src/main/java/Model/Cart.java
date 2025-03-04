package Model;

<<<<<<< HEAD
import java.sql.Timestamp;

public class Cart {
    private int id;
    private int userId;
    private Timestamp createdAt;

    public Cart(int userId)
    {
        this.userId = userId;
    }

=======
public class Cart {
    private int id;
    private int userId;
    private String createdAt;
    public Cart(int id, int userId, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
    }
>>>>>>> phihihi
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

<<<<<<< HEAD
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
=======
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
>>>>>>> phihihi
        this.createdAt = createdAt;
    }
}
