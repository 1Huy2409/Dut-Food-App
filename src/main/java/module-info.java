module com.example.loginapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.loginapp to javafx.fxml;
    exports com.example.loginapp;
}