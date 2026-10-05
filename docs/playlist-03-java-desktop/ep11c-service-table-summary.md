# EP3.11 ตอนที่ 3 — แสดงตารางและ Summary จาก Service

เป้าหมาย: แสดงเครื่องจักร 3 แถว และยอดสรุปตรงกับข้อมูล

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากตอนก่อนหน้า ใช้งานเดิมได้เลย หากต้องการเริ่มตอนนี้ใหม่ ให้ปิดแอปและบันทึกไฟล์ก่อนรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-3 -BackupExisting
```

คำสั่งสำรองโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมจุดเริ่ม ดู[ชุดพร้อมเรียน](../../lesson-resources/ep3-11-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ แล้วใช้คำสั่งนี้จากโฟลเดอร์หลัก Repository เมื่อถึงจุดรัน:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

ตอนนี้มี 2 จุดรัน: ทำขั้นที่ 1 ให้เห็นผลก่อน แล้วค่อยเพิ่มขั้นที่ 2

<a id="step-1"></a>

## ขั้นที่ 1: แสดงตารางจาก Service

### 1. วางพื้นที่ตาราง

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` แทนที่เนื้อหาภายใน BorderPane หลัง `padding` ด้วย:

```xml
<top><Label text="Smart Factory Dashboard" styleClass="header-title"/></top>
    <center>
        <SplitPane dividerPositions="0.68">
            <VBox spacing="10" styleClass="content-area">
                <TableView fx:id="machineTable" VBox.vgrow="ALWAYS"/>
            </VBox>
        </SplitPane>
    </center>
    <bottom><Label fx:id="statusLabel" text="พร้อมใช้งาน" styleClass="status-bar"/></bottom>
```

นำปุ่มทดสอบออก เปลี่ยนเป็นตารางจริง ด้านขวาของ SplitPane จะเพิ่ม Form ในตอนที่ 4

### 2. เตรียมข้อมูลใน Controller

ใน `src/main/java/smartfactory/ui/DashboardController.java` ลบ `handleHello()` ใช้ Import ชุดนี้แทน Import เดิม:

```java
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
```

แทนที่ Field เดิมด้วย:

```java
@FXML private Label statusLabel;
    @FXML private TableView<Machine> machineTable;
    private final SmartFactoryService service = SmartFactoryService.createWithSampleData();
    private final ObservableList<Machine> machines = FXCollections.observableArrayList();
```

ใช้ Service และ ObservableList แบบเดิม ต่างเพียง TableView มาจาก FXML

### 3. นำวิธีสร้างคอลัมน์เดิมมาใช้

เปิด `src/main/java/smartfactory/desktop/DashboardApp.java` จาก EP3.10 คัดลอกเฉพาะสอง Method ไปไว้หลัง Field ใน Controller:

- `buildMachineTable()`: เปลี่ยนหัว Method เป็น `private void configureTable()` และลบ `return machineTable;` ท้าย Method
- `format(double value)`: คงเดิม

คอลัมน์และ CellFactory ข้างในคงเดิม ไม่คัดลอก `start()` หรือ Field ของ DashboardApp มาซ้ำ

<details>
<summary>ดูสอง Method หลังปรับ</summary>

```java
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
```

```java
private static String format(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }
```

</details>

หลัง `format()` เพิ่ม:

```java
private void refreshDashboard() {
        machines.setAll(service.getMachines());
        machineTable.refresh();
    }
```

หลัง Field ก่อน `configureTable()` เพิ่ม:

```java
@FXML
    private void initialize() {
        configureTable();
        refreshDashboard();
    }
```

`initialize()` ถูกเรียกหลังตัวโหลดเชื่อม Control ให้แล้ว จึงใช้ `machineTable` ได้ที่นี่

**ก่อนรัน:** ข้อมูลสามแถวมาจาก FXML หรือ Service?

รันด้วยคำสั่งด้านบน แล้วดูตาราง

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

เห็น M-001, M-002, M-003 และ 8 คอลัมน์ ชั่วโมงเริ่มต้น 121, 481, 521 ตามลำดับ

</details>


### ลองทำเอง

ถ้าเปลี่ยนชื่อ Label หัวหน้าใน FXML ชื่อเครื่องจักรเปลี่ยนตามไหม?

<details>
<summary>เฉลย</summary>

ไม่เปลี่ยน เพราะชื่อเครื่องจักรมาจาก Model ผ่าน Service ไม่ได้อยู่ใน Label หัวหน้า

</details>

<details>
<summary>เทียบโค้ดที่จุดรันนี้</summary>

[ชุดจบขั้นที่ 1](../../lesson-resources/ep3-11-steps/03-table/) ใช้ตรวจหลังรัน ก่อนทำขั้นที่ 2

</details>

---

<a id="step-2"></a>

## ขั้นที่ 2: เพิ่ม Summary

### 1. วาง Summary

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` แทนที่บล็อก `top`:

```xml
<top>
        <VBox spacing="12">
            <Label text="Smart Factory Dashboard" styleClass="header-title"/>
            <FlowPane hgap="12" vgap="8">
                <Label fx:id="totalLabel" styleClass="summary-card"/>
                <Label fx:id="normalLabel" styleClass="summary-card"/>
                <Label fx:id="warningLabel" styleClass="summary-card"/>
                <Label fx:id="emergencyLabel" styleClass="summary-card"/>
                <Label fx:id="maintenanceLabel" styleClass="summary-card"/>
            </FlowPane>
        </VBox>
    </top>
```

FlowPane จัดการ์ดต่อกันและขึ้นบรรทัดใหม่ได้เมื่อพื้นที่ไม่พอ

### 2. ต่อข้อมูลเดิม

ใน `src/main/java/smartfactory/ui/DashboardController.java` เพิ่ม Import:

```java
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import smartfactory.model.MachineStatus;
```

เพิ่ม Field หลัง `machines`:

```java
@FXML private Label totalLabel;
    @FXML private Label normalLabel;
    @FXML private Label warningLabel;
    @FXML private Label emergencyLabel;
    @FXML private Label maintenanceLabel;
    private final IntegerProperty machineCount = new SimpleIntegerProperty(0);
```

เพิ่มบรรทัดนี้ต้น `initialize()` ก่อน `configureTable();`:

```java
totalLabel.textProperty().bind(machineCount.asString("ทั้งหมด: %d"));
```

แทนที่ `refreshDashboard()` ด้วย Method ชื่อเดียวกันจาก DashboardApp เดิม:

<details>
<summary>ดู Method ที่นำมาใช้</summary>

```java
private void refreshDashboard() {
        machines.setAll(service.getMachines());
        machineTable.refresh();
        machineCount.set(machines.size());
        normalLabel.setText("สถานะปกติ: " + service.countByStatus(MachineStatus.RUNNING));
        warningLabel.setText("Sensor ผิดปกติ: " + service.countByStatus(MachineStatus.WARNING));
        emergencyLabel.setText("หยุดฉุกเฉิน: " + service.countByStatus(MachineStatus.EMERGENCY_STOP));
        maintenanceLabel.setText("ต้องบำรุงทั้งหมด: " + service.countRequiringMaintenance());
    }
```

</details>

Binding คำนวณยอดทั้งหมดจาก `machineCount` ส่วนยอดอื่นอ่านจาก Service

### 3. ใช้ CSS เดิม

คัดลอกไฟล์ `src/main/resources/smartfactory/desktop/dashboard.css` ไปเป็น `src/main/resources/smartfactory/ui/smart-factory.css`

ใน `src/main/java/smartfactory/ui/DesktopApp.java` เพิ่มหลังสร้าง Scene ก่อน `stage.setTitle(...)`:

```java
scene.getStylesheets().add(getClass().getResource("smart-factory.css").toExternalForm());
```


**ก่อนรัน:** ทำไมต้องบำรุง 2 แต่ Sensor ผิดปกติ 1?

รันด้วยคำสั่งด้านบน แล้วดูยอดและแถว M-003

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ทั้งหมด 3, สถานะปกติ 2, Sensor ผิดปกติ 1, หยุดฉุกเฉิน 0, ต้องบำรุงทั้งหมด 2; M-003 ครบชั่วโมงบำรุงแม้ Sensor ยังปกติ

</details>


### ลองทำเอง

เพิ่มหน่วย “เครื่อง” หลังยอดทั้งหมด

<details>
<summary>เฉลย</summary>

```java
totalLabel.textProperty().bind(machineCount.asString("ทั้งหมด: %d เครื่อง"));
```

</details>

<details>
<summary>เทียบโค้ดเมื่อจบตอน</summary>

[ชุดจบตอนที่ 3](../../lesson-resources/ep3-11-steps/04-summary/) เป็นผลหลังทำครบตอน ไม่ต้องคัดลอกทั้งชุดถ้าทำต่อเนื่อง

</details>

[ก่อนหน้า](ep11b-fxml-controller.md) · [สารบัญ EP3.11](ep11-fxml-controller.md) · [ถัดไป](ep11d-form-crud.md)
