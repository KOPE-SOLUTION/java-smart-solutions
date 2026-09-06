package smartfactory.desktop;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardApp extends Application {
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField locationField = new TextField();
    private final Button addButton = new Button("เพิ่มเครื่องจักร");
    private final Label statusLabel = new Label("พร้อมใช้งาน");
    private final IntegerProperty machineCount = new SimpleIntegerProperty(0);
    private final Label totalLabel = new Label();
    private final Label normalLabel = new Label("สถานะปกติ: 0");
    private final Label warningLabel = new Label("Sensor ผิดปกติ: 0");
    private final Label emergencyLabel = new Label("หยุดฉุกเฉิน: 0");
    private final ObservableList<MachineRow> machines = FXCollections.observableArrayList();
    private final TableView<MachineRow> machineTable = new TableView<>();

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setTop(buildTopArea());

        SplitPane content = new SplitPane(buildMachineTable(), buildMachineForm());
        content.setDividerPositions(0.68);
        root.setCenter(content);

        statusLabel.getStyleClass().add("status-bar");

        root.setBottom(statusLabel);

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());

        stage.setTitle("KOPE SOLUTION - Smart Factory Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private VBox buildTopArea() {
        Label title = new Label("Smart Factory Dashboard");
        title.getStyleClass().add("header-title");

        totalLabel.textProperty().bind(machineCount.asString("ทั้งหมด: %d"));
        totalLabel.getStyleClass().add("summary-card");
        normalLabel.getStyleClass().add("summary-card");
        warningLabel.getStyleClass().add("summary-card");
        emergencyLabel.getStyleClass().add("summary-card");

        HBox summary = new HBox(12, totalLabel, normalLabel, warningLabel, emergencyLabel);

        return new VBox(12, title, summary);
    }

    private GridPane buildMachineForm() {
        idField.setPromptText("เช่น M-001");
        nameField.setPromptText("เช่น Conveyor Motor");
        locationField.setPromptText("เช่น Production Line A");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(24));
        form.addRow(0, new Label("รหัสเครื่องจักร:"), idField);
        form.addRow(1, new Label("ชื่อเครื่องจักร:"), nameField);
        form.addRow(2, new Label("ตำแหน่งเครื่องจักร:"), locationField);
        form.add(addButton, 1, 3);
        addButton.setOnAction(event -> handleAddMachine());

        return form;
    }

    private TableView<MachineRow> buildMachineTable() {
        TableColumn<MachineRow, String> idColumn = new TableColumn<>("รหัส");
        idColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().id()));

        TableColumn<MachineRow, String> nameColumn = new TableColumn<>("ชื่อเครื่องจักร");
        nameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().name()));

        TableColumn<MachineRow, String> locationColumn = new TableColumn<>("ตำแหน่ง");
        locationColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().location()));

        TableColumn<MachineRow, String> statusColumn = new TableColumn<>("สถานะ");
        statusColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().status()));
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                getStyleClass().removeAll("status-running", "status-warning", "status-emergency");

                if (empty || status == null) {
                    setText(null);
                    return;
                }

                setText(status);
                String style = switch (status) {
                    case "Sensor ผิดปกติ" -> "status-warning";
                    case "หยุดฉุกเฉิน" -> "status-emergency";
                    default -> "status-running";
                };
                getStyleClass().add(style);
            }
        });

        machineTable.getColumns().addAll(idColumn, nameColumn, locationColumn, statusColumn);
        machineTable.setItems(machines);


        return machineTable;
    }

    private void refreshSummary() {
        machineCount.set(machines.size());
        normalLabel.setText("สถานะปกติ: " + countStatus("กำลังทำงาน"));
        warningLabel.setText("Sensor ผิดปกติ: " + countStatus("Sensor ผิดปกติ"));
        emergencyLabel.setText("หยุดฉุกเฉิน: " + countStatus("หยุดฉุกเฉิน"));
    }

    private long countStatus(String status) {
        return machines.stream().filter(machine -> machine.status().equals(status)).count();
    }

    private String requireText(TextField field, String message) {
        String value = field.getText().trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private void handleAddMachine() {
        try {
            String id = requireText(idField, "กรุณากรอกรหัสเครื่องจักร");


            String name = requireText(nameField, "กรุณากรอกชื่อเครื่องจักร");
            String location = requireText(locationField, "กรุณากรอกตำแหน่งเครื่องจักร");

            machines.add(new MachineRow(id, name, location, "กำลังทำงาน"));
            refreshSummary();
            statusLabel.setText("เพิ่ม " + id + " - " + name + " เรียบร้อยแล้ว");
            idField.clear();
            nameField.clear();
            locationField.clear();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("ข้อมูลไม่ถูกต้อง");
        alert.showAndWait();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private record MachineRow(String id, String name, String location, String status) {}
}
