
package Controller.Client;

import Controller.Client.Account.cartController;
import Controller.Client.Product.categoryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class client_dashboardController implements Initializable {

    @FXML
    private BorderPane overallLayout;

    @FXML
    private VBox contentArea;

    public void initialize(URL location, ResourceBundle resources)
    {
        loadUI("/View/Client/category.fxml");
    }

    private void loadUI(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            switch (fxml)
            {
                case "/View/Client/category.fxml":
                    categoryController controller = loader.getController();
                    controller.setContentArea(contentArea);
                    break;
                case "/View/Client/cart.fxml":
                    cartController cartCtrl = loader.getController();
                    cartCtrl.setContentArea(contentArea);
                    break;
//                case "/View/Client/category.fxml":
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
        loadUI("/View/Client/category.fxml");
//        btnCategory.getStyleClass().add("selected-button-container");
    }

    public void ProfileOnAction(MouseEvent e)
    {
        loadUI("/View/Client/profile.fxml");
//        btnCategory.getStyleClass().add("selected-button-container");
    }
    public void CartOnAction(MouseEvent e)
    {
        loadUI("/View/Client/cart.fxml");
//        btnCategory.getStyleClass().add("selected-button-container");
    }
}
