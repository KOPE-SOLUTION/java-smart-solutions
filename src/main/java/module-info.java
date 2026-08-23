module smartfactory.dashboard {
    requires javafx.controls;
    requires javafx.fxml;

    exports smartfactory.basic;
    exports smartfactory.model;
    exports smartfactory.oop;
    exports smartfactory.service;
    exports smartfactory.ui;

    opens smartfactory.ui to javafx.fxml;
}
