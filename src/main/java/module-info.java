module com.example.loginapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
    requires jdk.compiler;
    requires jdk.jconsole;
    requires jakarta.mail;
    requires io.github.cdimascio.dotenv.java;
    requires stripe.java;
    requires itextpdf;
    exports Application;
    opens Application to javafx.fxml;
    exports Controller.Admin.Category;
    opens Controller.Admin.Category to javafx.fxml;
    exports Controller.Admin.Dashboard;
    opens Controller.Admin.Dashboard to javafx.fxml;
    exports Controller.Admin.Product;
    opens Controller.Admin.Product to javafx.fxml;
    exports Controller.Admin.Customer to javafx.fxml;
    opens Controller.Admin.Customer to javafx.fxml;
    exports Controller.Admin.Order to javafx.fxml;
    opens Controller.Admin.Order to javafx.fxml;
    exports Controller.Admin;
    opens Controller.Admin to javafx.fxml;
    exports Controller.Client.Account;
    opens Controller.Client.Account to javafx.fxml;
    exports Controller.Client.Product;
    opens Controller.Client.Product to javafx.fxml;
    exports Controller.Shared.Auth;
    opens Controller.Shared.Auth to javafx.fxml;

    exports Controller.Client;
    opens Controller.Client to javafx.fxml;
    exports Controller.Client.Profile to javafx.fxml;
    opens Controller.Client.Profile to javafx.fxml;
    exports Controller.Client.Payment;
    opens Controller.Client.Payment to javafx.fxml;
    exports Controller.Client.Cart;
    opens Controller.Client.Cart to javafx.fxml;
}