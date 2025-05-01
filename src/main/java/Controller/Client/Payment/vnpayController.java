package Controller.Client.Payment;

import Config.VNPayPayment;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.net.URI;
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
        try{
        String url = VNPayPayment.createPaymentUrl(50000,Integer.toString(5));
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public void setContentArea(VBox contentArea)
    {
        this.paymentMethodArea = contentArea;
    }
    public void setOrderId(String id)
    {
        this.orderId = id;
        System.out.println("Id cua don hang du kien la: " + this.orderId);
    }
    public void setAmount(int amount)
    {
        this.amount = amount;
        System.out.println(this.amount );
    }
}
