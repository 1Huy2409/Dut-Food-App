package Controller.Admin.Product;

import DAO.Dao_Food;
import Model.FoodItem;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class productController {
    @FXML
    private Button selectAll;
    private final ConcurrentHashMap<String, Image> imageCache = new ConcurrentHashMap<>();
    @FXML private Button add;
    @FXML private Button reload;

    @FXML
    private TableView<FoodItem> productTable;

    @FXML
    private TableColumn<FoodItem, Number> idColumn;
    @FXML
    private TableColumn<FoodItem, String> nameColumn;
    @FXML
    private TableColumn<FoodItem, String> created_timeColumn;
    @FXML
    private TableColumn<FoodItem, Boolean> selectColumn;
    @FXML
    private TableColumn<FoodItem, Void> actionColumn;
    @FXML
    private TableColumn<FoodItem, Number> priceColumn;
    @FXML
    private TableColumn<FoodItem, Number> categoryColumn;
    @FXML
    private TableColumn<FoodItem, String> imageColumn;

    private ObservableList<FoodItem> productList;
    private ObservableList<BooleanProperty> checkboxStates; // Trạng thái checkbox

    @FXML
    public void initialize()
    {
        List<FoodItem> listFoodItems = new ArrayList<>();
        listFoodItems = Dao_Food.getInstance().getAll();
        productList = FXCollections.observableArrayList(
                listFoodItems
        );
        checkboxStates = FXCollections.observableArrayList();
        for (int i = 0; i < productList.size(); i++) {
            checkboxStates.add(new SimpleBooleanProperty(false)); // mặc định chưa tick
        }

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFoodName()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCategoryId()));
        imageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImageUrl()));
        imageColumn.setCellFactory(column -> new TableCell<FoodItem, String>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(80);  // Giảm kích thước ảnh để tăng tốc độ
                imageView.setFitHeight(80);
                imageView.setPreserveRatio(false);
                imageView.setSmooth(true);
            }
            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                } else {
                    imageView.setImage(null); // Xóa ảnh cũ trước khi tải mới
                    setGraphic(imageView);
                    loadImageAsync(imageUrl, imageView);
                }
            }
        });
        created_timeColumn.setCellValueFactory(cellData -> {
            Timestamp timestamp = cellData.getValue().getCreatedAt();
            String formattedDate = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return new SimpleStringProperty(formattedDate);
        });
        actionColumn.setCellFactory(param -> new TableCell<FoodItem, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    FoodItem fooditem = getTableView().getItems().get(getIndex());
                    // thuc hien logic edit
                });

                deleteButton.setOnAction(event -> {
                    FoodItem fooditem = getTableView().getItems().get(getIndex());
                    // thuc hien logic delete
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
            int index = productList.indexOf(cellData.getValue());
            // Trả về BooleanProperty đại diện cho trạng thái checkbox
            return checkboxStates.get(index);
        });

        selectColumn.setCellFactory(tc -> {
            CheckBoxTableCell<FoodItem, Boolean> checkBoxCell = new CheckBoxTableCell<>();
            checkBoxCell.setEditable(true); // Cho phép chỉnh sửa trực tiếp trên bảng
            return checkBoxCell;
        });

        productTable.setEditable(true); // Bật chế độ chỉnh sửa bảng

        selectAll.setOnAction(event -> {
            boolean allSelected = checkboxStates.stream().allMatch(BooleanProperty::get);
            checkboxStates.forEach(state -> state.set(!allSelected)); // Đảo trạng thái
            productTable.refresh();
        });

        productTable.setItems(productList);
    }
    private void loadImageAsync(String imageUrl, ImageView imageView) {
        if (imageCache.containsKey(imageUrl)) {
            Platform.runLater(() -> imageView.setImage(imageCache.get(imageUrl))); // Dùng ảnh đã cache
            return;
        }

        new Thread(() -> {
            File file = new File("src/main/resources/" + imageUrl);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString(), 80, 80, false, true); // Giảm kích thước ảnh
                imageCache.put(imageUrl, image); // Lưu vào cache
                Platform.runLater(() -> imageView.setImage(image)); // Cập nhật UI
            } else {
                System.err.println("Không tìm thấy ảnh: " + file.getAbsolutePath());
            }
        }).start();
    }

    @FXML private void handleSelectAll() {
        checkboxStates.forEach(state -> state.set(true)); // Chọn tất cả
        productTable.refresh();
    }
}