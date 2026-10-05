package smartfactory.ui;
import java.util.List;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.util.Duration;
import smartfactory.desktop.SensorSimulationTask;
import smartfactory.desktop.SensorUpdate;
import java.util.Locale;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import smartfactory.model.Machine;
import smartfactory.service.SmartFactoryService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import smartfactory.model.MachineStatus;

public class DashboardController {
    @FXML private Label statusLabel;
    @FXML private TableView<Machine> machineTable;
    private final SmartFactoryService service = SmartFactoryService.createWithSampleData();
    private final ObservableList<Machine> machines = FXCollections.observableArrayList();
    @FXML private TextField searchField;
    @FXML private Label filterResultLabel;
    private final FilteredList<Machine> filteredMachines =
            new FilteredList<>(machines, machine -> true);
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> maintenanceFilter;
    @FXML private Label totalLabel;
    @FXML private Label normalLabel;
    @FXML private Label warningLabel;
    @FXML private Label emergencyLabel;
    @FXML private Label maintenanceLabel;
    private final IntegerProperty machineCount = new SimpleIntegerProperty(0);
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private Button addButton;
    @FXML private Button sensorButton;
    @FXML private Button autoSensorButton;
    private boolean sensorBusy;
    private Timeline sensorTimeline;
    private Task<?> activeSensorTask;

    @FXML
    private void initialize() {
        totalLabel.textProperty().bind(machineCount.asString("ทั้งหมด: %d"));
        sensorTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> simulateInBackground())
        );
        sensorTimeline.setCycleCount(Timeline.INDEFINITE);
        configureStatusFilter();
        configureMaintenanceFilter();
        configureTable();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applySearch());
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
        machineTable.setItems(filteredMachines);
    }

    private static String format(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    private void refreshDashboard() {
        machines.setAll(service.getMachines());
        applySearch();
        machineTable.refresh();
        machineCount.set(machines.size());
        normalLabel.setText("สถานะปกติ: " + service.countByStatus(MachineStatus.RUNNING));
        warningLabel.setText("Sensor ผิดปกติ: " + service.countByStatus(MachineStatus.WARNING));
        emergencyLabel.setText("หยุดฉุกเฉิน: " + service.countByStatus(MachineStatus.EMERGENCY_STOP));
        maintenanceLabel.setText("ต้องบำรุงทั้งหมด: " + service.countRequiringMaintenance());
    }

    private String requireText(TextField field, String message) {
        String value = field.getText().trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("ข้อมูลไม่ถูกต้อง");
        alert.showAndWait();

    }

    @FXML
    private void handleAddMachine() {
        try {
            String id = requireText(idField, "กรุณากรอกรหัสเครื่องจักร");

            String name = requireText(nameField, "กรุณากรอกชื่อเครื่องจักร");
            String location = requireText(locationField, "กรุณากรอกตำแหน่งเครื่องจักร");

            service.addMachine(new Machine(id, name, location));
            refreshDashboard();
            statusLabel.setText("เพิ่ม " + id + " - " + name + " เรียบร้อยแล้ว");
            idField.clear();
            nameField.clear();
            locationField.clear();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    @FXML
    private void handleDeleteMachine() {
        Machine selected = machineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("กรุณาเลือกเครื่องจักรในตารางก่อน");
            return;
        }
        service.removeMachine(selected.getId());
        refreshDashboard();
        statusLabel.setText("ลบ " + selected.getId() + " แล้ว");
    }

    @FXML
    private void handleMaintenance() {
        Machine selected = machineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("กรุณาเลือกเครื่องจักรในตารางก่อน");
            return;
        }
        service.performMaintenance(selected.getId());
        refreshDashboard();
        statusLabel.setText("บำรุงรักษา " + selected.getId() + " แล้ว");
    }

    @FXML
    private void simulateInBackground() {
        if (sensorBusy) {
            return;
        }
        if (service.getMachines().isEmpty()) {
            statusLabel.setText("ยังไม่มีเครื่องจักรให้จำลอง");
            return;
        }

        List<Machine> snapshot = List.copyOf(service.getMachines());
        List<String> ids = new ArrayList<>();
        for (Machine machine : snapshot) {
            ids.add(machine.getId());
        }

        sensorBusy = true;
        sensorButton.setDisable(true);
        statusLabel.setText("กำลังจำลองค่า Sensor...");

        SensorSimulationTask task = new SensorSimulationTask(ids);
        task.setOnSucceeded(event -> {
            finishSensorTask();
            for (SensorUpdate update : task.getValue()) {
                Machine current = service.findById(update.machineId()).orElse(null);
                if (current == null || !snapshot.contains(current)) {
                    continue;
                }
                service.updateSensor(update.machineId(), update.temperature(), update.vibration());
            }
            refreshDashboard();
            statusLabel.setText("อัปเดต Sensor ล่าสุดแล้ว");
        });

        task.setOnFailed(event -> {
            finishSensorTask();
            statusLabel.setText("จำลองค่า Sensor ไม่สำเร็จ กรุณาลองใหม่");
        });

        task.setOnCancelled(event -> finishSensorTask());
        activeSensorTask = task;

        Thread worker = new Thread(task, "sensor-worker");
        worker.setDaemon(true);
        worker.start();
    }

    @FXML
    private void toggleAutoSensor() {
        if (sensorTimeline.getStatus() == Animation.Status.RUNNING) {
            sensorTimeline.stop();
            autoSensorButton.setText("เริ่ม Auto Sensor");
            statusLabel.setText("หยุด Auto Sensor แล้ว");
        } else {
            sensorTimeline.play();
            autoSensorButton.setText("หยุด Auto Sensor");
            statusLabel.setText("เริ่ม Auto Sensor แล้ว");
        }
    }

    private void finishSensorTask() {
        sensorBusy = false;
        sensorButton.setDisable(false);
        activeSensorTask = null; // ล้างการอ้างถึงงานที่จบแล้ว
    }

    public void stopBackgroundWork() {
        sensorTimeline.stop();
        if (activeSensorTask != null) {
            activeSensorTask.cancel();
        }
    }

    private void applySearch() {
        String keyword = searchField.getText().trim().toLowerCase(Locale.ROOT);
        filteredMachines.setPredicate(machine -> {
            boolean matchesText = machine.getId().toLowerCase(Locale.ROOT).contains(keyword)
                    || machine.getName().toLowerCase(Locale.ROOT).contains(keyword)
                    || machine.getLocation().toLowerCase(Locale.ROOT).contains(keyword);
            boolean matchesStatus = "ทุกสถานะ".equals(statusFilter.getValue())
                    || machine.getStatus().getDisplayName().equals(statusFilter.getValue());
            boolean matchesMaintenance = switch (maintenanceFilter.getValue()) {
                case "ต้องบำรุง" -> machine.requiresMaintenance();
                case "ยังไม่ต้องบำรุง" -> !machine.requiresMaintenance();
                default -> true;
            };
            return matchesText && matchesStatus && matchesMaintenance;
        });
        filterResultLabel.setText("พบ " + filteredMachines.size() + " / " + machines.size() + " เครื่อง");
    }

    private void configureStatusFilter() {
        statusFilter.getItems().add("ทุกสถานะ");
        for (MachineStatus status : MachineStatus.values()) {
            statusFilter.getItems().add(status.getDisplayName());
        }
        statusFilter.setValue("ทุกสถานะ");
        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> applySearch());
    }

    private void configureMaintenanceFilter() {
        maintenanceFilter.getItems().setAll("ทุกเครื่อง", "ต้องบำรุง", "ยังไม่ต้องบำรุง");
        maintenanceFilter.setValue("ทุกเครื่อง");
        maintenanceFilter.valueProperty().addListener((observable, oldValue, newValue) -> applySearch());
    }
}
