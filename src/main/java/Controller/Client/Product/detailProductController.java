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
        // render food item detail

//        renderDetail(this.item);
//        foodImage.fitWidthProperty().bind(imageContainer.widthProperty());
//
//        // Clip theo bo góc
//        clipRectangle.widthProperty().bind(foodImage.fitWidthProperty());
//        clipRectangle.heightProperty().bind(foodImage.fitHeightProperty());
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
        lbPrice.setText(Double.toString(item.getPrice()));
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
        HandleCartBuy.getInstance().handleAddToCart(item, Integer.parseInt(textNum.getText()));
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
