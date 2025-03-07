package Controller;

import Model.Category;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class catagoryController {
    @FXML private Button selectAll;

    @FXML private Button Add;

    @FXML
    private TableView<Category> catagoryTable;

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
    private ObservableList<BooleanProperty> checkboxStates; // Trạng thái checkbox

    @FXML
    public void initialize() {
        catagoryList = FXCollections.observableArrayList(
                new Category(1, "mon trang mien", "hihi", Timestamp.valueOf("7/3/2025"), true),
                new Category(2, "nuoc ngot", "hehe", Timestamp.valueOf("8/3/2025"), true)
        );

        checkboxStates = FXCollections.observableArrayList();
        catagoryList.forEach(item -> checkboxStates.add(new SimpleBooleanProperty(false))); // Mặc định là chưa chọn

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
                    Category category = getTableView().getItems().get(getIndex());
                    // thuc hien logic edit
                });

                deleteButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
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

        selectColumn.setCellValueFactory(cellData -> checkboxStates.get(catagoryList.indexOf(cellData.getValue())));
        selectColumn.setCellFactory(tc -> new CheckBoxTableCell<>());

        catagoryTable.setItems(catagoryList);
    }



    @FXML private void handleSelectAll() {
        checkboxStates.forEach(state -> state.set(true)); // Chọn tất cả
        catagoryTable.refresh();
    }
}
