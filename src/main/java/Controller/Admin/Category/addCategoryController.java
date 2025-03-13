package Controller.Admin.Category;

import DAO.Dao_Category;
import Model.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addCategoryController {
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnCancel;

    @FXML
    private TextArea cateDesc;

    @FXML
    private TextField cateName;
    public void BtnAddOnAction(ActionEvent event)
    {
        Stage currentStage = (Stage) btnAdd.getScene().getWindow();
        Category newCategory = new Category();
        newCategory.setCategoryName(cateName.getText());
        newCategory.setDescription(cateDesc.getText());
        Dao_Category.getInstance().create(newCategory);
        currentStage.close();
    }
    public void BtnCancelOnAction(ActionEvent event)
    {
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();
    }
}
