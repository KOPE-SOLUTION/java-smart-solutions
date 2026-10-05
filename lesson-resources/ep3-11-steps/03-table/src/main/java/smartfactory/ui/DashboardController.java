package smartfactory.ui;
import java.util.Locale;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import smartfactory.model.Machine;
import smartfactory.service.SmartFactoryService;

public class DashboardController {
    @FXML private Label statusLabel;
    @FXML private TableView<Machine> machineTable;
    private final SmartFactoryService service = SmartFactoryService.createWithSampleData();
    private final ObservableList<Machine> machines = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureTable();
        refreshDashboard();
    }

    private void configureTable() {
        TableColumn<Machine, String> idColumn = new TableColumn<>("รหัส");
        idColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getId()));

        TableColumn<Machine, String> nameColumn = new TableColumn<>("ชื่อเครื่องจักร");
        nameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));

        TableColumn<Machine, String> locationColumn = new TableColumn<>("ตำแหน่ง");
        locationColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getLocation()));

        TableColumn<Machine, String> statusColumn = new TableColumn<>("สถานะ");
        statusColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getStatus().getDisplayName()));
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                getStyleClass().removeAll("status-running", "status-warning", "status-emergency", "status-offline");

                if (empty || status == null) {
                    setText(null);
                    return;
                }

                setText(status);
                String style = switch (status) {
                    case "Sensor ผิดปกติ" -> "status-warning";
                    case "หยุดฉุกเฉิน" -> "status-emergency";
                    case "ปิดเครื่อง" -> "status-offline";
                    default -> "status-running";
                };
                getStyleClass().add(style);
            }
        });

        TableColumn<Machine, Integer> hoursColumn = new TableColumn<>("ชั่วโมง");
        hoursColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getOperatingHours()));

        TableColumn<Machine, String> maintenanceColumn = new TableColumn<>("บำรุงรักษา");
        maintenanceColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().requiresMaintenance() ? "ต้องบำรุง" : "ปกติ"));

        TableColumn<Machine, String> temperatureColumn = new TableColumn<>("อุณหภูมิ °C");
        temperatureColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(format(data.getValue().getLatestReading().getTemperature())));

        TableColumn<Machine, String> vibrationColumn = new TableColumn<>("แรงสั่น mm/s");
        vibrationColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(format(data.getValue().getLatestReading().getVibration())));

        machineTable.getColumns().setAll(idColumn, nameColumn, locationColumn, statusColumn,
                temperatureColumn, vibrationColumn, hoursColumn, maintenanceColumn);
        machineTable.setItems(machines);
    }

    private static String format(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    private void refreshDashboard() {
        machines.setAll(service.getMachines());
        machineTable.refresh();
    }
}
