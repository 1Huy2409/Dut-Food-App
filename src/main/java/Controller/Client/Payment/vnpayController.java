package Controller.Client.Payment;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class vnpayController implements Initializable {
    @FXML
    private Button btnOpenPaymentBrowser;
    @FXML
    private VBox paymentMethodArea;
    private String orderId;
    private int amount;
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void OpenPaymentBrowserOnAction(MouseEvent e)
    {
        System.out.println("open payment browser");
        // open browser

    }
    public void setContentArea(VBox contentArea)
    {
        this.paymentMethodArea = contentArea;
    }
    public void setOrderId(String id)
    {
        this.orderId = id;
    }
    public void setAmount(int amount)
    {
        this.amount = amount;
    }
}
