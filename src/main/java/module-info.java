module com.example.loginapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    opens Controller to javafx.fxml;
    exports Controller;
    exports Application;
    opens Application to javafx.fxml;
}