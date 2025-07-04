package Controller.Admin.Order;

import DAO.Dao_OrderInfo;
import DAO.Dao_Orders;
import DAO.Dao_User;
import Helper.InvoiceGenerator;
import Helper.RouteScreen;
import Helper.AlertMessage;
import Model.Order;
import Model.OrderInfo;
import Model.User;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML private Button btnDetail;
    @FXML private Button btnInvoice;
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
    public static Order orderSelected = null;

    @FXML
    public void initialize() {
        functional.getStylesheets().add(getClass().getResource("/CSS/table-style.css").toExternalForm());
        orderTable.getStylesheets().add(getClass().getResource("/CSS/table-style.css").toExternalForm());
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        orderTable.setSelectionModel(null);
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
        loadOrderAndUserData();
        setupTableColumns();

        orderTable.setEditable(true);
        orderTable.setItems(orderList);
        setupButtonsAction();
    }
    private void loadOrderAndUserData() {
        List<Order> listOrder = Dao_Orders.getInstance().getAll();
        List<User> listUser = Dao_User.getInstance().getAllCustomer();
        userMap.clear();
        for (User user : listUser) {
            userMap.put(user.getId(), user);
        }
        orderList = FXCollections.observableArrayList(listOrder);
        checkboxStates = FXCollections.observableArrayList();
        for (int i = 0; i < orderList.size(); i++) {
            checkboxStates.add(new SimpleBooleanProperty(false));
        }
    }
    private void setupTableColumns() {
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

            {
                editIcon.setFitWidth(20);
                editIcon.setFitHeight(20);


                // Gán icon vào button
                editButton.setGraphic(editIcon);
                editButton.getStyleClass().add("edit-button");
                editButton.setOnAction(event -> {
                    orderSelected = getTableView().getItems().get(getIndex());
                    if (orderSelected != null) {
                        Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Order/editOrder.fxml");
                        stage.setOnHidden(e -> reload());
                    }
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, editButton);
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
    }
    private void setupButtonsAction()
    {
        selectAll.setOnAction(event -> {
            boolean allSelected = checkboxStates.stream().allMatch(BooleanProperty::get);
            checkboxStates.forEach(state -> state.set(!allSelected));
            orderTable.refresh();
        });

        btnInvoice.setOnAction(event -> {
            List<Order> selectedItems = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++) {
                if (checkboxStates.get(i).get()) {
                    selectedItems.add(orderList.get(i));
                }
            }
            if (selectedItems.isEmpty()) {
                AlertMessage.showAlertErrorMessage("Please select at least one order to generate invoice.");
                return;
            }
            orderSelected = selectedItems.get(0);
            OrderInfo orderInfo = Dao_OrderInfo.getInstance().getOrderInfoByOrderId(orderSelected.getId());
            if (orderInfo == null) {
                AlertMessage.showAlertErrorMessage("Order information not found for the selected order.");
                return;
            }
            InvoiceGenerator.exportInvoiceAsPDF(orderSelected, orderInfo);
            AlertMessage.showAlertSuccessMessage("Invoice generated successfully.");
        });
        btnDetail.setOnAction(event -> {
            List<Order> selectedItems = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++) {
                if (checkboxStates.get(i).get()) {
                    selectedItems.add(orderList.get(i));
                }
            }
            if (selectedItems.size() != 1) {
                AlertMessage.showAlertErrorMessage("Please select exactly one order to view details.");
                return;
            }
            orderSelected = selectedItems.get(0);
            if (orderSelected != null) {
                Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Order/orderDetail.fxml");
                stage.setOnHidden(e -> reload());
            }
        });
    }
}