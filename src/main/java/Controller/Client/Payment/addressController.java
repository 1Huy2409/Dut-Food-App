package Controller.Client.Payment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addressController {
    @FXML
    private TextArea address;

    @FXML
    private Button btnCa;

    @FXML
    private Button btnOK;

    @FXML
    private ComboBox<String> cbbCity;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSdt;
    String address1;
    private boolean isConfirmed = false;
    private boolean isCancelled = false;
    public void initialize() {
        cbbCity.getItems().addAll("Huế", "Hà Nội", "TP Hồ Chí Minh");
//        btnOK.setOnAction(e -> {
//            address1 = txtName.getText();
//            isConfirmed = true;
//            ((Stage) btnOK.getScene().getWindow()).close();
//        });
//
//        btnCa.setOnAction(e -> {
//            ((Stage) btnCa.getScene().getWindow()).close();
//        });
    }
    private Stage popupStage;
    private Runnable onConfirm;
    private Runnable onCancel;

    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }

    public void setOnConfirm(Runnable action) {
        this.onConfirm = action;
    }

    public void setOnCancel(Runnable action) {
        this.onCancel = action;
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        if (onConfirm != null) {
            onConfirm.run();
        }
    }
    @FXML
    private void handleCancel( ActionEvent event) {
        if (onCancel != null) {
            onCancel.run();
        }
    }
    private void closePopup() {
        if (popupStage != null) {
            popupStage.close();
        }
    }
    public boolean isCancelled() {
        return isCancelled;
    }

    public String getAddress() {
        return txtName.getText() +", " + txtSdt.getText() +", " + address.getText() + ", " + cbbCity.getValue();
    }

}
