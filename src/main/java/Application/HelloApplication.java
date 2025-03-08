package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Shared/login.fxml")); // Đúng
//        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//        stage.setTitle("Food App!");
//        stage.setScene(scene);
//        stage.show();
//    }
@Override
public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Admin/category.fxml")); // Đúng
    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
    stage.setTitle("Food App!");
    stage.setScene(scene);
    stage.show();
}



    public static void main(String[] args) {
        launch();
    }
}
