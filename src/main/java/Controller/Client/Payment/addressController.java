package Controller.Client.Payment;

import Helper.AlertMessage;
import Helper.Validation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.OrderInfo;
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
    private Stage popupStage;
    private Runnable onConfirm;
    private Runnable onCancel;

    public void initialize() {
        cbbCity.getItems().addAll(
                "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bắc Ninh",
                "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau",
                "Cần Thơ", "Cao Bằng", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai",
                "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương",
                "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang",
                "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định",
                "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình",
                "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La",
                "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên Huế",
                "Tiền Giang", "TP Hồ Chí Minh", "Trà Vinh", "Tuyên Quang", "Vĩnh Long",
                "Vĩnh Phúc", "Yên Bái"
        );

    }
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
        if(!Validation.isValidPhone(txtSdt.getText())){
            AlertMessage.showAlertErrorMessage("Invalid phone number");
            return;
        }
        if(getAddress() == null ){
            AlertMessage.showAlertErrorMessage("Please enter complete information");
            return;
        }
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
    public OrderInfo getAddress() {
        if(!txtName.getText().isEmpty() && !txtSdt.getText().isEmpty() && !address.getText().isEmpty() && !cbbCity.getValue().isEmpty()){
            OrderInfo orderInfo = new OrderInfo(txtName.getText(), "(+84) " + txtSdt.getText(), address.getText() + ", " + cbbCity.getValue());
            return orderInfo;
        }
        return null;
    }

}