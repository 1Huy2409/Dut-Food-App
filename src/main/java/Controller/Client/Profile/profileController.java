package Controller.Client.Profile;

import DAO.Dao_User;
import Helper.AlertMessage;
import Helper.PasswordHelper;
import Helper.RouteScreen;
import Helper.UserSession;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class profileController {
    @FXML
    private AnchorPane anchoACC;

    @FXML
    private Button btnEACC;

    @FXML
    private Button btnLog;

    @FXML
    private Button btnUI;

    @FXML
    private PasswordField confirmpass;

    @FXML
    private TextField email;

    @FXML
    private Label err;

    @FXML
    private TextField fullname;

    @FXML
    private ImageView img;

    @FXML
    private Label lbemail;

    @FXML
    private Label lbfullname;

    @FXML
    private PasswordField pass;

    @FXML
    private TextField phone;

    @FXML
    private Button saveacc;

    @FXML
    private TextField username;

    @FXML
    private ImageView imgdefault;

    @FXML
    private ImageView imguser;
    private String currentImagePath = null;
    public void initialize() {
        anchoACC.setVisible(false);
        loadInfo(Dao_User.getInstance().selectedById(UserSession.getInstance().getId()));
    }
    public void EditAccountAction(ActionEvent event){
        anchoACC.setVisible(true);
    }
    public void loadInfo(User user){
        lbfullname.setText(user.getFullName());
        lbemail.setText(user.getEmail());
        fullname.setText(user.getFullName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            try {
                Image image = new Image(user.getImage(), 100, 100, true, true);
                imguser.setImage(image);
                imguser.setFitWidth(100);
                imguser.setFitHeight(100);
                imguser.setPreserveRatio(false);

                Ellipse clip = new Ellipse();
                clip.radiusXProperty().bind(imguser.fitWidthProperty().divide(2));
                clip.radiusYProperty().bind(imguser.fitHeightProperty().divide(2));
                clip.centerXProperty().bind(imguser.fitWidthProperty().divide(2));
                clip.centerYProperty().bind(imguser.fitHeightProperty().divide(2));
                imguser.setClip(clip);

                imgdefault.setVisible(false);
                imguser.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void importImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            currentImagePath = file.toURI().toString();
            try {
                // Tạo Image và đặt preserveRatio để giữ tỉ lệ ảnh gốc
                Image image = new Image(file.toURI().toString(), 100, 100, true, true);

                // Đặt hình ảnh vào ImageView
                imguser.setImage(image);

                // Đặt kích thước cố định cho ImageView
                imguser.setFitWidth(100);
                imguser.setFitHeight(100);
                imguser.setPreserveRatio(false); // Tắt preserveRatio để ảnh lấp đầy kích thước

                // Tạo mặt nạ hình elip
                Ellipse clip = new Ellipse();
                clip.radiusXProperty().bind(imguser.fitWidthProperty().divide(2));
                clip.radiusYProperty().bind(imguser.fitHeightProperty().divide(2));
                clip.centerXProperty().bind(imguser.fitWidthProperty().divide(2));
                clip.centerYProperty().bind(imguser.fitHeightProperty().divide(2));

                imguser.setClip(clip);
                imgdefault.setVisible(false);
                imguser.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                // Xử lý lỗi nếu cần
            }
        }
    }
    public void btnSavaChange(){
        boolean checkinfo = true;
        boolean checkpass = true;
        String password = "";
        User user = new User();
        user = Dao_User.getInstance().selectedById(UserSession.getInstance().getId());
        if(pass.getText().equals(confirmpass.getText())){
            password = pass.getText();
            err.setText("");
        }
        else{
            checkpass = false;
            err.setText("Password confirmation does not match");
        }
        if(!username.getText().trim().isEmpty()){
            user.setUserName(username.getText());
        }
        else {
            checkinfo = false;
            AlertMessage.showAlertErrorMessage("Please fill in complete information");
        }
        if(checkpass){
            user.setPassWord(PasswordHelper.hashPassword(password));
        }

        if(checkinfo && checkpass){
            Dao_User.getInstance().updateAccount(user);
            AlertMessage.showAlertSuccessMessage("Completed change!");
            username.setText("");
            pass.setText("");
            confirmpass.setText("");
        }
    }
    public void saveUI(){
        if(!fullname.getText().trim().isEmpty() && !email.getText().trim().isEmpty() && !phone.getText().trim().isEmpty()){
            User user = Dao_User.getInstance().selectedById(UserSession.getInstance().getId());
            user.setFullName(fullname.getText());
            user.setEmail(email.getText());
            user.setPhone(phone.getText());
            if(currentImagePath!= null){
                user.setImage(currentImagePath);
            }
            Dao_User.getInstance().update(user);
            loadInfo(Dao_User.getInstance().selectedById(UserSession.getInstance().getId()));
            AlertMessage.showAlertSuccessMessage("Completed change!");
        }
        else{
            AlertMessage.showAlertErrorMessage("Please fill in complete information");
        }

    }
    public void logout(){
        UserSession.getInstance().clearSession();
        Stage currentStage = (Stage) btnLog.getScene().getWindow();
        currentStage.close();
        RouteScreen.getInstance().newScreen("/View/Shared/login.fxml");
    }

}
