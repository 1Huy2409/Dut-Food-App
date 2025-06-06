package Controller.Admin.Category;

import DAO.Dao_Category;
import DAO.Dao_Food;
import Helper.AlertMessage;
import Helper.RouteScreen;
import Helper.Validation;
import Model.Category;
import Model.FoodItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class editCategoryController{
    @FXML
    private TextArea cateDesc;

    @FXML
    private RadioButton cateFalse;

    @FXML
    private TextField cateId;

    @FXML
    private TextField cateName;

    @FXML
    private RadioButton cateTrue;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnOk;
    public static editCategoryController getInstance()
    {
        return new editCategoryController();
    }
    @FXML
    public void initialize()
    {
        getUpdate(categoryController.getSelected());
    }
    public void getUpdate(Category categorySelected)
    {

        cateId.setText(Integer.toString(categorySelected.getId()));
        cateName.setText(categorySelected.getCategoryName());
        cateDesc.setText(categorySelected.getDescription());
        if (categorySelected.getStatus())
        {
            cateTrue.setSelected(true);
        }
        else
        {
            cateFalse.setSelected(true);
        }
        System.out.println(categorySelected.getId());
    }
    public void BtnOkOnAction(ActionEvent e)
    {
        Stage currentStage = (Stage) btnOk.getScene().getWindow();
        Category item = new Category();
        item.setId(Integer.parseInt(cateId.getText()));
        item.setCategoryName(cateName.getText());
        item.setDescription(cateDesc.getText());

        if(Validation.isCategoryExists(cateName.getText(), Integer.parseInt(cateId.getText()))){
            AlertMessage.showAlertErrorMessage("Category already exists");
            return;
        }
        else if(cateName.getText().isEmpty()){
            AlertMessage.showAlertErrorMessage("Please enter category name");
            return;
        }
        if (cateTrue.isSelected())
        {
            item.setStatus(true);
            List<FoodItem> listFoodItem = Dao_Food.getInstance().selectByCategory(Integer.parseInt(cateId.getText()));
            for (FoodItem foodItem : listFoodItem)
            {
                Dao_Food.getInstance().updateStatus(foodItem, true);
            }
        }
        else
        {
            List<FoodItem> listFoodItem = Dao_Food.getInstance().selectByCategory(Integer.parseInt(cateId.getText()));
            for (FoodItem foodItem : listFoodItem)
            {
                Dao_Food.getInstance().updateStatus(foodItem, false);
            }
            item.setStatus(false);
        }
        Dao_Category.getInstance().update(item);
        currentStage.close();
    }
    public void BtnCancelOnAction(ActionEvent e)
    {
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        currentStage.close();
    }
}
