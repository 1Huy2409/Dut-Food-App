package Controller.Shared.Auth;

import DAO.Dao_User;
import Helper.AlertMessage;
import Helper.EmailSession;
import Helper.RouteScreen;
import Helper.SendingEmail;
import jakarta.mail.MessagingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Timestamp;

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
        currentStage.close();
    }

    @FXML
    public void handleSendOtp(ActionEvent event) throws MessagingException {
        String email = emailTextField.getText().trim();
        if (email.isEmpty())
        {
            AlertMessage.showAlertErrorMessage("Please enter your email account here!");
        }
        else
        {
            if (Dao_User.getInstance().checkEmail(email) != null)
            {
                String OTP = SendingEmail.randomOtp(6);
                Timestamp OTPExpiredAt = new Timestamp(System.currentTimeMillis() + 5 * 60 * 1000);
                Dao_User.getInstance().updateForgotPassword(email, OTP, OTPExpiredAt);
                SendingEmail.sendMail(email, OTP);
                EmailSession.getInstance().setEmail(email);
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
