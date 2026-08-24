# EP 3.11 — แยก View ด้วย FXML และ Controller

## สิ่งที่จะทำ

ย้ายโครงสร้างหน้าจอออกจาก Java ให้ `FXML` ดูแล View และให้ `DashboardController` ประสาน Event กับ Service

```mermaid
flowchart LR
    F[dashboard-view.fxml] -->|fx:id / onAction| C[DashboardController]
    C --> S[SmartFactoryService]
    S --> M[Model]
    CSS[smart-factory.css] --> F
```

## 1. เพิ่ม FXML Dependency

ใน `practice/smart-factory-dashboard/pom.xml` เพิ่มใต้ `javafx-controls`:

```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>${javafx.version}</version>
</dependency>
```

## 2. นำโครงฉบับสมบูรณ์มาแยกชั้น

รันจากโฟลเดอร์หลักของ Repository:

```powershell
$javaTarget = ".\practice\smart-factory-dashboard\src\main\java\smartfactory\ui"
$resourceTarget = ".\practice\smart-factory-dashboard\src\main\resources\smartfactory\ui"
New-Item -ItemType Directory -Force $javaTarget, $resourceTarget
Copy-Item .\src\main\java\smartfactory\ui\*.java $javaTarget -Force
Copy-Item .\src\main\resources\smartfactory\ui\* $resourceTarget -Force
```

เปลี่ยน `mainClass` ใน `pom.xml` เป็น:

```xml
<mainClass>smartfactory.ui.DesktopApp</mainClass>
```

ไฟล์สำคัญที่ได้:

- `dashboard-view.fxml` — โครงสร้าง Component, `fx:id` และ Event
- `DashboardController.java` — รับ Event, Validation, Binding และ Refresh
- `DesktopApp.java` — โหลด FXML และสร้าง Stage
- `smart-factory.css` — Theme ของหน้าจอ

## 3. ดูจุดเชื่อม FXML กับ Controller

ใน FXML:

```xml
<Button text="เพิ่มเครื่องจักร" onAction="#handleAddMachine"/>
<TableView fx:id="machineTable"/>
```

ใน Controller:

```java
@FXML private TableView<Machine> machineTable;

@FXML
private void handleAddMachine() {
    // อ่าน Form -> เรียก Service -> Refresh View
}
```

ชื่อหลัง `#` ต้องตรงกับชื่อ Method และ `fx:id` ต้องตรงกับ Field ที่มี `@FXML`

## 4. ดูการส่ง Service เข้า Controller

`DesktopApp` ใช้ Controller Factory เพื่อไม่ให้ Controller สร้าง Service เอง:

```java
SmartFactoryService service = SmartFactoryService.createWithSampleData();
loader.setControllerFactory(type -> {
    if (type == DashboardController.class) {
        return new DashboardController(service);
    }
    throw new IllegalArgumentException("Unknown controller: " + type.getName());
});
```

จุดนี้ทำให้ Dependency ชัดและเปลี่ยน Service สำหรับการทดสอบได้ง่ายขึ้น

## 5. ตรวจ Event ที่เปลี่ยนข้อมูล

ทั้งการอัปเดต Sensor และการบำรุงรักษาต้องเรียก `refreshDashboard()` หลัง Service เสมอ:

```java
service.updateSensor(selected.getId(), temperature, vibration);
refreshDashboard();

service.performMaintenance(selected.getId());
refreshDashboard();
```

การ์ด `สถานะปกติ` นับเฉพาะสถานะ `RUNNING` จึงแสดง `2` จาก `M-001` และ `M-003` โดยไม่ได้สื่อว่าเครื่องสถานะ `WARNING` หยุดทำงาน ส่วนการ์ด `Sensor ผิดปกติ` แสดง `1` จาก `M-002` และการ์ด `ต้องบำรุงทั้งหมด` แสดง `2` จาก `M-002` กับ `M-003`

`M-003` แสดงสถานะ `RUNNING` ได้อย่างถูกต้อง เพราะค่า Sensor ปกติ ขณะเดียวกันคอลัมน์บำรุงรักษาแสดง `ต้องบำรุง` เพราะทำงานเกิน 500 ชั่วโมง

## 6. รัน

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

ผลที่ต้องเห็น: Dashboard ฉบับเต็มทำงานเหมือนเดิม แต่ View, Controller, Service และ Model แยกหน้าที่กันแล้ว

ทดลองบำรุง `M-002` แล้วอัปเดต Sensor เป็นอุณหภูมิ `65` และแรงสั่น `3` ตัวเลขต้องยังเป็น `1` จากนั้นบำรุง `M-003` และอัปเดต Sensor เป็นค่าปกติ ตัวเลขจึงเปลี่ยนเป็น `0`

## Challenge

หา `fx:id="statusMessage"` ใน FXML และชี้ให้ได้ว่าเชื่อมกับ Field ใดใน Controller

ถัดไป: [EP 3.12 — ภาษาไทย Runtime Image และ IoT](ep12-thai-package-iot.md)
