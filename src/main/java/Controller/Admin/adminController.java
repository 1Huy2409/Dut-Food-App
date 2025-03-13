package Controller.Admin;

import DAO.Dao_Role;
import Helper.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

//import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class adminController implements Initializable{
    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private HBox bestSellerBox;
    @FXML
    private PieChart pieChart;
    @FXML
    private Label userName;
    @FXML
    private Label roleName;
    @FXML
    private HBox btnCategory;
    @FXML
    private HBox btnProduct;
    @FXML
    private HBox btnCustomer;
    @FXML
    private HBox btnDashboard;
    @FXML
    private HBox btnStaff;
    @FXML
    private HBox btnOrder;
    @FXML
    private HBox btnSetting;
    @FXML
    private HBox btnReport;
    @FXML
    private VBox contentArea;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        getUserName();
        loadUI("/View/Admin/Dashboard/dashboard.fxml");
    }


    private void getUserName()
    {
        userName.setText(Session.getInstance().getUserName());
        String nameRole = (Dao_Role.getInstance().selectedById(Session.getInstance().getRoleId()).getRoleName());
        roleName.setText(nameRole);
        System.out.println(Session.getInstance().getUserName());
        System.out.println(nameRole);
    }

    private void loadUI(String fxml)
    {
        try {
            contentArea.getChildren().clear();
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void CategoryOnAction(MouseEvent e)
    {
        loadUI("/View/Admin/Category/category.fxml");
//        btnCategory.getStyleClass().add("selected-button-container");
    }
    public void DashboardOnAction(MouseEvent e)
    {
        loadUI("/View/Admin/Dashboard/dashboard.fxml");
//        btnDashboard.getStyleClass().add("selected-button-container");
    }
    public void ProductOnAction(MouseEvent e)
    {
        loadUI("/View/Admin/Product/product.fxml");
//        btnProduct.getStyleClass().add("selected-button-container");
    }
}
