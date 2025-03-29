package Helper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

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
    public static boolean showConfirm(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
