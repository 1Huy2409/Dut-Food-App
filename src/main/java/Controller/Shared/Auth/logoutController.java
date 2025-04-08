package Controller.Shared.Auth;

import Helper.RouteScreen;
import Helper.UserSession;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class logoutController {
    @FXML
    private HBox btnLogout;
    public void handleLogout(MouseEvent e)
    {
        UserSession.getInstance().clearSession();
        Stage currentStage = (Stage) btnLogout.getScene().getWindow();
        currentStage.close();
        RouteScreen.getInstance().newScreen("/View/Shared/login.fxml");
    }
}
