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
import javafx.stage.Stage;

public class categoryController {
    @FXML
    private Button selectAll;
    @FXML
    private Button add;
    @FXML
    private Button reloadBtn;
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
    private ObservableList<Category> categoryList;
    private ObservableList<BooleanProperty> checkboxStates;
    protected static Category categorySelected = null;
    @FXML
    public void initialize() {
        // get stylesheets for table
        functional.getStylesheets().add(getClass().getResource("/CSS/table-style.css").toExternalForm());
        categoryTable.getStylesheets().add(getClass().getResource("/CSS/table-style.css").toExternalForm());
        // configure table columns
        categoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        categoryTable.setSelectionModel(null);
        selectColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.1));
        idColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.10));
        categoryNameColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.2));
        created_timeColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.2));
        actionColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.25));
        statusColumn.prefWidthProperty().bind(categoryTable.widthProperty().multiply(0.15));
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
        loadCategoryData();
        setupTableColumns();
        setupActionColumn();
        categoryTable.setEditable(true);
        setupButtonsAction();
    }
    private void loadCategoryData() {
        List<Category> listCategory = Dao_Category.getInstance().getAll();
        categoryList = FXCollections.observableArrayList(listCategory);
        checkboxStates = FXCollections.observableArrayList();
        for (int i = 0; i < categoryList.size(); i++) {
            checkboxStates.add(new SimpleBooleanProperty(false));
        }
    }
    private void setupTableColumns() {
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
        selectColumn.setCellValueFactory(cellData -> {
            int index = categoryList.indexOf(cellData.getValue());
            return checkboxStates.get(index);
        });

        selectColumn.setCellFactory(tc -> {
            CheckBoxTableCell<Category, Boolean> checkBoxCell = new CheckBoxTableCell<>();
            checkBoxCell.setEditable(true);
            return checkBoxCell;
        });
    }
    private void setupActionColumn()
    {
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
                    boolean confirm = AlertMessage.showConfirm("Are you sure you want to delete this category?");
                    if (confirm) {
                        DeleteCategoryWithFood(category);
                        AlertMessage.showAlertSuccessMessage("You have deleted this category!");
                        reload();
                    }
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
    }
    private void setupButtonsAction() {
        selectAll.setOnAction(event -> {
            boolean allSelected = checkboxStates.stream().allMatch(BooleanProperty::get);
            checkboxStates.forEach(state -> state.set(!allSelected)); // Đảo trạng thái
            categoryTable.refresh();
        });

        categoryTable.setItems(categoryList);
        add.setOnAction(event -> {
            Stage stage = RouteScreen.getInstance().newScreen("/View/Admin/Category/addCategory.fxml");
            stage.setOnHidden(e -> reload());
        });
        reloadBtn.setOnAction(event -> {
            reload();
        });
        multipleBtn.setOnAction(actionEvent -> {
            List<Category> selectedItems = new ArrayList<Category>();
            for (int i = 0; i < categoryList.size(); i++) {
                if (checkboxStates.get(i).get()) {
                    selectedItems.add(categoryList.get(i));
                }
            }
            boolean confirm = AlertMessage.showConfirm("Are you sure you want to delete ?");
            if(confirm){
                for (Category item : selectedItems) {
                    DeleteCategoryWithFood(item);
                }
                for (BooleanProperty state : checkboxStates) {
                    state.set(false);
                }
                AlertMessage.showAlertSuccessMessage("Deleted successfully");
                reload();
            }

        });
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
    public void DeleteCategoryWithFood(Category category)
    {
        List<FoodItem> listFoodItem = Dao_Food.getInstance().selectByCategory(category.getId());
        for (FoodItem item : listFoodItem)
        {
            Dao_Food.getInstance().updateStatus(item, false);
        }
        category.setStatus(false);
        Dao_Category.getInstance().update(category);
    }
}
