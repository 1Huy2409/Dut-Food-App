package Controller.Admin.Category;

import DAO.Dao_Category;
import DAO.Dao_Food;
import Helper.RouteScreen;
import Model.Category;
import Model.FoodItem;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import Helper.AlertMessage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.shape.Rectangle;

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
    private TableColumn<Category, String> statusColumn;
    @FXML
    private TableColumn<Category, Void> actionColumn;
    @FXML
    private HBox functional;
    private ObservableList<Category> catagoryList;
    private ObservableList<BooleanProperty> checkboxStates;
    protected static Category categorySelected = null;
    @FXML
    public void initialize() {
        functional.getStylesheets().add(getClass().getResource("/CSS/table-style.css").toExternalForm());
        categoryTable.getStylesheets().add(getClass().getResource("/CSS/table-style.css").toExternalForm());
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
        categoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // để cột co giãn theo tổng width

        selectColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.15));
        idColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.15));
        categoryNameColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.2));
        created_timeColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.2));
        actionColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.3));
        statusColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.1));
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
        statusColumn.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().getStatus();
            return new SimpleStringProperty(status ? "Active" : "Inactive");
        });
        statusColumn.setCellFactory(column -> new TableCell<Category, String>() {
            @Override
            protected void updateItem(String statusText, boolean empty) {
                super.updateItem(statusText, empty);

                if (empty || statusText == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(statusText);
                    if (statusText.equals("Active")) {
                        setTextFill(Color.GREEN);
                    } else {
                        setTextFill(Color.GRAY);
                    }
                }
            }
        });
        actionColumn.setCellFactory(param -> new TableCell<Category, Void>() {
            private final ImageView editIcon = new ImageView(new Image(getClass().getResource("/Pictures/edit.png").toExternalForm()));
            private final ImageView deleteIcon = new ImageView(new Image(getClass().getResource("/Pictures/delete.png").toExternalForm()));
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(10, editButton, deleteButton);

            {
                editIcon.setFitWidth(20);
                editIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                // Gán icon vào button
                editButton.setGraphic(editIcon);
                deleteButton.setGraphic(deleteIcon);
                editButton.getStyleClass().add("edit-button");
                editButton.setOnAction(event -> {
                    categorySelected = getTableView().getItems().get(getIndex());
                    if (categorySelected != null) {
                        Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Category/editCategory.fxml");
                        stage.setOnHidden(e -> reload());
                    }
                });
                deleteButton.getStyleClass().add("delete-button");
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
                    buttons.setStyle("-fx-alignment: CENTER;");
                    setGraphic(buttons);
                    setStyle("-fx-alignment: CENTER;");
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
        reloadBtn.setOnAction(event -> {
            reload();
        });
        multipleBtn.setOnAction(actionEvent -> {
            List<Category> selectedItems = new ArrayList<Category>();
            for (int i = 0; i < catagoryList.size(); i++) {
                if (checkboxStates.get(i).get()) {
                    selectedItems.add(catagoryList.get(i));
                }
            }
            boolean confirm = AlertMessage.showConfirm("Are you sure you want to delete ?");
            if(confirm){
                for (Category items : selectedItems) {
                    Dao_Category.getInstance().delete(items);
                }
                for (BooleanProperty state : checkboxStates) {
                    state.set(false);
                }
                AlertMessage.showAlertSuccessMessage("Deleted successfully");
                reload();
            }

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
//    public void handleMultipleDelete(ActionEvent event)
//    {
//        for (int i = 0; i < catagoryList.size(); i++)
//        {
//            if (checkboxStates.get(i).get())
//            {
//                Category category = catagoryList.get(i);
//                Dao_Category.getInstance().delete(category);
//            }
//        }
//        reload();
//    }
}
