package Controller.Shared;

import DAO.Dao_ForgotPassword;
import DAO.Dao_User;
import Helper.AlertMessage;
import Helper.EmailSession;
import Helper.RouteScreen;
import Helper.SendingEmail;
import Model.ForgotPassword;
import jakarta.mail.MessagingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class forgotPassword {
    @FXML
    private Button btnBack;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button sendOtpBtn;
    public static forgotPassword getInstance()
    {
        return new forgotPassword();
    }
    @FXML
    public void handleBackLogin(ActionEvent event) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        RouteScreen.switchRouter(currentStage, "/View/Shared/login.fxml", 600, 400);
    }

    @FXML
    public void handleSendOtp(ActionEvent event) throws MessagingException {
        String email = emailTextField.getText();
        if (email == "")
        {
            AlertMessage.showAlertErrorMessage("Please enter your email account here!");
        }
        else
        {
            if (Dao_User.getInstance().checkEmail(email) != null)
            {
                // create new record in table forgotPassword
                ForgotPassword newRecord = new ForgotPassword();
                String OTP = SendingEmail.randomOtp(6);
                newRecord.setEmail(email);
                newRecord.setOtp(OTP); // tempo OTP waiting random and email sending
                SendingEmail.sendMail(email, OTP);
                Dao_ForgotPassword.getInstance().create(newRecord);
                EmailSession.getInstance().setEmail(emailTextField.getText());
                Stage currentStage = (Stage) sendOtpBtn.getScene().getWindow();
                RouteScreen.getInstance().newScreen("/View/Shared/confirmOtp.fxml");
                currentStage.close();
            }
            else
            {
                AlertMessage.showAlertErrorMessage("This email doesn't exist in this system! Try again!");
            }
        }
    }
}
