package Controller.Admin.Category;

import DAO.Dao_Category;
import Helper.RouteScreen;
import Model.Category;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import Helper.AlertMessage;
import javafx.stage.Stage;

public class categoryController {
    @FXML private Button selectAll;

    @FXML private Button add;
    @FXML private Button reloadBtn;
    @FXML
    private Button multipleBtn;

    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, Number> idColumn;
    @FXML
    private TableColumn<Category, String> categoryNameColumn;
    @FXML
    private TableColumn<Category, String> created_timeColumn;
    @FXML
    private TableColumn<Category, Boolean> selectColumn;
    @FXML
    private TableColumn<Category, Void> actionColumn;

    private ObservableList<Category> catagoryList;
    private ObservableList<BooleanProperty> checkboxStates;
    protected static Category categorySelected = null;
    @FXML
    public void initialize() {
        reload();
    }
    public static categoryController getInstance()
    {
        return new categoryController();
    }
    public static Category getSelected()
    {
        return categorySelected;
    }
    public void reload()
    {
        List<Category> listCategory = Dao_Category.getInstance().getAll();
        for (Category item: listCategory)
        {
            System.out.println(item.getCategoryName());
        }
        catagoryList = FXCollections.observableArrayList(listCategory);
        checkboxStates = FXCollections.observableArrayList();
        for (int i = 0; i < catagoryList.size(); i++) {
            checkboxStates.add(new SimpleBooleanProperty(false));
        }
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        categoryNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoryName()));
        created_timeColumn.setCellValueFactory(cellData -> {
            Timestamp timestamp = cellData.getValue().getCreatedAt();
            String formattedDate = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return new SimpleStringProperty(formattedDate);
        });

        actionColumn.setCellFactory(param -> new TableCell<Category, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    categorySelected = getTableView().getItems().get(getIndex());
                    if (categorySelected != null) {
                        Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Category/editCategory.fxml");
                        stage.setOnHidden(e -> reload());
                    }
                });

                deleteButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    Dao_Category.getInstance().delete(category);
                    reload();
                    AlertMessage.showAlertSuccessMessage("You have deleted this category!");
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                    HBox buttons = new HBox(10, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        selectColumn.setCellValueFactory(cellData -> {
            int index = catagoryList.indexOf(cellData.getValue());
            // Trả về BooleanProperty đại diện cho trạng thái checkbox
            return checkboxStates.get(index);
        });

        selectColumn.setCellFactory(tc -> {
            CheckBoxTableCell<Category, Boolean> checkBoxCell = new CheckBoxTableCell<>();
            checkBoxCell.setEditable(true); // Cho phép chỉnh sửa trực tiếp trên bảng
            return checkBoxCell;
        });

        categoryTable.setEditable(true); // Bật chế độ chỉnh sửa bảng

        selectAll.setOnAction(event -> {
            boolean allSelected = checkboxStates.stream().allMatch(BooleanProperty::get);
            checkboxStates.forEach(state -> state.set(!allSelected)); // Đảo trạng thái
            categoryTable.refresh();
        });

        categoryTable.setItems(catagoryList);
        add.setOnAction(event -> {
            Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Category/addCategory.fxml");
            stage.setOnHidden(e -> reload());
        });
    }

    @FXML private void handleSelectAll() {
        checkboxStates.forEach(state -> state.set(true)); // Chọn tất cả
        categoryTable.refresh();
    }
    public void ReloadOnAction(ActionEvent e)
    {
        reload();
    }

    public void BtnAddOnAction(ActionEvent event)
    {
        Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Category/addCategory.fxml");
        stage.setOnHidden(e -> reload());
    }
    public void handleMultipleDelete(ActionEvent event)
    {
        for (int i = 0; i < catagoryList.size(); i++)
        {
            if (checkboxStates.get(i).get())
            {
                Category category = catagoryList.get(i);
                Dao_Category.getInstance().delete(category);
            }
        }
        reload();
    }
}
