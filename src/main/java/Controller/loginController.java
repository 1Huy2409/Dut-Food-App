package Controller;
import Database.JDBC;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.*;

//import helper function
import Helper.AlertMessage;
import Helper.PasswordHelper;
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

        // Checking empty input
        if (userNameCheck.isEmpty() || passwordCheck.isEmpty()) {
            AlertMessage.showAlertErrorMessage("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        try (Connection con = JDBC.getConnection()) {
            String query = "SELECT password FROM users WHERE userName = ?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, userNameCheck);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("password");

                        // Check password with BCrypt
                        if (PasswordHelper.checkPassword(passwordCheck, hashedPassword)) {
                            loginMessageLabel.setText("Welcome " + userNameCheck);
                        } else {
                            AlertMessage.showAlertErrorMessage("Wrong password. Please try again!");
                        }
                    } else {
                        AlertMessage.showAlertErrorMessage("This account dont exist. Please try again!");
                    }
                }
            }
        } catch (SQLException ex) {
            AlertMessage.showAlertErrorMessage("Database Connection Error: " + ex.getMessage());
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
