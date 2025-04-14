package Controller.Client.Account;

import DAO.Dao_Cart;
import DAO.Dao_User;
import Model.Cart;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//import java.awt.*;
import javafx.event.ActionEvent;
import java.io.IOException;

//import helper function

public class registerController {
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private Button registerButton;
    @FXML
    private Button switchLoginButton;

    //method
    public void registerButtonOnAction(ActionEvent e)
    {
        String fullName = fullNameTextField.getText();
        String email = emailTextField.getText();
        String userName = userNameTextField.getText();
        String password = passwordPasswordField.getText();
        String phone = phoneTextField.getText();
        // check userName and email has been existed in databases yet, if yes => "try again", else => "create new user"
        User newUser = Dao_User.getInstance().checkRegister(fullName, email, userName, password, phone);
        Cart newCart = new Cart(newUser.getId());
        Dao_Cart.getInstance().create(newCart);
    }
    public void switchLoginOnAction(ActionEvent e)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Shared/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = (Stage) switchLoginButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
