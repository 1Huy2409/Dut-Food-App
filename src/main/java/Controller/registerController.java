package Controller;

import DAO.Dao_User;
import Database.JDBC;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

//import helper function
import Helper.AlertMessage;
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
        // check userName and email has been existed in databases yet, if yes => "try again", else => "create new user"
        try {
            Connection connection = JDBC.getConnection();
            Statement statement = connection.createStatement();
            String verifyRegister = "select count(id) from users where userName = '"+ userName +"' or email = '"+ email +"' or status = false";
            ResultSet rs = statement.executeQuery(verifyRegister);
            while (rs.next())
            {
                if (rs.getInt(1) > 0)
                {
                    String errorText = "This username or email have been used by another user!";
                    AlertMessage.showAlertErrorMessage(errorText);
                    return;
                }
                else
                {
                    // create new account
                    User newUser = new User();
                    newUser.setFullName(fullName);
                    newUser.setEmail(email);
                    newUser.setUserName(userName);
                    newUser.setPassWord(password);
                    newUser.setStatus(true);
                    Dao_User.getInstance().create(newUser);
                    String successMessage = "New account is created!!! Please click Login Button to Login into App";
                    AlertMessage.showAlertSuccessMessage(successMessage);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
