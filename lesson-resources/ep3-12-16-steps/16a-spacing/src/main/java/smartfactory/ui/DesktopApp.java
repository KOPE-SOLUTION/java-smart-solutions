package smartfactory.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DesktopApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 700);
        scene.getStylesheets().add(getClass().getResource("smart-factory.css").toExternalForm());
        stage.setTitle("KOPE SOLUTION - Smart Factory Dashboard");
        stage.setScene(scene);
        DashboardController controller = loader.getController();
        stage.setOnHidden(event -> controller.stopBackgroundWork());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
