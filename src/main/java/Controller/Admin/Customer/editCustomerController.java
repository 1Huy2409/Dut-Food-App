package Controller.Admin.Customer;

import DAO.Dao_User;
import Helper.AlertMessage;
import Helper.Validation;
import Model.Category;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
public class editCustomerController {
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtemail;

    @FXML
    private TextField txtname;

    @FXML
    private TextField txtphone;

    @FXML
    private RadioButton activeCus;

    @FXML
    private RadioButton inactiveCus;

    private static customerController getInstance() {
        return new customerController();
    }
    @FXML
    public void initialize() {
        getEdit(customerController.userSelected);
    }
    public void getEdit(User user){
        txtname.setText(user.getFullName());
        txtemail.setText(user.getEmail());
        txtphone.setText(user.getPhone());
        if (user.getStatus())
        {
            activeCus.setSelected(true);
        }
        else
        {
            inactiveCus.setSelected(true);
        }
    }

    public void BtnSaveOnAction(ActionEvent e){
        Stage currentStage = (Stage) btnSave.getScene().getWindow();

        User newUser = new User();
        newUser.setId(customerController.userSelected.getId());
        newUser.setUserName(customerController.userSelected.getUserName());
        newUser.setPhone(txtphone.getText());
        newUser.setFullName(txtname.getText());
        newUser.setRoleId(customerController.userSelected.getRoleId());
        newUser.setPassWord(customerController.userSelected.getPassWord());
        newUser.setEmail(txtemail.getText());
        if (activeCus.isSelected()) {
            newUser.setStatus(true);
        } else {
            newUser.setStatus(false);
        }
        newUser.setImage("");
        Dao_User.getInstance().update(newUser);
        currentStage.close();

    }
    public void BtnCancelOnACtion(ActionEvent e){
        Stage currentStage = (Stage) btnSave.getScene().getWindow();
        currentStage.close();
    }
}
