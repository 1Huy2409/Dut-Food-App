package Controller;
import Database.JDBC;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import helper function
import Helper.AlertMessage;
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
    public void loginButtonOnAction(ActionEvent e)
    {
        try {
            Connection con = JDBC.getConnection();
            String userNameCheck = userNameTextField.getText();
            String passwordCheck = passwordPasswordField.getText();
            String verifyLogin = "select count(1) from users where userName = '"+ userNameCheck +"' and password = '"+ passwordCheck +"'";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(verifyLogin);
            while (rs.next())
            {
                if (rs.getInt(1) > 0)
                {
                    loginMessageLabel.setText("Welcome " + userNameTextField.getText());
                }
                else
                {
                    String errorText = "This account is not exist. Please try again";
                    AlertMessage.showAlertErrorMessage(errorText);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
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
