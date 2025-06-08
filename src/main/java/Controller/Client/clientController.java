
package Controller.Client;

import Controller.Client.Cart.cartController;
import Controller.Client.Product.productController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class clientController implements Initializable {

    @FXML
    private BorderPane overallLayout;

    @FXML
    private VBox contentArea;
    private static clientController instance;
    public clientController() {
        instance = this; // gán khi JavaFX tạo controller từ FXML
    }
    public void initialize(URL location, ResourceBundle resources)
    {
        loadUI("/View/Client/Product/product.fxml");
    }
    public static clientController  getInstance() {return instance; }
    public void loadUI(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            switch (fxml)
            {
                case "/View/Client/Product/product.fxml":
                    productController controller = loader.getController();
                    controller.setContentArea(contentArea);
                    break;
                case "/View/Client/Cart/cart.fxml":
                    cartController cartCtrl = loader.getController();
                    cartCtrl.setContentArea(contentArea);
                    break;

//                case "/View/Client/product.fxml":
//                    categoryController controller = loader.getController();
//                    controller.setContentArea(contentArea);
            }
//            detailProductController controller = loader.getController();
//            controller.setContentArea(contentArea);

            // 🔑 Cho phép root giãn chiều cao trong VBox
            VBox.setVgrow(root, Priority.ALWAYS);
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
            if (root instanceof Region) {
                Region region = (Region) root;
                region.prefWidthProperty().bind(contentArea.widthProperty());
                region.prefHeightProperty().bind(contentArea.heightProperty());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void DUTFOODOnAction(MouseEvent e)
    {
        loadUI("/View/Client/Product/product.fxml");
//        btnCategory.getStyleClass().add("selected-button-container");
    }

    public void ProfileOnAction(MouseEvent e)
    {
        loadUI("/View/Client/Profile/profile.fxml");
//        btnCategory.getStyleClass().add("selected-button-container");
    }
    public void CartOnAction(MouseEvent e)
    {
        loadUI("/View/Client/Cart/cart.fxml");
//        btnCategory.getStyleClass().add("selected-button-container");
    }
}
