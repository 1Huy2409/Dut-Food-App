package Controller.Admin.Category;

import DAO.Dao_Category;
import Helper.AlertMessage;
import Model.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class addCategoryController {

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnOK;

    @FXML
    private TextField txtDesc;

    @FXML
    private TextField txtName;

    public void btnOkOnAction(ActionEvent e){
        List<Category> liCate = new ArrayList<Category>();
        liCate = Dao_Category.getInstance().getAll();
        if (txtName.getText().trim().isEmpty()) {
            AlertMessage.showAlertErrorMessage("Please enter category name");
        }
        for(Category i : liCate){
            if(txtName.getText().equalsIgnoreCase((i.getCategoryName()))){
                AlertMessage.showAlertErrorMessage(("Category already exists"));
                return;
            }
        }
        Stage currentStage = (Stage) btnOK.getScene().getWindow();
        Category newCate = new Category();
        newCate.setCategoryName(txtName.getText());
        newCate.setDescription(txtDesc.getText());
        Dao_Category.getInstance().create(newCate);
        AlertMessage.showAlertSuccessMessage("Category has been added");
        currentStage.close();
    }
    public void btnCancel(ActionEvent e){
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();
    }
}
