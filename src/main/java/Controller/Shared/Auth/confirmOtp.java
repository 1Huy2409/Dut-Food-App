package Controller.Shared.Auth;

import DAO.Dao_User;
import Helper.AlertMessage;
import Helper.EmailSession;
import Helper.RouteScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class confirmOtp {
    @FXML
    private Button btnBack;

    @FXML
    private Button confirmOtpBtn;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField otpTextField;
    @FXML
    public void initialize() {
        getEmailTextField();
    }
    private void getEmailTextField()
    {
        emailTextField.setText(EmailSession.getInstance().getEmail());
    }
    @FXML
    public void handleConfirmOtp(ActionEvent e)
    {
        if (otpTextField.getText().trim().isEmpty())
        {
            AlertMessage.showAlertErrorMessage("Please enter your OTP code!");
        }
        else
        {
            if (Dao_User.getInstance().checkOtp(emailTextField.getText().trim(), otpTextField.getText().trim()))
            {
                Stage currentStage = (Stage) confirmOtpBtn.getScene().getWindow();
                currentStage.close();
                RouteScreen.getInstance().newScreen("/View/Shared/resetPassword.fxml");
            }
            else
            {
                AlertMessage.showAlertErrorMessage("Invalid OTP code! Try again");
            }
        }
    }
    @FXML
    public void handleBack(ActionEvent e)
    {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        currentStage.close();
    }
}
