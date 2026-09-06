# EP 3.9 ตอนที่ 1 — อ่านข้อมูลจาก Service มาแสดงในตาราง

## สิ่งที่จะทำ

เปิดหน้าต่างแล้วเห็นข้อมูลเครื่องจักรตัวอย่าง 3 เครื่อง พร้อม Summary ที่อ่านจำนวนจาก `SmartFactoryService`

## ก่อนเริ่ม

ใช้หน้าจอที่จบ EP3.8 หรือเลือก [ชุดก่อนเริ่มตอนที่ 1](../../lesson-resources/ep3-9-steps/00-start/) ซึ่งมี `pom.xml`, Java และ CSS ครบแล้ว

หากใช้ชุดไฟล์ ให้คัดลอก **เนื้อหาภายใน** `00-start` ไปไว้ใน `practice/smart-factory-dashboard` ให้ `pom.xml` อยู่ใต้โฟลเดอร์นี้ทันที หากมีงานเดิมให้เปลี่ยนชื่อโฟลเดอร์เดิมเก็บไว้ก่อน ไม่วางทับหรือรวมสองเวอร์ชันเข้าด้วยกัน

รันจากโฟลเดอร์หลักของ Repository ที่มี `mvnw.cmd`:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

ต้องเปิดได้โดยตารางว่างและ Summary ทั้งสี่เป็น 0 ปิดหน้าต่างก่อนเริ่มแก้โค้ด

แก้ Java ที่ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/DashboardApp.java` ทุก Method ที่เพิ่มให้อยู่ภายใน `DashboardApp` ก่อนปีกกาปิด Class ไม่วางซ้อนใน `start()` หรือ Method อื่น

## 1. เตรียม Model และ Service

หากเริ่มจาก `00-start` มีสองโฟลเดอร์นี้แล้ว ข้ามไปขั้นที่ 2 ได้เลย

หากใช้ไฟล์จาก EP3.8 ให้คัดลอก `model` และ `service` จาก [ชุด OOP Core](../../lesson-resources/ep3-9-oop-core/) ไปวางข้าง `desktop` ที่ `practice/smart-factory-dashboard/src/main/java/smartfactory/` หากมีสองโฟลเดอร์นี้อยู่แล้ว ให้สำรองก่อนใช้ชุดสำหรับ EP นี้

`Machine` แทนเครื่องจักรหนึ่งเครื่อง ส่วน `SmartFactoryService` จัดการรายการเครื่องจักร

## 2. เปลี่ยนชนิดข้อมูลของตาราง

เพิ่ม Import ด้านบน `DashboardApp.java` ต่อจาก Import เดิม:

```java
import smartfactory.model.Machine;
import smartfactory.model.MachineStatus;
import smartfactory.service.SmartFactoryService;
```

แทนที่ Field `machines` และ `machineTable` เดิม พร้อมเพิ่ม `service`:

```java
private final SmartFactoryService service = SmartFactoryService.createWithSampleData();
private final ObservableList<Machine> machines = FXCollections.observableArrayList();
private final TableView<Machine> machineTable = new TableView<>();
```

ใน `buildMachineTable()` เปลี่ยนชนิด `MachineRow` เป็น `Machine` ทั้งบรรทัดประกาศ Method และ `TableColumn` ทั้งสี่ตัว ตัวอย่าง:

```java
private TableView<Machine> buildMachineTable() {
```

```java
TableColumn<Machine, String> idColumn = new TableColumn<>("รหัส");
```

จากนั้นแทนที่ `setCellValueFactory(...)` ของแต่ละคอลัมน์ ณ ตำแหน่งเดิมด้วยบรรทัดที่ตรงกัน:

```java
idColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getId()));
nameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
locationColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getLocation()));
statusColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getStatus().getDisplayName()));
```

`getStatus()` คืน `MachineStatus` ซึ่งเป็น Enum ส่วน `getDisplayName()` คืนข้อความสำหรับแสดง ดังนั้น `statusColumn` ยังใช้ `String` และ `updateItem(String status, boolean empty)` จาก EP3.8 ต่อได้

## 3. ให้อ่านข้อมูลล่าสุดจาก Service

ลบ Method `refreshSummary()` และ `countStatus(String status)` เดิม แล้ววาง Method นี้ในตำแหน่งนั้น:

```java
private void refreshDashboard() {
        machines.setAll(service.getMachines());
        machineCount.set(machines.size());
        normalLabel.setText("สถานะปกติ: " + service.countByStatus(MachineStatus.RUNNING));
        warningLabel.setText("Sensor ผิดปกติ: " + service.countByStatus(MachineStatus.WARNING));
        emergencyLabel.setText("หยุดฉุกเฉิน: " + service.countByStatus(MachineStatus.EMERGENCY_STOP));
    }
```

`machines.setAll(...)` นำรายการล่าสุดมาให้ตาราง ส่วน `machineCount.set(...)` ทำให้ `totalLabel` ที่ Bind ไว้แสดงจำนวนใหม่

ใน `start()` เพิ่มหลัง `root.setCenter(content);`:

```java
refreshDashboard();
```

## 4. ปรับจุดเพิ่มข้อมูลให้ใช้ Model เดียวกัน

ใน `handleAddMachine()` ภายใน `try` แทนที่เฉพาะสองบรรทัด `machines.add(new MachineRow(...));` และ `refreshSummary();` ด้วย:

```java
service.addMachine(new Machine(id, name, location));
refreshDashboard();
```

เก็บการรับ `id`, `name`, `location`, Validation, `catch`, ข้อความหลังเพิ่ม และการล้างช่องกรอกไว้ตามเดิม ปุ่มเพิ่มจึงยังใช้ได้หลังเปลี่ยนชนิดข้อมูล โดยตอนที่ 2 จะทดสอบการเพิ่มและรหัสซ้ำโดยเฉพาะ

ลบ `private record MachineRow(...)` ที่ท้าย Class เพราะตารางและปุ่มเพิ่มใช้ `Machine` แล้ว

## 5. เพิ่มสีสถานะปิดเครื่อง

เครื่องใหม่เริ่มด้วย `OFFLINE` ซึ่งแสดงคำว่า `ปิดเครื่อง` ภายใน `updateItem(...)` ของ `statusColumn` ให้แทนที่บรรทัดล้าง Class เดิมด้วย:

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

<details>
<summary>หากทำ Challenge จาก EP ก่อนหน้าไว้</summary>

- มี Event คลิกแถว: เปลี่ยน `MachineRow selected` เป็น `Machine selected` และ `selected.id()/name()` เป็น `selected.getId()/getName()`
- มีช่องอุณหภูมิ: เก็บช่องและ Validation เดิมไว้ได้ ตอนนี้ยังไม่ได้นำค่านั้นไปอัปเดต Sensor การจำลอง Sensor เริ่มใน EP3.10
- มีสี `หยุดซ่อมบำรุง` อยู่แล้ว: เก็บ Case และ CSS ไว้ พร้อมเก็บ `"status-maintenance"` ในรายการ `removeAll(...)` ด้วย การกดบำรุงเสร็จในตอนที่ 3 จะได้สถานะ `OFFLINE` ไม่ใช่ `MAINTENANCE`

</details>

## 6. รันและตรวจผล

บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

| รหัส | สถานะในตาราง | สี |
| --- | --- | --- |
| M-001 | กำลังทำงาน | เขียว |
| M-002 | Sensor ผิดปกติ | ส้ม |
| M-003 | กำลังทำงาน | เขียว |

Summary ต้องเป็น **ทั้งหมด 3 · สถานะปกติ 2 · Sensor ผิดปกติ 1 · หยุดฉุกเฉิน 0**

ตอนนี้ตารางมี 4 คอลัมน์ ยังไม่มีคอลัมน์ชั่วโมงหรือปุ่มบำรุงรักษา คำว่า `Sensor ผิดปกติ` ไม่ได้หมายความว่าเครื่องหยุดทำงานแล้ว

หากยังพบ Error ที่ `MachineRow` หรือ `refreshSummary` ให้ค้นหาสองชื่อนี้ใน `DashboardApp.java` แล้วตรวจจุดที่ยังไม่ได้เปลี่ยนตามขั้นที่ 2–4

## เปิดผลลัพธ์ของตอนนี้ได้ทันที

[ซอร์สหลังจบตอนนี้](../../lesson-resources/ep3-9-steps/01-read/) เป็นโปรเจกต์ครบชุด หากต้องการดูผลก่อนทำตาม รันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\lesson-resources\ep3-9-steps\01-read\pom.xml javafx:run
```

ชุดนี้มีปุ่มเพิ่มที่เชื่อม Service แล้ว แต่ยังไม่มีปุ่มลบหรือปุ่มบำรุงรักษา

ถัดไป: [EP3.9 ตอนที่ 2 — เพิ่มและลบเครื่องจักร](ep09b-service-add-delete.md) · [สารบัญ EP3.9](ep09-service-crud.md)
