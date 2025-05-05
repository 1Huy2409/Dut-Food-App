package Controller.Client.Product;

import Controller.Client.Account.cartController;
import DAO.Dao_CartItem;
import DAO.Dao_Category;
import DAO.Dao_Food;
import Helper.HandleCartBuy;
import Helper.UserSession;
import Model.CartItem;
import Model.Category;
import Model.FoodItem;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;


import java.io.IOException;
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
    private Button btnAll;

    @FXML
    private TilePane productBox;
    @FXML
    private VBox VBoxProduct;
    @FXML
    private VBox productItemBox;
    @FXML
    private ScrollPane productCover;

    private VBox contentArea;
    private Button lastSelectedButton;
    public void initialize(URL location, ResourceBundle resources)
    {
        btnAll.getStyleClass().add("selected-category");
        lastSelectedButton = btnAll;
        renderBtnCategory();
        renderProduct();
    }
    public void setContentArea(VBox contentArea)
    {
        this.contentArea = contentArea;
    }
    private void loadUI(String fxml, FoodItem selectedItem)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // getController
            detailProductController controller = loader.getController();
            controller.setFoodItem(selectedItem);
            controller.setContentArea(contentArea);
            VBox.setVgrow(root, Priority.ALWAYS);
            this.contentArea.getChildren().clear();
            this.contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void renderBtnCategory()
    {
        List<Category> categoryItems = Dao_Category.getInstance().selectByCondition("categoriesBtn");
        for (Category item : categoryItems)
        {
            Button btn = new Button(item.getCategoryName());
            btn.getStyleClass().add("categories-button");
            btn.setOnAction(event -> {
                lastSelectedButton.getStyleClass().add("unselected-category");
                lastSelectedButton = btn;
                lastSelectedButton.getStyleClass().remove("unselected-category");
                lastSelectedButton.getStyleClass().add("selected-category");
                renderProductByCategory(item);
            });
            categoryButtonBox.getChildren().add(btn);
        }
    }
    public void renderProduct() {
//        VBoxProduct.getChildren().removeIf(node -> node != categoryButtonBox);
        VBoxProduct.getChildren().clear();
        VBoxProduct.getChildren().add(categoryButtonBox);

        List<Category> categoryItems = Dao_Category.getInstance().selectByCondition("categoriesBtn");

        for (Category item : categoryItems) {
            Label nameCategory = new Label(item.getCategoryName());
            nameCategory.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14pt;");
            VBoxProduct.getChildren().add(nameCategory);

            // Tạo productBox 1 lần duy nhất cho mỗi Category
            TilePane productBox = new TilePane();
            productBox.setHgap(15);
            productBox.setVgap(15);
            productBox.setPrefColumns(3); // hoặc 4 tùy layout
            productBox.setTileAlignment(Pos.TOP_LEFT); // tránh render lẻ


            List<FoodItem> foodItems = Dao_Food.getInstance().selectByCategory(item.getId());

            for (FoodItem foodItemOfCategory : foodItems)
            {
                VBox productItemBox = new VBox();
                productItemBox.setSpacing(5); // khoảng cách giữa các phần tử
                productItemBox.setAlignment(Pos.CENTER); // căn giữa các phần tử con
//                productItemBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 10; -fx-background-radius: 10;");
                productItemBox.getStyleClass().add("product-item-box");

                ImageView imageview = new ImageView();
                imageview.setFitWidth(120);
                imageview.setFitHeight(90);

                String imageUrl = foodItemOfCategory.getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    URL imageUrlPath = getClass().getResource("/" + imageUrl);
                    if (imageUrlPath != null) {
                        Image image = new Image(imageUrlPath.toExternalForm(),true);
                        imageview.setImage(image);
                    }
                }

                Rectangle clip = new Rectangle(120, 90);
                clip.setArcWidth(20);
                clip.setArcHeight(20);
                imageview.setClip(clip);

                Label nameLabel = new Label(foodItemOfCategory.getFoodName());
                nameLabel.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");

                Label priceLabel = new Label(foodItemOfCategory.getPrice().toString());

                Button addToCart = new Button("THÊM VÀO GIỎ");
//                addToCart.setStyle("-fx-background-color: #E93940; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                addToCart.getStyleClass().add("btnAddCart");
                addToCart.setUserData(foodItemOfCategory);
                addToCart.setOnAction(event -> {
                    FoodItem selectedItem = (FoodItem) ((Button) event.getSource()).getUserData();
                    handleAddToCart(selectedItem);
                });

                Button buyNow = new Button("MUA NGAY");
//                buyNow.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #e93940; -fx-font-weight: bold; -fx-cursor: hand;");
                buyNow.getStyleClass().add("btnBuyNow");
                buyNow.setUserData(foodItemOfCategory);
                buyNow.setOnAction(event -> {
                    FoodItem selectedItem = (FoodItem) ((Button) event.getSource()).getUserData();
                    handleBuyNow(selectedItem);
                });

                productItemBox.getChildren().addAll(imageview, nameLabel, priceLabel, addToCart, buyNow);

                // bắt sự kiện click ở label của productItemBox
                nameLabel.setOnMouseClicked(event ->
                {
                    loadUI("/View/Client/detailProduct.fxml", foodItemOfCategory);
                });

                // Bọc trong TilePane hoặc VBox nếu cần layout grid
                productBox.getChildren().add(productItemBox);
            }


            VBoxProduct.getChildren().add(productBox); // Thêm tất cả item vào 1 productBox rồi mới add vào VBoxProduct
        }
    }
    public void renderProductByCategory(Category category) {
        VBoxProduct.getChildren().removeIf(node -> node != categoryButtonBox);

        Label nameCategory = new Label(category.getCategoryName());
        nameCategory.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14pt;");
        VBoxProduct.getChildren().add(nameCategory);

        TilePane productBox = new TilePane();
        productBox.setHgap(15);
        productBox.setVgap(15);
        productBox.setPrefColumns(3); // hoặc 4 tùy layout
        productBox.setTileAlignment(Pos.TOP_LEFT); // tránh render lẻ


        List<FoodItem> foodItems = Dao_Food.getInstance().selectByCategory(category.getId());

        for (FoodItem foodItemOfCategory : foodItems) {
            VBox productItemBox = new VBox();
            productItemBox.setSpacing(5);
            productItemBox.setAlignment(Pos.CENTER);
//            productItemBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 10; -fx-background-radius: 10;");
            productItemBox.getStyleClass().add("product-item-box");

            ImageView imageview = new ImageView();
            imageview.setFitWidth(120);
            imageview.setFitHeight(90);

            String imageUrl = foodItemOfCategory.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                URL imageUrlPath = getClass().getResource("/" + imageUrl);
                if (imageUrlPath != null) {
                    Image image = new Image(imageUrlPath.toExternalForm(), true);
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
            addToCart.getStyleClass().add("btnAddCart");
            addToCart.setUserData(foodItemOfCategory);
            addToCart.setOnAction(event -> {
                FoodItem selectedItem = (FoodItem) ((Button) event.getSource()).getUserData();
                handleAddToCart(selectedItem);
            });

            Button buyNow = new Button("MUA NGAY");
            buyNow.getStyleClass().add("btnBuyNow");
            buyNow.setUserData(foodItemOfCategory);
            buyNow.setOnAction(event -> {
                FoodItem selectedItem = (FoodItem) ((Button) event.getSource()).getUserData();
                handleBuyNow(selectedItem);
            });

            productItemBox.getChildren().addAll(imageview, nameLabel, priceLabel, addToCart, buyNow);
            nameLabel.setOnMouseClicked(event ->
            {
                loadUI("/View/Client/detailProduct.fxml", foodItemOfCategory);
            });
            productBox.getChildren().add(productItemBox);
        }

        VBoxProduct.getChildren().add(productBox);
    }

    public void btnAllOnAction(MouseEvent e)
    {
        lastSelectedButton.getStyleClass().add("unselected-category");
        btnAll.getStyleClass().remove("unselected-category");
        btnAll.getStyleClass().add("selected-category");
        lastSelectedButton = btnAll;
        renderProduct();
    }
    private void handleAddToCart(FoodItem item) {
        System.out.println("Thêm vào giỏ: " + item.getFoodName());
        // Dao check xem thử đối với cart_id này đã tồn tại foodItemId này trong đó cart đó chưa
        HandleCartBuy.getInstance().handleAddToCart(item, null);
    }

    private void handleBuyNow(FoodItem item) {
        System.out.println("Mua ngay: " + item.getFoodName());
        HandleCartBuy.getInstance().handleAddToCart(item, null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/cart.fxml"));
            Parent root = loader.load();
            cartController controller = loader.getController();
            controller.setContentArea(contentArea); // Nếu bạn cần truyền lại contentArea
            controller.setCheckedItem(item); // Gọi hàm này để render + tích
            VBox.setVgrow(root, Priority.ALWAYS);
            this.contentArea.getChildren().clear();
            this.contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}