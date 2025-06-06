package Controller.Shared.Auth;
import DAO.Dao_Cart;
import DAO.Dao_User;
import Model.Cart;
import Model.User;
import Helper.Validation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

//import helper function
import Helper.*;
import Helper.RouteScreen;

public class loginController
{
    @FXML
    private Button switchRegisterButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Hyperlink forgotPwLink;

    public void loginButtonOnAction(ActionEvent e) {
        String emailCheck = emailTextField.getText().trim();
        String passwordCheck = passwordPasswordField.getText().trim();
        // validate data
        if (!Validation.getInstance().loginValidation(emailCheck, passwordCheck))
        {
            return;
        }
        User currentUser = Dao_User.getInstance().checkLogin(emailCheck, passwordCheck);
        if (currentUser != null)
        {
            // user khác null thì gán cart_id vào user session
            Cart currentCart = Dao_Cart.getInstance().selectedByUserId(currentUser.getId());
            UserSession.getInstance().setUser(currentUser.getId(), currentUser.getPhone(), currentUser.getFullName(), currentUser.getEmail(), currentUser.getUserName(), currentUser.getStatus(), currentUser.getRoleId(), currentCart.getId());
            if (UserSession.getInstance().getRoleId() == 1)
            {
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                RouteScreen.switchRouter(currentStage, "/View/Admin/admin_dashboard.fxml", null, null);
            }
            else
            {
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                RouteScreen.switchRouter(currentStage, "/View/Client/Dashboard/client_dashboard.fxml",null, null);
            }
        }
    }
    public void switchRegisterOnAction(ActionEvent e)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Shared/register.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 694, 437);
            Stage stage = (Stage) switchRegisterButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void handleForgotPassword(ActionEvent e)
    {
        RouteScreen.getInstance().newScreen("/View/Shared/forgotPassword.fxml");
    }
}
