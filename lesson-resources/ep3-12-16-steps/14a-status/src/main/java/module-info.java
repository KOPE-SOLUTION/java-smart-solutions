module smartfactory.dashboard {
    requires javafx.controls;
    requires javafx.fxml;
    exports smartfactory.model;
    exports smartfactory.service;
    exports smartfactory.ui;
    opens smartfactory.ui to javafx.fxml;
}
