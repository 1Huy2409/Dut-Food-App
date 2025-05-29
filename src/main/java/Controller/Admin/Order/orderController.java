package Controller.Admin.Order;

import DAO.Dao_Orders;
import DAO.Dao_User;
import Helper.RouteScreen;
import Helper.AlertMessage;
import Model.Order;
import Model.User;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class orderController {
    @FXML private Button selectAll;
    @FXML private Button add;
    @FXML private Button reloadBtn;
    @FXML private Button multipleBtn;

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Boolean> selectColumn;
    @FXML private TableColumn<Order, Number> idColumn;
    @FXML private TableColumn<Order, String> userNameColumn;
    @FXML private TableColumn<Order, Number> totalPriceColumn;
    @FXML private TableColumn<Order, String> statusColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, Void> actionColumn;
    @FXML private HBox functional;

    private ObservableList<Order> orderList;
    private ObservableList<BooleanProperty> checkboxStates;
    private Map<Integer, User> userMap = new HashMap<>();
    protected static Order orderSelected = null;

    @FXML
    public void initialize() {
        functional.getStylesheets().add(getClass().getResource("/CSS/table-style.css").toExternalForm());
        orderTable.getStylesheets().add(getClass().getResource("/CSS/table-style.css").toExternalForm());
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        selectColumn.prefWidthProperty().bind(orderTable.widthProperty().multiply(0.1));
        idColumn.prefWidthProperty().bind(orderTable.widthProperty().multiply(0.1));
        userNameColumn.prefWidthProperty().bind(orderTable.widthProperty().multiply(0.2));
        totalPriceColumn.prefWidthProperty().bind(orderTable.widthProperty().multiply(0.15));
        statusColumn.prefWidthProperty().bind(orderTable.widthProperty().multiply(0.15));
        orderDateColumn.prefWidthProperty().bind(orderTable.widthProperty().multiply(0.15));
        actionColumn.prefWidthProperty().bind(orderTable.widthProperty().multiply(0.15));

        reload();
    }

    public static orderController getInstance() {
        return new orderController();
    }

    public static Order getSelected() {
        return orderSelected;
    }

    public void reload() {
        List<Order> listOrder = Dao_Orders.getInstance().getAll();
        List<User> listUser = Dao_User.getInstance().getAll();

        // Create a map of users for quick lookup by ID
        userMap.clear();
        for (User user : listUser) {
            userMap.put(user.getId(), user);
        }

        orderList = FXCollections.observableArrayList(listOrder);
        checkboxStates = FXCollections.observableArrayList();
        for (int i = 0; i < orderList.size(); i++) {
            checkboxStates.add(new SimpleBooleanProperty(false));
        }

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));

        userNameColumn.setCellValueFactory(cellData -> {
            int userId = cellData.getValue().getUserId();
            User user = userMap.get(userId);
            String userName = user != null ? user.getFullName() : "Unknown User";
            return new SimpleStringProperty(userName);
        });

        totalPriceColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getTotalPrice()));

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus()));

        statusColumn.setCellFactory(column -> new TableCell<Order, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if (status.equalsIgnoreCase("Completed")) {
                        setTextFill(Color.GREEN);
                    } else if (status.equalsIgnoreCase("Pending")) {
                        setTextFill(Color.ORANGE);
                    } else if (status.equalsIgnoreCase("Canceled")) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });

        orderDateColumn.setCellValueFactory(cellData -> {
            Timestamp timestamp = cellData.getValue().getOrderDate();
            String formattedDate = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            return new SimpleStringProperty(formattedDate);
        });

        actionColumn.setCellFactory(param -> new TableCell<Order, Void>() {
            private final ImageView editIcon = new ImageView(new Image(getClass().getResource("/Pictures/edit.png").toExternalForm()));
            private final ImageView deleteIcon = new ImageView(new Image(getClass().getResource("/Pictures/delete.png").toExternalForm()));
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

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
                    orderSelected = getTableView().getItems().get(getIndex());
                    if (orderSelected != null) {
                        Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Order/editOrder.fxml");
                        stage.setOnHidden(e -> reload());
                    }
                });
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    boolean confirm = AlertMessage.showConfirm("Are you sure you want to delete this order?");
                    if (confirm) {
                        Dao_Orders.getInstance().delete(order);
                        AlertMessage.showAlertSuccessMessage("You have deleted this order!");
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
                    buttons.setStyle("-fx-alignment: CENTER;");
                    setGraphic(buttons);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });

        selectColumn.setCellValueFactory(cellData -> {
            int index = orderList.indexOf(cellData.getValue());
            return checkboxStates.get(index);
        });

        selectColumn.setCellFactory(tc -> {
            CheckBoxTableCell<Order, Boolean> checkBoxCell = new CheckBoxTableCell<>();
            checkBoxCell.setEditable(true);
            return checkBoxCell;
        });

        orderTable.setEditable(true);

        selectAll.setOnAction(event -> {
            boolean allSelected = checkboxStates.stream().allMatch(BooleanProperty::get);
            checkboxStates.forEach(state -> state.set(!allSelected));
            orderTable.refresh();
        });

        orderTable.setItems(orderList);

        add.setOnAction(event -> {
            Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Order/addOrder.fxml");
            stage.setOnHidden(e -> reload());
        });

        reloadBtn.setOnAction(event -> reload());

        multipleBtn.setOnAction(actionEvent -> {
            List<Order> selectedItems = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++) {
                if (checkboxStates.get(i).get()) {
                    selectedItems.add(orderList.get(i));
                }
            }

            boolean confirm = AlertMessage.showConfirm("Are you sure you want to delete these orders?");
            if (confirm) {
                for (Order order : selectedItems) {
                    Dao_Orders.getInstance().delete(order);
                }
                for (BooleanProperty state : checkboxStates) {
                    state.set(false);
                }
                AlertMessage.showAlertSuccessMessage("Orders deleted successfully");
                reload();
            }
        });
    }

    @FXML
    private void handleSelectAll() {
        checkboxStates.forEach(state -> state.set(true));
        orderTable.refresh();
    }

    public void ReloadOnAction(ActionEvent e) {
        reload();
    }

    public void BtnAddOnAction(ActionEvent event) {
        Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Order/addOrder.fxml");
        stage.setOnHidden(e -> reload());
    }
}