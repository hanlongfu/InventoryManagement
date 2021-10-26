module InventoryManagement {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    opens Main;
    opens model;
    opens controller;

    exports Main;
    exports model;
    exports controller;

}