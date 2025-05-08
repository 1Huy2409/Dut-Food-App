package Controller.Admin.Product;

import DAO.Dao_Category;
import DAO.Dao_Food;
import Helper.AlertMessage;
import Helper.Validation;
import Model.Category;
import Model.FoodItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.util.List;

public class addProductController {
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnIm;

    @FXML
    private Button btnOK;

    @FXML
    private ComboBox<Category> cbCategory;

    @FXML
    private ImageView img;

    @FXML
    private TextField txtDes;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;
    private ObservableList<Category> categoryList;
    private static FoodItem item = new FoodItem();
    @FXML
    public void initialize(){
        loadCategories();
    }
    private void loadCategories() {
        List<Category> categories = Dao_Category.getInstance().getAll();
        categoryList = FXCollections.observableArrayList(categories);
        cbCategory.setItems(categoryList);

        // Hiển thị tên Category thay vì hiển thị Object
        cbCategory.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getCategoryName() : "";
            }

            @Override
            public Category fromString(String string) {
                return categoryList.stream()
                        .filter(cat -> cat.getCategoryName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }
    public void importImg(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image newImage = new Image(selectedFile.toURI().toString());
            img.setImage(newImage);
            img.setPreserveRatio(false);

            item.setImageUrl(selectedFile.getAbsolutePath().substring(selectedFile.getAbsolutePath().indexOf("Picture"),selectedFile.getAbsolutePath().toString().length()).replace("\\","/"));
            System.out.println("Ảnh mới: " + selectedFile.getAbsolutePath());
        }
    }
    public void btnok(){
        Stage currentStage = (Stage) btnOK.getScene().getWindow();

        if(!txtName.getText().isEmpty() && Validation.isValidPrice(txtPrice.getText()) && !txtDes.getText().isEmpty() && img.getImage() != null && cbCategory.getValue() != null){
            item.setFoodName(txtName.getText());
            item.setPrice(Double.parseDouble(txtPrice.getText()));
            item.setDescription(txtDes.getText());
            item.setCategoryId(cbCategory.getValue().getId());
            Dao_Food.getInstance().create(item);
            currentStage.close();
        }
        else {
            AlertMessage.showAlertErrorMessage("Please fill in complete information");
        }
        System.out.println();
    }
    public void btncancel(){
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();
    }
}
