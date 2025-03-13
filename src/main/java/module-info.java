module com.example.loginapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
    requires jdk.compiler;
    requires jdk.jconsole;
    exports Application;
    opens Application to javafx.fxml;
    exports Controller.Admin.Category;
    opens Controller.Admin.Category to javafx.fxml;
    exports Controller.Admin.Dashboard;
    opens Controller.Admin.Dashboard to javafx.fxml;
    exports Controller.Admin.Product;
    opens Controller.Admin.Product to javafx.fxml;
    exports Controller.Admin.User;
    opens Controller.Admin.User to javafx.fxml;
    exports Controller.Admin;
    opens Controller.Admin to javafx.fxml;
    exports Controller.Client.Account;
    opens Controller.Client.Account to javafx.fxml;
    exports Controller.Shared;
    opens Controller.Shared to javafx.fxml;
}