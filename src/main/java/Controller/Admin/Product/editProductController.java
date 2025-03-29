package Controller.Admin.Product;

import Controller.Admin.Category.editCategoryController;
import DAO.Dao_Category;
import DAO.Dao_Food;
import Model.Category;
import Model.FoodItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class editProductController {
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
    @FXML
    private TextField stock;
    private ObservableList<Category> categoryList;
//    public static editCategoryController getInstance()
//    {
//        return new editCategoryController();
//    }
    @FXML
    public void initialize(){
        loadCategories();
        setDefaultCategory(productController.foodItemSelected.getCategoryId());
        EditProduct(productController.foodItemSelected);
    }
    public void EditProduct(FoodItem item){
        txtName.setText(item.getFoodName());
        txtPrice.setText(Double.toString(item.getPrice()));
        txtDes.setText(item.getDescription());
        stock.setText(Integer.toString(item.getStock()));
        File file = new File("src/main/resources/" + item.getImageUrl());
        Image image = new Image(file.toURI().toString());
        img.setImage(image);
//        cbCategory.
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
    private void setDefaultCategory(int categoryId) {
        for (Category category : categoryList) {
            if (category.getId() == categoryId) {
                cbCategory.setValue(category);
                System.out.println("ID da chon la: " +categoryId );
                break;
            }
        }
    }
    public void ImportImg(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image newImage = new Image(selectedFile.toURI().toString());
            img.setImage(newImage);

            productController.foodItemSelected.setImageUrl(selectedFile.getAbsolutePath());
            System.out.println("Ảnh mới: " + selectedFile.getAbsolutePath());
        }
    }
    public void btnok(){
        Stage currentStage = (Stage) btnOK.getScene().getWindow();
        FoodItem item = new FoodItem();
        item.setId(productController.foodItemSelected.getId());
        item.setFoodName(txtName.getText());
        item.setPrice(Double.parseDouble(txtPrice.getText()));
        item.setDescription(txtDes.getText());
        item.setStock(Integer.parseInt(stock.getText()));
        item.setCategoryId(cbCategory.getValue().getId());
        item.setImageUrl(productController.foodItemSelected.getImageUrl().substring(43,productController.foodItemSelected.getImageUrl().length()).replace("\\","/"));
        Dao_Food.getInstance().update(item);
        currentStage.close();
    }
    public void btncancel(){
        Stage currentStage = (Stage) btnOK.getScene().getWindow();
        currentStage.close();
    }
}
