package Model;
import javafx.beans.property.*;
import java.sql.Timestamp;
public class Category {
    private SimpleIntegerProperty id;
    private SimpleStringProperty categoryName;
    private SimpleStringProperty description;
    private ObjectProperty<Timestamp> createdAt;
    private boolean status;

    public Category()
    {
        this.status = true;
    }

    public Category(int id, String categoryName, String description, Timestamp createdAt, boolean status) {
        this.id = new SimpleIntegerProperty(id);
        this.categoryName = new SimpleStringProperty(categoryName);
        this.description = new SimpleStringProperty(description);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.status = status;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.set(categoryName);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Timestamp getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt.set(createdAt);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public IntegerProperty idProperty() {
        return id;
    }
    public StringProperty categoryNameProperty() {return categoryName;}
    public StringProperty descriptionProperty() {return description;}
    public ObjectProperty<Timestamp> createdAtProperty() {return createdAt;}
}
