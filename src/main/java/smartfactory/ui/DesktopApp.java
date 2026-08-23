package smartfactory.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smartfactory.service.SmartFactoryService;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/** จุดเริ่มต้นของ JavaFX Smart Factory Dashboard */
public final class DesktopApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ThaiUiSupport.configureLocale();

        SmartFactoryService service = SmartFactoryService.createWithSampleData();
        URL fxml = Objects.requireNonNull(
                DesktopApp.class.getResource("/smartfactory/ui/dashboard-view.fxml"),
                "dashboard-view.fxml not found"
        );

        FXMLLoader loader = new FXMLLoader(fxml);
        loader.setControllerFactory(type -> {
            if (type == DashboardController.class) {
                return new DashboardController(service);
            }
            throw new IllegalArgumentException("Unsupported controller: " + type.getName());
        });

        Parent root = loader.load();
        root.setStyle("-fx-font-family: '" + ThaiUiSupport.findPreferredFontFamily() + "';");

        Scene scene = new Scene(root, 1_240, 760);
        URL stylesheet = Objects.requireNonNull(
                DesktopApp.class.getResource("/smartfactory/ui/smart-factory.css"),
                "smart-factory.css not found"
        );
        scene.getStylesheets().add(stylesheet.toExternalForm());

        DashboardController controller = loader.getController();
        stage.setTitle("Smart Factory Dashboard");
        stage.setMinWidth(1_080);
        stage.setMinHeight(680);
        stage.setScene(scene);
        stage.setOnHidden(event -> controller.shutdown());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
