package smartfactory.motion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smartfactory.ui.ThaiUiSupport;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/** Entry point for the STM32F3 USB motion dashboard case study. */
public final class MotionDashboardApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ThaiUiSupport.configureLocale();
        URL fxml = Objects.requireNonNull(
                MotionDashboardApp.class.getResource("/smartfactory/motion/motion-dashboard.fxml"),
                "motion-dashboard.fxml not found"
        );
        FXMLLoader loader = new FXMLLoader(fxml);
        Parent root = loader.load();
        root.setStyle("-fx-font-family: '" + ThaiUiSupport.findPreferredFontFamily() + "';");

        Scene scene = new Scene(root, 1_320, 860);
        URL stylesheet = Objects.requireNonNull(
                MotionDashboardApp.class.getResource("/smartfactory/motion/motion-dashboard.css"),
                "motion-dashboard.css not found"
        );
        scene.getStylesheets().add(stylesheet.toExternalForm());

        MotionDashboardController controller = loader.getController();
        stage.setTitle("KOPE Motion Console · STM32F3");
        stage.setMinWidth(1_120);
        stage.setMinHeight(720);
        stage.setScene(scene);
        stage.setOnHidden(event -> controller.shutdown());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
