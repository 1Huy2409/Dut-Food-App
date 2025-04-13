package Controller.Client.Account;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class cartController implements Initializable {

    @FXML
    private TextField addressTextField;

    @FXML
    private Button checkoutButton;

    @FXML
    private TextField fullNameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Label totalQuantityLabel;

    public void initialize(URL location, ResourceBundle resources)
    {

    }

}
