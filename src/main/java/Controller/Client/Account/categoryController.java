package Controller.Client.Account;


import DAO.Dao_Category;
import DAO.Dao_Food;
import Model.Category;
import Model.FoodItem;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class categoryController implements Initializable {
//    @FXML
//    private Button categoryBtn;

    @FXML
    private TilePane categoryButtonBox;
    @FXML
    private Label btnMN;

    @FXML
    private Button btnTVG;

    @FXML
    private TilePane productBox;
    @FXML
    private VBox VBoxProduct;
    @FXML
    private VBox productItemBox;

    public void initialize(URL location, ResourceBundle resources)
    {
        renderBtnCategory();
        renderProduct();
    }
    public void renderBtnCategory()
    {
        List<Category> categoryItems = Dao_Category.getInstance().selectByCondition("categoriesBtn");
        for (Category item : categoryItems)
        {
            Button btn = new Button(item.getCategoryName());
            categoryButtonBox.getChildren().add(btn);
        }
    }
    public void renderProduct() {
        VBoxProduct.getChildren().removeIf(node -> node != categoryButtonBox);

        List<Category> categoryItems = Dao_Category.getInstance().selectByCondition("categoriesBtn");

        for (Category item : categoryItems) {
            Label nameCategory = new Label(item.getCategoryName());
            VBoxProduct.getChildren().add(nameCategory);

            // Tạo productBox 1 lần duy nhất cho mỗi Category
            TilePane productBox = new TilePane();
            productBox.setHgap(15);
            productBox.setVgap(15);

            List<FoodItem> foodItems = Dao_Food.getInstance().selectByCategory(item.getId());

            for (FoodItem foodItemOfCategory : foodItems)
            {
                VBox productItemBox = new VBox();
                productItemBox.setSpacing(5); // khoảng cách giữa các phần tử
                productItemBox.setAlignment(Pos.CENTER); // căn giữa các phần tử con
                productItemBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 10; -fx-background-radius: 10;");

                ImageView imageview = new ImageView();
                imageview.setFitWidth(120);
                imageview.setFitHeight(90);

                String imageUrl = foodItemOfCategory.getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    URL imageUrlPath = getClass().getResource("/" + imageUrl);
                    if (imageUrlPath != null) {
                        Image image = new Image(imageUrlPath.toExternalForm());
                        imageview.setImage(image);
                    }
                }

                Rectangle clip = new Rectangle(120, 90);
                clip.setArcWidth(20);
                clip.setArcHeight(20);
                imageview.setClip(clip);

                Label nameLabel = new Label(foodItemOfCategory.getFoodName());
                nameLabel.setStyle("-fx-font-weight: bold;");

                Label priceLabel = new Label(foodItemOfCategory.getPrice().toString());

                Button addToCart = new Button("THÊM VÀO GIỎ");
                addToCart.setStyle("-fx-background-color: #ffb6c1; -fx-text-fill: white; -fx-font-weight: bold;");

                Button buyNow = new Button("MUA NGAY");
                buyNow.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #ffb6c1; -fx-font-weight: bold;");

                productItemBox.getChildren().addAll(imageview, nameLabel, priceLabel, addToCart, buyNow);

                // Bọc trong TilePane hoặc VBox nếu cần layout grid
                productBox.getChildren().add(productItemBox);
            }


            VBoxProduct.getChildren().add(productBox); // Thêm tất cả item vào 1 productBox rồi mới add vào VBoxProduct
        }
    }


}
