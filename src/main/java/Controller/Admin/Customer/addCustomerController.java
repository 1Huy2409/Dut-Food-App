package Controller.Admin.Customer;

import DAO.Dao_User;
import Helper.AlertMessage;
import Helper.PasswordHelper;
import Helper.Validation;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addCustomerController {
    @FXML
    private TextField txtemail;

    @FXML
    private TextField txtfullname;

    @FXML
    private PasswordField txtpassword;

    @FXML
    private TextField txtphone;

    @FXML
    private TextField txtusername;
    @FXML
    private Label lbpass;
    @FXML
    private PasswordField txtpasswordconfirm;
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnOK;
    @FXML
    public void initialize(){
    }
    public void OkOnAction(){
        Stage currentStage = (Stage) btnOK.getScene().getWindow();
        lbpass.setText("");
        String fullName = txtfullname.getText().trim();
        String email = txtemail.getText().trim();
        String phone = txtphone.getText().trim();
        String userName = txtusername.getText().trim();
        String password = txtpassword.getText().trim();
        String confirmPassword = txtpasswordconfirm.getText().trim();

        if (    fullName.isEmpty() ||
                email.isEmpty() ||
                phone.isEmpty() ||
                userName.isEmpty() ||
                password.trim().isEmpty() ||
                confirmPassword.trim().isEmpty()) {

            AlertMessage.showAlertErrorMessage("Please fill in complete information");
            return;
        }

        // Kiểm tra email hợp lệ
        if (!Validation.isValidEmail(email)) {
            AlertMessage.showAlertErrorMessage("Invalid email");
            return;
        }

        // Kiểm tra số điện thoại hợp lệ
        if (!Validation.isValidPhone(phone)) {
            AlertMessage.showAlertErrorMessage("Invalid phone number");
            return;
        }

        // Kiểm tra mật khẩu mạnh
        if (!Validation.isValidPassword(password)) {
            lbpass.setText("Mật khẩu tối thiểu 6 ký tự bao gồm chữ, số và ký tự đặc biệt.");
            return;
        }

        // Kiểm tra xác nhận mật khẩu
        if (!password.equals(confirmPassword)) {
            lbpass.setText("Password confirmation does not match");
            return;
        }
        Dao_User.getInstance().checkRegister(fullName, email, userName, password, phone);
        currentStage.close();
    }
    public void cancelOnAction(){
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();
    }
}
