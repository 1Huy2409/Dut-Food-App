package Controller.Admin.Product;

import DAO.Dao_Category;
import DAO.Dao_Food;
import Helper.AlertMessage;
import Helper.RouteScreen;
import Model.Category;
import Model.FoodItem;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javafx.geometry.Pos;
public class productController {
    @FXML
    private Button selectAll;
    private final ConcurrentHashMap<String, Image> imageCache = new ConcurrentHashMap<>();
    @FXML private Button add;
    @FXML private Button reload;

    @FXML
    private TableView<FoodItem> productTable;
    @FXML
    private Button demul;
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
    private TableColumn<FoodItem, String> categoryColumn;
    @FXML
    private TableColumn<FoodItem, String> imageColumn;
    protected static FoodItem foodItemSelected = null;
//    protected static FoodItem multifoodItemSelected = null;
    private ObservableList<FoodItem> productList;
    private ObservableList<BooleanProperty> checkboxStates; // Trạng thái checkbox
    private final ConcurrentHashMap<Integer, String> categoryMap = new ConcurrentHashMap<>();
    @FXML
    public void initialize()
    {
        productTable.getStylesheets().add(getClass().getResource("/CSS/table-style.css").toExternalForm());
        reload();
    }
    private void loadCategories() {
        List<Category> categories = Dao_Category.getInstance().getAll();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getCategoryName());
        }
    }
    public void reload() {
        loadCategories();
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
        categoryColumn.setCellValueFactory(cellData -> {
            int categoryId = cellData.getValue().getCategoryId();
            String categoryName = categoryMap.getOrDefault(categoryId, " ");
            return new SimpleStringProperty(categoryName);
        });
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
                // Thêm class CSS cho nút delete
                deleteButton.getStyleClass().add("delete-button");

                editButton.setOnAction(event -> {
                    foodItemSelected = getTableView().getItems().get(getIndex());
                    if (foodItemSelected != null) {
                        Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Product/editProduct.fxml");
                        stage.setOnHidden(e -> reload());
                    }
                });

                deleteButton.setOnAction(event -> {
                    foodItemSelected = getTableView().getItems().get(getIndex());
                    boolean confirm = AlertMessage.showConfirm("Are you sure you want to delete this dish?");
                    if (confirm) {
                        Dao_Food.getInstance().delete(productController.foodItemSelected);
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
                    buttons.setAlignment(Pos.CENTER_LEFT);
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
        add.setOnAction(event -> {
            Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Product/addProduct.fxml");
            stage.setOnHidden(e -> reload());
        });
        demul.setOnAction(event -> {
            List<FoodItem> selectedItems = new ArrayList<FoodItem>();
            for (int i = 0; i < productList.size(); i++) {
                if (checkboxStates.get(i).get()) {
                    selectedItems.add(productList.get(i));
                }
            }
            for (FoodItem items : selectedItems) {
                Dao_Food.getInstance().delete(items);
            }
            for (BooleanProperty state : checkboxStates) {
                state.set(false); // Hủy chọn tất cả
            }

        });
        reload.setOnAction(event -> {
            reload();
        });
    }
    private void loadImageAsync(String imageUrl, ImageView imageView) {
        if (imageCache.containsKey(imageUrl)) {
            Platform.runLater(() -> {
                imageView.setImage(imageCache.get(imageUrl));
                imageView.setStyle("-fx-opacity: 1;"); // Hiển thị rõ khi đã load xong
            });
            return;
        }

        // Hiệu ứng loading
        imageView.setStyle("-fx-opacity: 0.5;");

        new Thread(() -> {
            File file = new File("src/main/resources/" + imageUrl);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString(), 80, 80, false, true, true);
                imageCache.put(imageUrl, image);
                Platform.runLater(() -> {
                    imageView.setImage(image);
                    imageView.setStyle("-fx-opacity: 1;");
                });
            } else {
                System.err.println("Không tìm thấy ảnh: " + file.getAbsolutePath());
                Platform.runLater(() -> {
                    imageView.setStyle("-fx-opacity: 1;");
                    // Có thể thêm icon ảnh lỗi ở đây
                });
            }
        }).start();
    }

    @FXML private void handleSelectAll() {
        checkboxStates.forEach(state -> state.set(true)); // Chọn tất cả
        productTable.refresh();
    }
}