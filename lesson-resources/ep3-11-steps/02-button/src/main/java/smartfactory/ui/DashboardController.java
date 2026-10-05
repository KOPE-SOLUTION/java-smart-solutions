package smartfactory.ui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label statusLabel;

    @FXML
    private void handleHello() {
        statusLabel.setText("Controller รับการกดปุ่มแล้ว");
    }
}
