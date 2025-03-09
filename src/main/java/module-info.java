module com.example.loginapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
    requires jdk.compiler;
    requires jdk.jconsole;
    opens Controller to javafx.fxml;
    exports Controller;
    exports Application;
    opens Application to javafx.fxml;
}