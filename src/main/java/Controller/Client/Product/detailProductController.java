package Controller.Client.Product;

import Controller.Client.Cart.cartController;
import Model.FoodItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Helper.HandleCartBuy;

public class detailProductController implements Initializable {
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnBackDetail;

    @FXML
    private Button btnSub;
    @FXML
    private Button btnAddCart;
    @FXML
    private Button btnBuyNow;

    @FXML
    private TextArea foodDesc;

    @FXML
    private ImageView foodImage;

    @FXML
    private VBox foodInfo;

    private VBox contentArea;

    @FXML
    private TextField textNum;

    private FoodItem item;

    @FXML
    private Label lbName;
    @FXML
    private Label lbPrice;
    @FXML
    private StackPane imageContainer;
    @FXML
    private Rectangle imageBackground;
    @FXML
    private Rectangle clipRectangle;


    public void initialize(URL location, ResourceBundle resources) {
    }

    private void loadUI(String fxml)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            VBox.setVgrow(root, Priority.ALWAYS);
            this.contentArea.getChildren().clear();
            this.contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setContentArea(VBox contentArea) {
        this.contentArea = contentArea;
    }

    public void setFoodItem(FoodItem item) {
        String imageUrl = item.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Lấy đường dẫn từ resources
            URL imageUrlPath = getClass().getResource("/" + imageUrl);
            if (imageUrlPath != null) {
                javafx.scene.image.Image image = new Image(imageUrlPath.toExternalForm());
                foodImage.setImage(image);
            } else {
                System.err.println("This image file does not exist in resources: " + imageUrl);
            }
        } else {
            System.err.println("Error file path for: " + item.getFoodName());
        }
        lbName.setText(item.getFoodName());
        lbPrice.setText(String.format("%,.0f VND", item.getPrice()));
        textNum.setText("0");
        btnAdd.setOnAction(event ->
                {
                    // xử lý với stock còn lại
                    int currentNum = Integer.parseInt(textNum.getText());
                    if (currentNum < item.getStock()) {
                        ++currentNum;
                        textNum.setText(Integer.toString(currentNum));
                    }
                }
        );
        btnSub.setOnAction(event ->
                {
                    // xử lý với stock còn lại
                    int currentNum = Integer.parseInt(textNum.getText());
                    if (currentNum > 0) {
                        --currentNum;
                        textNum.setText(Integer.toString(currentNum));
                    }
                }
        );
        btnBuyNow.setUserData(item);
        btnBuyNow.setOnAction(event -> {
            FoodItem selectedItem = (FoodItem) ((Button) event.getSource()).getUserData();
            handleBuyNow(selectedItem);
        });
        btnAddCart.setUserData(item);
        btnAddCart.setOnAction(event -> {
            FoodItem selectedItem = (FoodItem) ((Button) event.getSource()).getUserData();
            handleAddCart(selectedItem);
        });
        foodDesc.setText(item.getDescription());
    }

    @FXML
    void backDetailOnAction(ActionEvent event) {
        try {
            // Load lại giao diện danh sách sản phẩm
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/Product/product.fxml"));
            Parent root = loader.load();

            productController controller = loader.getController();
            controller.setContentArea(this.contentArea);
            // Có thể gọi lại renderProduct nếu cần
            this.contentArea.getChildren().clear();
            this.contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleAddCart(FoodItem item) {
        System.out.println("Thêm vào giỏ: " + item.getFoodName());
        int quantity = Integer.parseInt(textNum.getText());
        
        // Kiểm tra số lượng
        if (quantity <= 0) {
            showToastStage("Vui lòng chọn số lượng sản phẩm!");
            return;
        }
        
        HandleCartBuy.getInstance().handleAddToCart(item, quantity);
        showToastStage("Đã thêm " + quantity + " " + item.getFoodName() + " vào giỏ hàng!");
    }

    // Phương thức hiển thị thông báo bằng cửa sổ riêng (stage)
    private void showToastStage(String message) {
        System.out.println("Hiển thị toast bằng stage mới: " + message);
        
        // Tạo Stage mới cho toast
        Stage toastStage = new Stage();
        toastStage.initStyle(StageStyle.UNDECORATED);
        toastStage.setAlwaysOnTop(true);
        
        // Lấy stage chính từ scene của btnAddCart
        if (btnAddCart.getScene() != null && btnAddCart.getScene().getWindow() != null) {
            Stage mainStage = (Stage) btnAddCart.getScene().getWindow();
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

    private void handleBuyNow(FoodItem item) {

        System.out.println("Mua ngay: " + item.getFoodName());
        // loadUI ra cart
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
}
