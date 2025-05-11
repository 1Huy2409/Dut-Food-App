package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/Font/Dancing_Script/static/DancingScript-Regular.ttf"),10);
        Font.loadFont(getClass().getResourceAsStream("/Font/Dancing_Script/static/DancingScript-Bold.ttf"),10);
        Font.loadFont(getClass().getResourceAsStream("/Font/Dancing_Script/static/DancingScript-Medium.ttf"),10);
        Font.loadFont(getClass().getResourceAsStream("/Font/Dancing_Script/static/DancingScript-SemiBold.ttf"),10);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Shared/login.fxml")); // Đúng
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Admin/admin_dashboard.fxml")); // Đúng
        Scene scene = new Scene(fxmlLoader.load(), 684, 437);
//        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Food App!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
