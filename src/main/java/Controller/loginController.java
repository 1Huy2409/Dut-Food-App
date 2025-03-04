package Controller;
import DAO.Dao_Food;
import DAO.Dao_User;
import Database.JDBC;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.*;

//import helper function
import Helper.*;
import Helper.RouteScreen;
import DAO.Dao_User.*;
public class loginController
{
    @FXML
    private Button switchRegisterButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordPasswordField;

    // method alert error
    public void loginButtonOnAction(ActionEvent e) {
        String userNameCheck = userNameTextField.getText();
        String passwordCheck = passwordPasswordField.getText();
        User currentUser = Dao_User.getInstance().checkLogin(userNameCheck, passwordCheck);
        if (currentUser != null)
        {
            Session.getInstance().setUser(currentUser.getPhone(), currentUser.getFullName(), currentUser.getEmail(), currentUser.getUserName(), currentUser.getStatus(), currentUser.getRoleId());
            if (Session.getInstance().getRoleId() == 1)
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
}
