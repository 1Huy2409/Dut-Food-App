package Controller.Admin.Order;

import DAO.Dao_Orders;
import Model.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class editOrderController {
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> cbStatus;

    private Order currentOrder;
    @FXML
    public void initialize() {
        // Initialization logic for the edit order controller
        // This could include loading data, setting up UI components, etc.
        currentOrder = orderController.orderSelected;
        LoadStatus();
        LoadOrder(orderController.orderSelected);
    }
    private void LoadOrder(Order order)
    {
        // Logic to load the order details into the UI components
        // This could include setting text fields, checkboxes, etc. with the order data
        cbStatus.setValue(order.getStatus());
    }
    private void LoadStatus() {
        cbStatus.getItems().addAll("Pending", "Completed", "Cancelled");
    }
    @FXML
    private void btnCancelOnAction(ActionEvent event) {
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void btnSaveOnAction(ActionEvent event) {
        currentOrder.setStatus(cbStatus.getValue());
        Dao_Orders.getInstance().update(currentOrder);
        Stage currentStage = (Stage) btnSave.getScene().getWindow();
        currentStage.close();
    }
}
