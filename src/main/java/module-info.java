module smartfactory.dashboard {
    requires com.fazecast.jSerialComm;
    requires javafx.controls;
    requires javafx.fxml;

    exports smartfactory.basic;
    exports smartfactory.model;
    exports smartfactory.oop;
    exports smartfactory.service;
    exports smartfactory.motion;
    exports smartfactory.ui;

    opens smartfactory.motion to javafx.fxml;
    opens smartfactory.ui to javafx.fxml;
}
