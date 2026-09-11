# EP 3.9 ตอนที่ 2 — อ่านข้อมูลจาก Service มาแสดงในตาราง

เป้าหมาย: เปิดหน้าจอแล้วเห็นเครื่องจักรตัวอย่าง 3 เครื่อง พร้อมจำนวนสรุป

ใช้โปรเจกต์ที่จบ [ตอนที่ 1](ep09-preparation.md) ต่อ โดยมี Model และ Service ที่สร้างไว้แล้ว

แก้ไฟล์ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/DashboardApp.java`

## 1. ใช้ Machine แทน MachineRow

เพิ่ม Import ต่อจาก Import เดิม:

```java
import smartfactory.model.Machine;
import smartfactory.model.MachineStatus;
import smartfactory.service.SmartFactoryService;
```

แทนที่ Field `machines` และ `machineTable` เดิมด้วย:

```java
private final SmartFactoryService service = SmartFactoryService.createWithSampleData();
private final ObservableList<Machine> machines = FXCollections.observableArrayList();
private final TableView<Machine> machineTable = new TableView<>();
```

`Machine` คือเครื่องจักรหนึ่งเครื่อง ส่วน `service` เตรียมรายการตัวอย่างไว้ให้หน้าจอ

ใน `buildMachineTable()` เปลี่ยน `MachineRow` เป็น `Machine` ที่ชนิดคืนค่าของ Method และ `TableColumn` ทั้งสี่ตัว เช่น:

```java
private TableView<Machine> buildMachineTable() {
```

```java
TableColumn<Machine, String> idColumn = new TableColumn<>("รหัส");
```

แทนที่ `setCellValueFactory(...)` ของแต่ละคอลัมน์ ณ ตำแหน่งเดิม:

```java
idColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getId()));
nameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
locationColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getLocation()));
statusColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getStatus().getDisplayName()));
```

`getDisplayName()` คืนข้อความสถานะ จึงใช้ `updateItem(String status, boolean empty)` เดิมได้

## 2. อัปเดตตารางและ Summary

ลบ Method `refreshSummary()` และ `countStatus(String status)` ทั้งสองออก แล้ววาง Method นี้แทน ภายใน Class:

```java
private void refreshDashboard() {
    machines.setAll(service.getMachines());
    machineCount.set(machines.size());
    normalLabel.setText("สถานะปกติ: " + service.countByStatus(MachineStatus.RUNNING));
    warningLabel.setText("Sensor ผิดปกติ: " + service.countByStatus(MachineStatus.WARNING));
    emergencyLabel.setText("หยุดฉุกเฉิน: " + service.countByStatus(MachineStatus.EMERGENCY_STOP));
}
```

ใน `start()` เพิ่มหลัง `root.setCenter(content);`:

```java
refreshDashboard();
```

## 3. ให้ปุ่มเพิ่มใช้ Service

ใน `handleAddMachine()` แทนที่เฉพาะสองบรรทัด `machines.add(new MachineRow(...));` และ `refreshSummary();` ด้วย:

```java
service.addMachine(new Machine(id, name, location));
refreshDashboard();
```

ลบ `private record MachineRow(...) {}` ที่ท้าย Class

## 4. เพิ่มสีสถานะปิดเครื่อง

ใน `updateItem(...)` ของ `statusColumn` แทนที่บรรทัด `removeAll(...)` เดิม:

```java
getStyleClass().removeAll("status-running", "status-warning", "status-emergency", "status-offline");
```

ใน `switch (status)` เพิ่มก่อน `default`:

```java
case "ปิดเครื่อง" -> "status-offline";
```

ต่อท้าย `practice/smart-factory-dashboard/src/main/resources/smartfactory/desktop/dashboard.css`:

```css
.status-offline { -fx-text-fill: #94a3b8; -fx-font-weight: bold; }
```

## 5. รันและตรวจผล

บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

- ตารางมี 3 แถว: M-001 และ M-003 สีเขียว ส่วน M-002 เป็น Sensor ผิดปกติสีส้ม
- Summary: ทั้งหมด 3 · สถานะปกติ 2 · Sensor ผิดปกติ 1 · หยุดฉุกเฉิน 0

[ซอร์สหลังจบตอนนี้](../../lesson-resources/ep3-9-steps/01-read/) · [ต่อ ตอนที่ 3 — เพิ่มและลบเครื่องจักร](ep09b-service-add-delete.md)
