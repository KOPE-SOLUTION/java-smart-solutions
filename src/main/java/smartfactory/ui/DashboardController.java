package smartfactory.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import smartfactory.model.Machine;
import smartfactory.model.MachineStatus;
import smartfactory.model.SensorReading;
import smartfactory.service.SmartFactoryService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** Controller ประสาน View กับ SmartFactoryService โดยไม่ย้ายกฎของ Model มาไว้ใน UI */
public final class DashboardController {
    private static final String ALL_STATUSES = "ทุกสถานะ";
    private static final String ALL_MAINTENANCE = "ทุกเงื่อนไขบำรุงรักษา";
    private static final String MAINTENANCE_REQUIRED = "ต้องบำรุง";
    private static final String MAINTENANCE_NORMAL = "ปกติ";
    private static final List<String> STATUS_STYLES = List.of(
            "status-running", "status-warning", "status-emergency", "status-offline", "status-maintenance"
    );

    private final SmartFactoryService service;
    private final ObservableList<Machine> machineItems = FXCollections.observableArrayList();
    private final FilteredList<Machine> filteredMachines = new FilteredList<>(machineItems, machine -> true);
    private final SortedList<Machine> sortedMachines = new SortedList<>(filteredMachines);
    private final IntegerProperty totalMachines = new SimpleIntegerProperty();
    private final IntegerProperty runningMachines = new SimpleIntegerProperty();
    private final IntegerProperty warningMachines = new SimpleIntegerProperty();
    private final IntegerProperty emergencyMachines = new SimpleIntegerProperty();
    private final IntegerProperty maintenanceMachines = new SimpleIntegerProperty();

    @FXML private Label totalValue;
    @FXML private Label runningValue;
    @FXML private Label warningValue;
    @FXML private Label emergencyValue;
    @FXML private Label maintenanceValue;
    @FXML private Label statusMessage;
    @FXML private TableView<Machine> machineTable;
    @FXML private TableColumn<Machine, String> idColumn;
    @FXML private TableColumn<Machine, String> nameColumn;
    @FXML private TableColumn<Machine, String> locationColumn;
    @FXML private TableColumn<Machine, MachineStatus> statusColumn;
    @FXML private TableColumn<Machine, String> temperatureColumn;
    @FXML private TableColumn<Machine, String> vibrationColumn;
    @FXML private TableColumn<Machine, Integer> hoursColumn;
    @FXML private TableColumn<Machine, String> maintenanceColumn;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> maintenanceFilter;
    @FXML private Label filterResultLabel;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private Label formModeLabel;
    @FXML private Button addMachineButton;
    @FXML private Button editMachineButton;
    @FXML private Button cancelEditButton;
    @FXML private TextField temperatureField;
    @FXML private TextField vibrationField;
    @FXML private Button autoButton;

    private Timeline simulationTimeline;
    private boolean simulationRunning;

    public DashboardController(SmartFactoryService service) {
        this.service = service;
    }

    @FXML
    private void initialize() {
        configureTable();
        configureFilters();
        bindSummaryCards();
        sortedMachines.comparatorProperty().bind(machineTable.comparatorProperty());
        machineTable.setItems(sortedMachines);
        machineTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, previous, selected) -> handleMachineSelection(selected)
        );

        simulationTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> simulateInBackground())
        );
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshDashboard();
    }

    private void configureTable() {
        machineTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
        locationColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getLocation()));
        statusColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getStatus()));
        temperatureColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                format(data.getValue().getLatestReading().getTemperature())
        ));
        vibrationColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                format(data.getValue().getLatestReading().getVibration())
        ));
        hoursColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getOperatingHours()));
        maintenanceColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().requiresMaintenance() ? "ต้องบำรุง" : "ปกติ"
        ));

        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(MachineStatus status, boolean empty) {
                super.updateItem(status, empty);
                getStyleClass().removeAll(STATUS_STYLES);
                if (empty || status == null) {
                    setText(null);
                    return;
                }
                setText(status.getDisplayName());
                getStyleClass().add(statusStyle(status));
            }
        });
    }

    private void configureFilters() {
        statusFilter.getItems().add(ALL_STATUSES);
        for (MachineStatus status : MachineStatus.values()) {
            statusFilter.getItems().add(status.getDisplayName());
        }
        statusFilter.setValue(ALL_STATUSES);

        maintenanceFilter.getItems().setAll(
                ALL_MAINTENANCE,
                MAINTENANCE_REQUIRED,
                MAINTENANCE_NORMAL
        );
        maintenanceFilter.setValue(ALL_MAINTENANCE);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        maintenanceFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    private void applyFilters() {
        String keyword = normalize(searchField.getText());
        String selectedStatus = statusFilter.getValue();
        String selectedMaintenance = maintenanceFilter.getValue();

        filteredMachines.setPredicate(machine -> {
            boolean matchesKeyword = keyword.isBlank()
                    || normalize(machine.getId()).contains(keyword)
                    || normalize(machine.getName()).contains(keyword)
                    || normalize(machine.getLocation()).contains(keyword);
            boolean matchesStatus = selectedStatus == null
                    || ALL_STATUSES.equals(selectedStatus)
                    || machine.getStatus().getDisplayName().equals(selectedStatus);
            boolean matchesMaintenance = switch (selectedMaintenance == null
                    ? ALL_MAINTENANCE
                    : selectedMaintenance) {
                case MAINTENANCE_REQUIRED -> machine.requiresMaintenance();
                case MAINTENANCE_NORMAL -> !machine.requiresMaintenance();
                default -> true;
            };
            return matchesKeyword && matchesStatus && matchesMaintenance;
        });

        filterResultLabel.setText(
                "แสดง " + filteredMachines.size() + " จาก " + machineItems.size() + " เครื่อง"
        );
    }

    @FXML
    private void handleClearFilters() {
        searchField.clear();
        statusFilter.setValue(ALL_STATUSES);
        maintenanceFilter.setValue(ALL_MAINTENANCE);
        applyFilters();
        showStatus("ล้างการค้นหาและตัวกรองแล้ว");
    }

    private void bindSummaryCards() {
        totalValue.textProperty().bind(totalMachines.asString());
        runningValue.textProperty().bind(runningMachines.asString());
        warningValue.textProperty().bind(warningMachines.asString());
        emergencyValue.textProperty().bind(emergencyMachines.asString());
        maintenanceValue.textProperty().bind(maintenanceMachines.asString());
    }

    @FXML
    private void handleAddMachine() {
        runUiAction(() -> {
            String id = requireText(idField, "กรุณากรอกรหัสเครื่องจักร");
            String name = requireText(nameField, "กรุณากรอกชื่อเครื่องจักร");
            String location = requireText(locationField, "กรุณากรอกตำแหน่ง");
            service.addMachine(new Machine(id, name, location));
            resetMachineForm();
            refreshDashboard();
            showStatus("เพิ่มเครื่องจักร " + id + " แล้ว");
        });
    }

    @FXML
    private void handleUpdateMachine() {
        runUiAction(() -> {
            Machine selected = requireSelectedMachine();
            String name = requireText(nameField, "กรุณากรอกชื่อเครื่องจักร");
            String location = requireText(locationField, "กรุณากรอกตำแหน่ง");
            service.updateMachineDetails(selected.getId(), name, location);
            refreshDashboard();
            machineTable.getSelectionModel().clearSelection();
            resetMachineForm();
            showStatus("แก้ไขข้อมูล " + selected.getId() + " แล้ว");
        });
    }

    @FXML
    private void handleCancelEdit() {
        machineTable.getSelectionModel().clearSelection();
        resetMachineForm();
        showStatus("ยกเลิกการแก้ไขแล้ว");
    }

    @FXML
    private void handleUpdateSensor() {
        runUiAction(() -> {
            Machine selected = requireSelectedMachine();
            double temperature = parseNumber(temperatureField, "อุณหภูมิ");
            double vibration = parseNumber(vibrationField, "แรงสั่นสะเทือน");
            service.updateSensor(selected.getId(), temperature, vibration);
            refreshDashboard();
            showStatusWithMaintenanceCount("อัปเดต Sensor ของ " + selected.getName() + " แล้ว");
        });
    }

    @FXML
    private void handleMaintenance() {
        runUiAction(() -> {
            Machine selected = requireSelectedMachine();
            service.performMaintenance(selected.getId());
            refreshDashboard();
            showStatusWithMaintenanceCount("บันทึกการบำรุงรักษา " + selected.getName() + " แล้ว");
        });
    }

    @FXML
    private void handleDeleteMachine() {
        runUiAction(() -> {
            Machine selected = requireSelectedMachine();
            Alert confirmation = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "ต้องการลบ " + selected.getName() + " หรือไม่?",
                    ButtonType.YES,
                    ButtonType.NO
            );
            confirmation.setHeaderText("ยืนยันการลบเครื่องจักร");
            Optional<ButtonType> answer = confirmation.showAndWait();
            if (answer.orElse(ButtonType.NO) == ButtonType.YES) {
                service.removeMachine(selected.getId());
                refreshDashboard();
                showStatus("ลบเครื่องจักรแล้ว");
            }
        });
    }

    @FXML
    private void handleSimulateOnce() {
        simulateInBackground();
    }

    @FXML
    private void handleToggleAutomaticSimulation() {
        if (simulationTimeline.getStatus() == Timeline.Status.RUNNING) {
            simulationTimeline.stop();
            autoButton.setText("เริ่ม Auto Sensor");
            showStatus("หยุดการจำลองอัตโนมัติแล้ว");
        } else {
            simulateInBackground();
            simulationTimeline.play();
            autoButton.setText("หยุด Auto Sensor");
            showStatus("กำลังจำลอง Sensor ทุก 2 วินาที");
        }
    }

    private void simulateInBackground() {
        if (simulationRunning || service.getMachines().isEmpty()) {
            return;
        }

        List<String> machineIds = service.getMachines().stream().map(Machine::getId).toList();
        SensorSimulationTask task = new SensorSimulationTask(machineIds);
        simulationRunning = true;
        statusMessage.setText("กำลังอ่านข้อมูล Sensor...");

        task.setOnSucceeded(event -> {
            for (SensorUpdate update : task.getValue()) {
                service.updateSensor(update.machineId(), update.temperature(), update.vibration());
            }
            simulationRunning = false;
            refreshDashboard();
            showStatus("อัปเดตข้อมูล Sensor ล่าสุดแล้ว");
        });
        task.setOnFailed(event -> {
            simulationRunning = false;
            showError("จำลอง Sensor ไม่สำเร็จ: " + task.getException().getMessage());
        });

        Thread worker = new Thread(task, "sensor-simulation");
        worker.setDaemon(true);
        worker.start();
    }

    private void refreshDashboard() {
        Machine selected = machineTable.getSelectionModel().getSelectedItem();
        String selectedId = selected == null ? null : selected.getId();

        machineItems.setAll(service.getMachines());
        applyFilters();
        machineTable.refresh();
        totalMachines.set(machineItems.size());
        runningMachines.set((int) service.countByStatus(MachineStatus.RUNNING));
        warningMachines.set((int) service.countByStatus(MachineStatus.WARNING));
        emergencyMachines.set((int) service.countByStatus(MachineStatus.EMERGENCY_STOP));
        maintenanceMachines.set((int) service.countRequiringMaintenance());

        if (selectedId != null) {
            service.findById(selectedId).ifPresent(machineTable.getSelectionModel()::select);
        }
    }

    private Machine requireSelectedMachine() {
        Machine selected = machineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            throw new IllegalArgumentException("กรุณาเลือกเครื่องจักรในตารางก่อน");
        }
        return selected;
    }

    private static String requireText(TextField field, String message) {
        String text = field.getText() == null ? "" : field.getText().trim();
        if (text.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return text;
    }

    private static double parseNumber(TextField field, String fieldName) {
        try {
            return Double.parseDouble(requireText(field, "กรุณากรอก" + fieldName));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + "ต้องเป็นตัวเลข");
        }
    }

    private void fillSensorFields(Machine machine) {
        if (machine == null) {
            temperatureField.clear();
            vibrationField.clear();
            return;
        }
        SensorReading reading = machine.getLatestReading();
        temperatureField.setText(format(reading.getTemperature()));
        vibrationField.setText(format(reading.getVibration()));
    }

    private void handleMachineSelection(Machine machine) {
        fillSensorFields(machine);
        if (machine == null) {
            resetMachineForm();
            return;
        }

        idField.setText(machine.getId());
        nameField.setText(machine.getName());
        locationField.setText(machine.getLocation());
        idField.setEditable(false);
        formModeLabel.setText("กำลังแก้ไข " + machine.getId());
        addMachineButton.setDisable(true);
        editMachineButton.setDisable(false);
        cancelEditButton.setDisable(false);
    }

    private void resetMachineForm() {
        idField.clear();
        nameField.clear();
        locationField.clear();
        idField.setEditable(true);
        formModeLabel.setText("จัดการข้อมูล");
        addMachineButton.setDisable(false);
        editMachineButton.setDisable(true);
        cancelEditButton.setDisable(true);
    }

    private void runUiAction(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("ข้อมูลไม่ถูกต้อง");
        alert.showAndWait();
    }

    private void showStatus(String message) {
        statusMessage.setText(message);
    }

    private void showStatusWithMaintenanceCount(String message) {
        statusMessage.setText(
                message + " | ต้องบำรุงทั้งหมด " + maintenanceMachines.get() + " เครื่อง"
        );
    }

    private static String format(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private static String statusStyle(MachineStatus status) {
        return switch (status) {
            case RUNNING -> "status-running";
            case WARNING -> "status-warning";
            case EMERGENCY_STOP -> "status-emergency";
            case MAINTENANCE -> "status-maintenance";
            case OFFLINE -> "status-offline";
        };
    }

    public void shutdown() {
        if (simulationTimeline != null) {
            simulationTimeline.stop();
        }
    }
}
