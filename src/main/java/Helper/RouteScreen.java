package Helper;
import com.sun.source.util.ParameterNameProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RouteScreen {
    private static RouteScreen _Instance;
    public static RouteScreen getInstance()
    {
        if (_Instance == null)
        {
            return new RouteScreen();
        }
        return _Instance;
    }
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
    public Stage newScreen(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            return stage; // Trả về Stage để có thể dùng setOnHidden
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
