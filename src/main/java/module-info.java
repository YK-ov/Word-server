module pl.umcs.oop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens pl.umcs.oop.client to javafx.fxml;
    exports pl.umcs.oop.client;

    exports pl.umcs.oop.server;
}