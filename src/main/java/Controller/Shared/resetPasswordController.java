package Controller.Shared;

import DAO.Dao_ForgotPassword;
import DAO.Dao_User;
import Helper.AlertMessage;
import Helper.EmailSession;
import Helper.PasswordHelper;
import Model.User;
import com.sun.tools.jconsole.JConsoleContext;
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
        System.out.println(newPasswordField.getText());
        System.out.println(confirmNewPasswordField.getText());
        if (confirmNewPasswordField.getText() == "" || newPasswordField.getText() == "")
        {
            AlertMessage.showAlertErrorMessage("Please enter full field!");
        }
        else if (!confirmNewPasswordField.getText().equals(newPasswordField.getText()))
        {
            AlertMessage.showAlertErrorMessage("Invalid confirm password!");
        }
        else
        {

            String userEmail = EmailSession.getInstance().getEmail();
            User currentUser = Dao_User.getInstance().checkEmail(userEmail);
            //update user with new hashed password
            String newPassword = newPasswordField.getText();
            String hashedPassword = PasswordHelper.hashPassword(newPassword);
            currentUser.setPassWord(hashedPassword);
            Dao_User.getInstance().update(currentUser);
            AlertMessage.showAlertSuccessMessage("Please login to your account again");
            Stage currentStage = (Stage) ResetPasswordBtn.getScene().getWindow();
            currentStage.close();
        }
    }
}
