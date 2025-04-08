package Controller.Admin.Customer;
import DAO.Dao_User;
import Helper.AlertMessage;
import Helper.RouteScreen;
import Model.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class customerController {
    @FXML
    private TableColumn<User, Void> actionColumn;

    @FXML
    private Button add;
    @FXML
    private TextField search;
    @FXML
    private TableView<User> customerTable;

    @FXML
    private TableColumn<User, String> email;

    @FXML
    private Button multipleBtn;

    @FXML
    private TableColumn<User, String> name;

    @FXML
    private TableColumn<User, String> phone;

    @FXML
    private Button reloadBtn;

    @FXML
    private Button selectAll;

    @FXML
    private TableColumn<User, Boolean> selectColumn;

    @FXML
    private TableColumn<User, Boolean> status;
    private ObservableList<User> userList;
    private ObservableList<BooleanProperty> checkboxStates;
    protected static User userSelected = null;
    private FilteredList<User> filteredUsers;
    public static customerController getInstance()
    {
        return new customerController();
    }
    @FXML
    public void initialize() {
        reload();
    }
    public void reload() {
        List<User> listUser = Dao_User.getInstance().getAll();
        userList = FXCollections.observableArrayList(listUser);
        filteredUsers = new FilteredList<>(userList, p -> true);
        checkboxStates = FXCollections.observableArrayList();
        for (int i = 0; i < userList.size(); i++) {
            checkboxStates.add(new SimpleBooleanProperty(false));
        }
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullName()));
        email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        phone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        status.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getStatus()).asObject());
        status.setCellFactory(column -> new TableCell<User, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item ? "Active" : "Inactive");
                    setTextFill(item ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.web("#828282"));
                    setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
                }
            }
        });
        customerTable.setRowFactory(tv -> new TableRow<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle(""); // Xóa style nếu hàng trống
                } else {
                    setStyle("-fx-border-color: transparent;");
                }
            }
        });
        customerTable.setStyle("-fx-table-cell-border-color: transparent;");
        actionColumn.setCellFactory(param -> new TableCell<User, Void>() {
            private final ImageView editIcon = new ImageView(new Image(getClass().getResource("/Pictures/edit.png").toExternalForm()));
            private final ImageView deleteIcon = new ImageView(new Image(getClass().getResource("/Pictures/delete.png").toExternalForm()));
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox buttons = new HBox(10, editButton, deleteButton);
            {
                editIcon.setFitWidth(20);
                editIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                // Gán icon vào button
                editButton.setGraphic(editIcon);
                deleteButton.setGraphic(deleteIcon);
                editButton.setOnAction(event -> {
                    userSelected = getTableView().getItems().get(getIndex());
                    if (userSelected != null) {
                        Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Customer/editCustomer.fxml");
                        stage.setOnHidden(e -> reload());
                    }
                });

                deleteButton.setOnAction(event -> {
                   userSelected = getTableView().getItems().get(getIndex());
                     boolean confirm = AlertMessage.showConfirm("Are you sure you want to delete this usert?");
                    if (confirm) {
                        Dao_User.getInstance().delete(customerController.userSelected);
                        AlertMessage.showAlertSuccessMessage("Deleted successfully");
                       reload();
                    }
                });
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUsers.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getFullName().toLowerCase().contains(lowerCaseFilter) ||
                        user.getEmail().toLowerCase().contains(lowerCaseFilter) ||
                        user.getPhone().toLowerCase().contains(lowerCaseFilter);
            });
        });
        selectColumn.setCellValueFactory(cellData -> {
            int index = userList.indexOf(cellData.getValue());
            // Trả về BooleanProperty đại diện cho trạng thái checkbox
            return checkboxStates.get(index);
        });

        selectColumn.setCellFactory(tc -> {
            CheckBoxTableCell<User, Boolean> checkBoxCell = new CheckBoxTableCell<>();
            checkBoxCell.setEditable(true); // Cho phép chỉnh sửa trực tiếp trên bảng
            return checkBoxCell;
        });

        customerTable.setEditable(true); // Bật chế độ chỉnh sửa bảng

        selectAll.setOnAction(event -> {
            boolean allSelected = checkboxStates.stream().allMatch(BooleanProperty::get);
            checkboxStates.forEach(state -> state.set(!allSelected)); // Đảo trạng thái
            customerTable.refresh();
        });

        //customerTable.setItems(userList);
        customerTable.setItems(filteredUsers);


//        private void handleSelectAll() {
//            checkboxStates.forEach(state -> state.set(true)); // Chọn tất cả
//            customerTable.refresh();
//        }
        add.setOnAction(event -> {
            Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Customer/addCustomer.fxml");
            stage.setOnHidden(e -> reload());
        });
        multipleBtn.setOnAction(event -> {
            List<User> selectedItems = new ArrayList<User>();
            for (int i = 0; i < userList.size(); i++) {
                if (checkboxStates.get(i).get()) {
                     selectedItems.add(userList.get(i));
                }

            }
            if(selectedItems.size()== 0){

            }
            else {
                for (User items : selectedItems) {
                    Dao_User.getInstance().delete(items);
                }
                for (BooleanProperty state : checkboxStates) {
                    state.set(false); // Hủy chọn tất cả
                }
                AlertMessage.showAlertSuccessMessage("Deleted successfully");
                reload();
            }
        });
        reloadBtn.setOnAction(event -> {
            reload();
        });
    }

}
