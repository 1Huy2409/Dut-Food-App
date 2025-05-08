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
        boolean checkinfo = true;
        boolean checkpass = true;
        Stage currentStage = (Stage) btnOK.getScene().getWindow();
        String pass = "";
        User user = new User();
        if(txtpassword.getText().equals(txtpasswordconfirm.getText())){
            pass = txtpassword.getText();
            lbpass.setText("");
        }
        else{
            checkpass = false;
            lbpass.setText("Password confirmation does not match");
        }
        if(!txtfullname.getText().trim().isEmpty() && !txtemail.getText().trim().isEmpty() && !txtphone.getText().trim().isEmpty() && !txtusername.getText().trim().isEmpty() ){
            user.setFullName(txtfullname.getText());
            user.setEmail(txtemail.getText());
            user.setPhone(txtphone.getText());
            user.setUserName(txtusername.getText());
            user.setRoleId(2);
        }
        else if(!Validation.isValidEmail(txtemail.getText())){
            checkinfo = false;
            AlertMessage.showAlertErrorMessage("Invalid email");
        }
        else if(!Validation.isValidPhone(txtphone.getText())){
            checkinfo = false;
            AlertMessage.showAlertErrorMessage("Invalid phone number");
        }
        else {
            checkinfo = false;
            AlertMessage.showAlertErrorMessage("Please fill in complete information");
        }
        if(checkpass){
            user.setPassWord(PasswordHelper.hashPassword(pass));
        }

        if(checkinfo && checkpass){
            Dao_User.getInstance().create(user);
            currentStage.close();
        }

    }
    public void cancelOnAction(){
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();
    }
}
