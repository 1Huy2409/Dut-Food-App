package Helper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertMessage {
    public static void showAlertErrorMessage(String message)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error!!!");
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void showAlertSuccessMessage(String message)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Success!!!");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
