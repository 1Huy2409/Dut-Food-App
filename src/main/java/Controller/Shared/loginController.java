package Controller.Shared;
import DAO.Dao_User;
import Model.User;
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
        String emailCheck = emailTextField.getText();
        String passwordCheck = passwordPasswordField.getText();
        User currentUser = Dao_User.getInstance().checkLogin(emailCheck, passwordCheck);
        if (currentUser != null)
        {
            UserSession.getInstance().setUser(currentUser.getPhone(), currentUser.getFullName(), currentUser.getEmail(), currentUser.getUserName(), currentUser.getStatus(), currentUser.getRoleId());
            if (UserSession.getInstance().getRoleId() == 1)
            {
                // go to admin_dashboard
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                RouteScreen.switchRouter(currentStage, "/View/Admin/admin_dashboard.fxml");
            }
            else
            {
                // go to client_dashboard
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                RouteScreen.switchRouter(currentStage, "/View/Client/client_dashboard.fxml");
            }
        }
    }
    public void switchRegisterOnAction(ActionEvent e)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Shared/register.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
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
