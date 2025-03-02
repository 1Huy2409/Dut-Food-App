package Helper;
import com.sun.source.util.ParameterNameProvider;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RouteScreen {
    public static void switchRouter(Stage stage, String link)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RouteScreen.class.getResource(link));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
