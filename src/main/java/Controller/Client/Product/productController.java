package Controller.Client.Product;

import Controller.Client.Cart.cartController;
import DAO.Dao_Category;
import DAO.Dao_Food;
import Helper.HandleCartBuy;
import Model.Category;
import Model.FoodItem;
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
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class productController implements Initializable {
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
        nameLabel.setOnMouseClicked(e -> loadUI("/View/Client/Product/detailProduct.fxml", foodItem));

        // Giá
        Label priceLabel = new Label();
        priceLabel.setText(String.format("%,.0f VND", foodItem.getPrice()));
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
    // Phương thức hiển thị thông báo bằng cửa sổ riêng (stage) - phòng hờ trường hợp toast trong StackPane không hoạt động
    private void showToastStage(String message) {
        System.out.println("Hiển thị toast bằng stage mới: " + message);
        
        // Tạo Stage mới cho toast
        Stage toastStage = new Stage();
        toastStage.initStyle(StageStyle.UNDECORATED);
        toastStage.setAlwaysOnTop(true);
        
        // Lấy stage chính từ scene của root
        if (root.getScene() != null && root.getScene().getWindow() != null) {
            Stage mainStage = (Stage) root.getScene().getWindow();
            toastStage.initOwner(mainStage);
            toastStage.initModality(Modality.NONE);
            
            // Label chứa nội dung thông báo
            Label toastLabel = new Label(message);
            toastLabel.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-padding: 15px; " +
                    "-fx-background-radius: 5px; -fx-font-weight: bold; -fx-font-size: 14px;");
            toastLabel.setMinWidth(200);
            toastLabel.setMinHeight(50);
            toastLabel.setMaxWidth(300);
            toastLabel.setWrapText(true);
            toastLabel.setAlignment(Pos.CENTER);
            
            StackPane toastPane = new StackPane(toastLabel);
            toastPane.setStyle("-fx-background-color: transparent;");
            toastPane.setPadding(new Insets(20));
            
            Scene toastScene = new Scene(toastPane);
            toastScene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            toastStage.setScene(toastScene);
            
            // Định vị toast ở góc dưới bên phải của cửa sổ chính
            toastStage.setX(mainStage.getX() + mainStage.getWidth() - 320);
            toastStage.setY(mainStage.getY() + mainStage.getHeight() - 120);
            
            // Hiển thị toast
            toastStage.show();
            
            // Thiết lập timeout để đóng toast sau 2 giây
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                // Hiệu ứng mờ dần trước khi đóng
                FadeTransition fade = new FadeTransition(Duration.millis(500), toastPane);
                fade.setFromValue(1);
                fade.setToValue(0);
                fade.setOnFinished(e -> toastStage.close());
                fade.play();
            });
            delay.play();
        }
    }
    
    private void handleAddToCart(FoodItem item) {
        System.out.println("Thêm vào giỏ: " + item.getFoodName());
        // Dao check xem thử đối với cart_id này đã tồn tại foodItemId này trong đó cart đó chưa
        HandleCartBuy.getInstance().handleAddToCart(item, null);
        
        // Debug kiểm tra StackPane root
        if (root == null) {
            System.err.println("ERROR: StackPane root là null khi gọi handleAddToCart!");
        } else {
            System.out.println("StackPane root OK - Có thể hiển thị toast");
        }
        
        // Hiển thị thông báo đã thêm vào giỏ hàng bằng stage (phương pháp đáng tin cậy hơn)
        showToastStage("Đã thêm " + item.getFoodName() + " vào giỏ hàng!");
    }

    private void handleBuyNow(FoodItem item) {
        System.out.println("Mua ngay: " + item.getFoodName());
        HandleCartBuy.getInstance().handleAddToCart(item, null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/Cart/cart.fxml"));
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

    // Phương thức hiển thị thông báo tạm thời
    private void showToastNotification(String message) {
        System.out.println("Hiển thị toast: " + message);
        
        // Tạo label chứa thông báo
        Label toast = new Label(message);
        toast.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-padding: 10px 15px; " +
                "-fx-background-radius: 5px; -fx-font-weight: bold; -fx-opacity: 0.9; -fx-font-size: 14px;");
        toast.setMaxWidth(300);
        toast.setWrapText(true);
        toast.setAlignment(Pos.CENTER);
        
        // Đặt vị trí ở góc dưới bên phải
        StackPane.setAlignment(toast, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(toast, new Insets(0, 20, 20, 0));
        
        // Đảm bảo toast hiển thị ở trên cùng (z-index cao nhất)
        toast.toFront();
        toast.setViewOrder(-1000);
        
        // Ban đầu ẩn toast
        toast.setOpacity(0);
        
        // Thêm toast vào root (StackPane)
        if (root != null) {
            System.out.println("Thêm toast vào root StackPane");
            root.getChildren().add(toast);
            root.toFront(); // Đảm bảo root ở phía trước
            
            // Hiệu ứng hiện dần
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setOnFinished(e -> System.out.println("Toast đã hiện hoàn toàn"));
            fadeIn.play();
            
            // Dừng một khoảng thời gian trước khi biến mất
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                System.out.println("Bắt đầu hiệu ứng mất dần của toast");
                // Hiệu ứng mờ dần và xóa toast
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), toast);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(e -> {
                    root.getChildren().remove(toast);
                    System.out.println("Đã xóa toast khỏi giao diện");
                });
                fadeOut.play();
            });
            delay.play();
        } else {
            System.err.println("ERROR: root StackPane là null, không thể hiển thị toast!");
        }
    }
}