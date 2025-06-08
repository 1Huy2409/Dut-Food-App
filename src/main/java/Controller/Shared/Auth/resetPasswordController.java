package Controller.Shared.Auth;

import DAO.Dao_User;
import Helper.AlertMessage;
import Helper.EmailSession;
import Helper.PasswordHelper;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class resetPasswordController {
    @FXML
    private Button ResetPasswordBtn;

    @FXML
    private PasswordField confirmNewPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    public void handleResetPassword(ActionEvent event) {
        if (confirmNewPasswordField.getText().trim().isEmpty() || newPasswordField.getText().trim().isEmpty())
        {
            AlertMessage.showAlertErrorMessage("Please enter full field!");
        }
        else if (!confirmNewPasswordField.getText().equals(newPasswordField.getText().trim()))
        {
            AlertMessage.showAlertErrorMessage("Invalid confirm password!");
        }
        else
        {

            String userEmail = EmailSession.getInstance().getEmail();
            User currentUser = Dao_User.getInstance().checkEmail(userEmail);
            String newPassword = newPasswordField.getText();
            String hashedPassword = PasswordHelper.hashPassword(newPassword);
            currentUser.setPassWord(hashedPassword);
            Dao_User.getInstance().updatePassword(currentUser);
            AlertMessage.showAlertSuccessMessage("Please login to your account again");
            Stage currentStage = (Stage) ResetPasswordBtn.getScene().getWindow();
            currentStage.close();
        }
    }
}
