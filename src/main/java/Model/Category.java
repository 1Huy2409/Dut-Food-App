package Model;

<<<<<<< HEAD
import java.sql.Timestamp;

=======
>>>>>>> phihihi
public class Category {
    private int id;
    private String categoryName;
    private String description;
<<<<<<< HEAD
    private Timestamp createdAt;
    private boolean status;

    public Category()
    {
        this.status = true;
    }

    public Category(int id, String categoryName, String description, Timestamp createdAt, boolean status) {
=======
    private String createdAt;

    public Category(int id, String categoryName, String description, String createdAt) {
>>>>>>> phihihi
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.createdAt = createdAt;
<<<<<<< HEAD
        this.status = status;
=======
>>>>>>> phihihi
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

<<<<<<< HEAD
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
=======
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
>>>>>>> phihihi
}
