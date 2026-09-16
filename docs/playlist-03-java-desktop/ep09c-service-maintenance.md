# EP 3.9 ตอนที่ 4 — ชั่วโมงและการบำรุงรักษา

เป้าหมาย: แสดงชั่วโมงและบันทึกว่าบำรุงเสร็จ พร้อมอัปเดตตารางและจำนวนสรุป

ใช้โปรเจกต์ที่จบ [ตอนที่ 3](ep09b-service-add-delete.md) ต่อ แก้ไฟล์ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/DashboardApp.java`

## 1. เพิ่มสองคอลัมน์

เพิ่ม Import ต่อจาก Import เดิม:

```java
import javafx.beans.property.ReadOnlyObjectWrapper;
```

ใน `buildMachineTable()` หลังจบ `statusColumn.setCellFactory(...);` และก่อน `machineTable.getColumns()...` เพิ่ม:

```java
TableColumn<Machine, Integer> hoursColumn = new TableColumn<>("ชั่วโมง");
hoursColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getOperatingHours()));

TableColumn<Machine, String> maintenanceColumn = new TableColumn<>("บำรุงรักษา");
maintenanceColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().requiresMaintenance() ? "ต้องบำรุง" : "ปกติ"));
```

แทนที่บรรทัด `machineTable.getColumns().addAll(...);` เดิม:

```java
machineTable.getColumns().setAll(idColumn, nameColumn, locationColumn, statusColumn, hoursColumn, maintenanceColumn);
```

`requiresMaintenance()` ตรวจจาก Model: สถานะ WARNING / EMERGENCY_STOP หรือชั่วโมงตั้งแต่ 500 จึงไม่ต้องเขียนเงื่อนไขซ้ำในหน้าจอ

## 2. เพิ่ม Summary การบำรุงรักษา

เพิ่ม Field ต่อจาก `emergencyLabel`:

```java
private final Label maintenanceLabel = new Label("ต้องบำรุงทั้งหมด: 0");
```

ใน `buildTopArea()` แทนที่บรรทัด `HBox summary = ...;` เดิมด้วย:

```java
maintenanceLabel.getStyleClass().add("summary-card");
HBox summary = new HBox(12, totalLabel, normalLabel, warningLabel, emergencyLabel, maintenanceLabel);
```

ใน `refreshDashboard()` เพิ่มหลัง `machines.setAll(service.getMachines());`:

```java
machineTable.refresh();
```

บรรทัดนี้ให้ Cell อ่านค่าใหม่ เพราะเราแก้ข้อมูลภายใน `Machine` ตัวเดิมที่ไม่ได้ใช้ JavaFX Property

เพิ่มท้าย `refreshDashboard()` ก่อนปีกกาปิด Method:

```java
maintenanceLabel.setText("ต้องบำรุงทั้งหมด: " + service.countRequiringMaintenance());
```

## 3. เพิ่มปุ่มบำรุงเสร็จ

วาง Method นี้หลังปีกกาปิดของ `handleDeleteMachine()` ก่อน `showError(...)`:

```java
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
```

ใน `buildMachineForm()` เพิ่มหลัง `form.add(actionButtons, 1, 3);`:

```java
Button maintenanceButton = new Button("บำรุงเสร็จแล้ว");
maintenanceButton.setOnAction(event -> handleMaintenance());
actionButtons.getChildren().add(maintenanceButton);
```

ตัวอย่างนี้บันทึกชั่วโมงเป็น 0 และสถานะเป็น OFFLINE ไม่ได้สั่งซ่อมหรือหยุดเครื่องจักรจริง

## 4. รันและตรวจผล

บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เริ่มใหม่โดยยังไม่เพิ่มหรือลบข้อมูล:

| ทดลอง | ผลที่ต้องเห็น |
| --- | --- |
| เปิดโปรแกรม | ต้องบำรุง 2 เครื่อง: M-002 เพราะ Sensor ผิดปกติ และ M-003 เพราะครบ 521 ชั่วโมง |
| กดบำรุงเสร็จโดยไม่เลือกแถว | Alert ให้เลือกเครื่องจักร |
| บำรุง M-002 | ชั่วโมง 0 · ปิดเครื่อง · บำรุงรักษาปกติ · ต้องบำรุงเหลือ 1 |
| บำรุง M-003 ต่อ | ชั่วโมง 0 · ปิดเครื่อง · บำรุงรักษาปกติ · ต้องบำรุงเหลือ 0 |

หลังทำครบ ทั้งหมดยังเป็น 3 แต่สถานะปกติเหลือ 1 เพราะอีกสองเครื่องเป็น OFFLINE

<details>
<summary>Challenge — แสดงจำนวนที่ยังต้องบำรุงในแถบล่าง พร้อมเฉลย</summary>

หลังบำรุงเสร็จ ให้แถบล่างแสดงจำนวนเครื่องที่ยังต้องบำรุง

ใน `handleMaintenance()` แทนที่บรรทัด `statusLabel.setText(...);` ด้วย:

```java
long remaining = service.countRequiringMaintenance();
statusLabel.setText("บำรุงรักษา " + selected.getId() + " แล้ว — เหลือเครื่องที่ต้องบำรุงทั้งหมด " + remaining + " เครื่อง");
```

เปิดโปรแกรมใหม่แล้วบำรุง M-002 ต้องเหลือ 1 จากนั้นบำรุง M-003 ต้องเหลือ 0

</details>

[ซอร์สหลังจบตอนนี้](../../lesson-resources/ep3-9-steps/03-maintenance/) · [ต่อ EP3.10 ตอนที่ 1 — กดปุ่มจำลอง Sensor](ep10a-sensor-button.md)
