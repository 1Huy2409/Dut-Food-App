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
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.StackPane;
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
    @FXML private StackPane root;
    @FXML private ImageView introImage;
    @FXML private Rectangle gradientOverlayTop;
    @FXML private Rectangle gradientOverlayBot;
    @FXML
    private Button btnScrollToProducts;
    @FXML
    private Label lbThucdon;

    private VBox contentArea;
    private Button lastSelectedButton;

public void initialize(URL location, ResourceBundle resources) {
    try {
        URL imageUrl = getClass().getResource("/Pictures/intro.jpg");
        System.out.println("Image URL: " + imageUrl);

        if (imageUrl != null) {
            Image image = new Image(imageUrl.toExternalForm());

            // Thêm listener để bắt lỗi load ảnh
            image.errorProperty().addListener((obs, wasError, isNowError) -> {
                if (isNowError) {
                    System.err.println("Lỗi khi tải ảnh: " + image.getException());
                }
            });

            // Thêm listener khi ảnh load thành công
            image.progressProperty().addListener((obs, oldVal, newVal) -> {
                System.out.println("Tiến trình load ảnh: " + newVal);
                if (newVal.doubleValue() == 1.0) {
                    System.out.println("Ảnh đã load xong, kích thước: " +
                            image.getWidth() + "x" + image.getHeight());
                }
            });

            introImage.setImage(image);
        } else {
            System.err.println("Không tìm thấy file ảnh");
        }

        introImage.setOpacity(1.0);
        introImage.setVisible(true);

        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                System.out.println("Scene size: " + newScene.getWidth() + "x" + newScene.getHeight());
                introImage.fitWidthProperty().bind(newScene.widthProperty());
                introImage.fitHeightProperty().bind(newScene.heightProperty());
            }
        });
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                introImage.fitWidthProperty().bind(newScene.widthProperty());
                introImage.fitHeightProperty().bind(newScene.heightProperty());

                gradientOverlayTop.widthProperty().bind(newScene.widthProperty());
                gradientOverlayTop.heightProperty().set(150); // độ cao lớp mờ
//                gradientOverlayTop.setTranslateY(0);
                gradientOverlayBot.widthProperty().bind(newScene.widthProperty());
                gradientOverlayBot.heightProperty().set(150); // độ cao lớp mờ
//                gradientOverlayBot.setTranslateY(newScene.getHeight() - 150);
//                blackOverlay.widthProperty().bind(newScene.widthProperty());
//                blackOverlay.heightProperty().bind(newScene.heightProperty());
//                blackOverlay.setTranslateY(150);
            }
        });

//        btnScrollToProducts.setOnAction(event -> {
//            // Cuộn tới phần productBox
//            double targetY = productBox.getBoundsInParent().getMinY();
//            double totalHeight = VBoxProduct.getHeight();
//
//            // Tính tỉ lệ cuộn trong ScrollPane
//            double scrollValue = targetY / totalHeight;
//
//            // Cuộn đến vị trí đó
//            productCover.setVvalue(scrollValue);
//        });
        btnScrollToProducts.setOnAction(event -> smoothScrollToProductSection());

        btnAll.getStyleClass().add("selected-category");
        lastSelectedButton = btnAll;
        renderBtnCategory();
        renderProduct();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private void smoothScrollToProductSection() {
        // Tính toán độ cao cần cuộn đến (ví dụ: bỏ qua vùng giới thiệu ~200px đầu tiên)
        double targetY = lbThucdon.getBoundsInParent().getMinY();

        // Thực hiện scroll mượt bằng AnimationTimer hoặc Timeline
        final double start = productCover.getVvalue();
        final double height = VBoxProduct.getHeight() - productCover.getViewportBounds().getHeight();
        final double end = targetY / height;

        javafx.animation.Timeline timeline = new javafx.animation.Timeline();
        javafx.animation.KeyValue kv = new javafx.animation.KeyValue(productCover.vvalueProperty(), end);
        javafx.animation.KeyFrame kf = new javafx.animation.KeyFrame(javafx.util.Duration.millis(800), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
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
//    public void renderProduct() {
//        productBox.getChildren().clear();
//        TilePane productBox = new TilePane();
//        productBox.setHgap(15);
//        productBox.setVgap(15);
//        productBox.setPrefColumns(3); // hoặc 4 tùy layout
//        productBox.setTileAlignment(Pos.CENTER); // tránh render lẻ
//        productBox.setPadding(new Insets(0,50,0,50));
//        List<FoodItem> foodItems = Dao_Food.getInstance().getAll();
//        for (FoodItem foodItemOfCategory : foodItems)
//        {
//            VBox productItemBox = new VBox();
//            productItemBox.setSpacing(5); // khoảng cách giữa các phần tử
//            productItemBox.setAlignment(Pos.CENTER); // căn giữa các phần tử con
//            productItemBox.setPrefWidth(200); // Cố định chiều rộng
//            productItemBox.setPrefHeight(250);
////                productItemBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 10; -fx-background-radius: 10;");
//            productItemBox.getStyleClass().add("product-item-box");
//
//            ImageView imageview = new ImageView();
//            imageview.setFitWidth(150);
//            imageview.setFitHeight(120);
//
//            String imageUrl = foodItemOfCategory.getImageUrl();
//            if (imageUrl != null && !imageUrl.isEmpty()) {
//                URL imageUrlPath = getClass().getResource("/" + imageUrl);
//                if (imageUrlPath != null) {
//                    Image image = new Image(imageUrlPath.toExternalForm(),true);
//                    imageview.setImage(image);
//                }
//            }
//
//            Rectangle clip = new Rectangle(150, 120);
//            clip.setArcWidth(20);
//            clip.setArcHeight(20);
//            imageview.setClip(clip);
//
//            Label nameLabel = new Label(foodItemOfCategory.getFoodName());
//            nameLabel.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
//
//            Label priceLabel = new Label(foodItemOfCategory.getPrice().toString());
//
//            Button addToCart = new Button("THÊM VÀO GIỎ");
////                addToCart.setStyle("-fx-background-color: #E93940; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
//            addToCart.getStyleClass().add("btnAddCart");
//            addToCart.setUserData(foodItemOfCategory);
//            addToCart.setOnAction(event -> {
//                FoodItem selectedItem = (FoodItem) ((Button) event.getSource()).getUserData();
//                handleAddToCart(selectedItem);
//            });
//
//            Button buyNow = new Button("MUA NGAY");
////                buyNow.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #e93940; -fx-font-weight: bold; -fx-cursor: hand;");
//            buyNow.getStyleClass().add("btnBuyNow");
//            buyNow.setUserData(foodItemOfCategory);
//            buyNow.setOnAction(event -> {
//                FoodItem selectedItem = (FoodItem) ((Button) event.getSource()).getUserData();
//                handleBuyNow(selectedItem);
//            });
//
//            productItemBox.getChildren().addAll(imageview, nameLabel, priceLabel, addToCart, buyNow);
//
//            // bắt sự kiện click ở label của productItemBox
//            nameLabel.setOnMouseClicked(event ->
//            {
//                loadUI("/View/Client/detailProduct.fxml", foodItemOfCategory);
//            });
//
//            // Bọc trong TilePane hoặc VBox nếu cần layout grid
//            productBox.getChildren().add(productItemBox);
//        }
//        VBoxProduct.getChildren().add(productBox);
//    }
    public void clearProductNodes() {
        // Giữ lại các node cố định (intro, button, label...)
        int fixedNodesCount = VBoxProduct.getChildren().indexOf(categoryButtonBox) + 1; // +1 để giữ cả categoryButtonBox
        if (VBoxProduct.getChildren().size() > fixedNodesCount) {
            VBoxProduct.getChildren().remove(fixedNodesCount, VBoxProduct.getChildren().size());
        }
    }
    public void renderProduct() {
        // Xóa toàn bộ nội dung cũ (trừ categoryButtonBox)
        clearProductNodes();

        // Tạo TilePane mới
        TilePane productTilePane = new TilePane();
        productTilePane.setHgap(15);
        productTilePane.setVgap(15);
        productTilePane.setPrefColumns(3);
        productTilePane.setAlignment(Pos.CENTER);
        productTilePane.setTileAlignment(Pos.CENTER);
        productTilePane.setPadding(new Insets(0, 50, 0, 50));
        TilePane.setMargin(productTilePane, new Insets(20,0,0,0));
        // Render sản phẩm
        List<FoodItem> foodItems = Dao_Food.getInstance().getAll();
        for (FoodItem foodItem : foodItems) {
            VBox productItemBox = createProductItemBox(foodItem);
            productTilePane.getChildren().add(productItemBox);
        }

        // Thêm vào VBoxProduct
        VBoxProduct.getChildren().add(productTilePane);
    }

    // Hàm tạo VBox cho từng sản phẩm (tách riêng để code gọn hơn)
    private VBox createProductItemBox(FoodItem foodItem) {
        VBox productItemBox = new VBox();
        productItemBox.setSpacing(5);
        productItemBox.setAlignment(Pos.CENTER);
        productItemBox.setPrefWidth(200);
        productItemBox.setPrefHeight(250);
        productItemBox.getStyleClass().add("product-item-box");

        // ImageView
        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(120);

        if (foodItem.getImageUrl() != null && !foodItem.getImageUrl().isEmpty()) {
            try {
                URL imageUrlPath = getClass().getResource("/" + foodItem.getImageUrl());
                if (imageUrlPath != null) {
                    Image image = new Image(imageUrlPath.toExternalForm(), true);
                    imageView.setImage(image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Bo góc ảnh
        Rectangle clip = new Rectangle(150, 120);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);

        // Tên sản phẩm
        Label nameLabel = new Label(foodItem.getFoodName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
        nameLabel.setOnMouseClicked(e -> loadUI("/View/Client/detailProduct.fxml", foodItem));

        // Giá
        Label priceLabel = new Label(foodItem.getPrice().toString());

        // Nút thêm vào giỏ
        Button addToCart = new Button("THÊM VÀO GIỎ");
        addToCart.getStyleClass().add("btnAddCart");
        addToCart.setUserData(foodItem);
        addToCart.setOnAction(e -> handleAddToCart((FoodItem) addToCart.getUserData()));

        // Nút mua ngay
        Button buyNow = new Button("MUA NGAY");
        buyNow.getStyleClass().add("btnBuyNow");
        buyNow.setUserData(foodItem);
        buyNow.setOnAction(e -> handleBuyNow((FoodItem) buyNow.getUserData()));

        // Thêm các thành phần vào VBox
        productItemBox.getChildren().addAll(imageView, nameLabel, priceLabel, addToCart, buyNow);
        return productItemBox;
    }
    public void renderProductByCategory(Category category) {
        // Xóa toàn bộ nội dung cũ (trừ categoryButtonBox)
        clearProductNodes();

        // Thêm Label tên danh mục
        Label nameCategory = new Label(category.getCategoryName());
        nameCategory.setStyle("-fx-text-fill: white; -fx-font-family: 'Dancing Script'; -fx-font-weight: bold; -fx-font-size: 32px");
        VBoxProduct.getChildren().add(nameCategory);

        // Tạo TilePane mới
        TilePane productBox = new TilePane();
        productBox.setHgap(15);
        productBox.setVgap(15);
        productBox.setPrefColumns(3);
        productBox.setTileAlignment(Pos.CENTER);
        productBox.setPadding(new Insets(0, 50, 0, 50));
        TilePane.setMargin(productBox, new Insets(20,0,0,0));
        // Render sản phẩm theo danh mục
        List<FoodItem> foodItems = Dao_Food.getInstance().selectByCategory(category.getId());
        for (FoodItem foodItem : foodItems) {
            VBox productItemBox = createProductItemBox(foodItem);
            productBox.getChildren().add(productItemBox);
        }

        // Thêm vào VBoxProduct
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