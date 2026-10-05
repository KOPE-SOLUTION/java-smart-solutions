# EP3.14 — ขั้นที่ 2: กรองการบำรุงรักษา

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.14-2 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-12-16-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เปิดแอปใหม่ ยังไม่เปิด Auto และล้างตัวกรองก่อนทดสอบข้อมูลเริ่มต้น เว้นแต่ขั้นตอนระบุอย่างอื่น

## เพิ่มเงื่อนไขบำรุง

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` เพิ่มหลัง statusFilter:

```xml
<ComboBox fx:id="maintenanceFilter" promptText="บำรุงรักษา"/>
```

ใน `src/main/java/smartfactory/ui/DashboardController.java` เพิ่ม Field หลัง statusFilter:

```java
@FXML private ComboBox<String> maintenanceFilter;
```

เพิ่มหลัง `configureStatusFilter()`:

```java
private void configureMaintenanceFilter() {
        maintenanceFilter.getItems().setAll("ทุกเครื่อง", "ต้องบำรุง", "ยังไม่ต้องบำรุง");
        maintenanceFilter.setValue("ทุกเครื่อง");
        maintenanceFilter.valueProperty().addListener((observable, oldValue, newValue) -> applySearch());
    }
```

เพิ่ม `configureMaintenanceFilter();` หลัง `configureStatusFilter();` ใน `initialize()`

ใน `applySearch()` แทนที่บรรทัด `return matchesText && matchesStatus;` ด้วย:

```java
boolean matchesMaintenance = switch (maintenanceFilter.getValue()) {
                case "ต้องบำรุง" -> machine.requiresMaintenance();
                case "ยังไม่ต้องบำรุง" -> !machine.requiresMaintenance();
                default -> true;
            };
            return matchesText && matchesStatus && matchesMaintenance;
```

ใช้ `requiresMaintenance()` จาก Model ไม่สร้างเกณฑ์ 500 ชั่วโมงซ้ำในหน้าจอ

**ก่อนรัน:** M-003 ไม่มี Sensor ผิดปกติ แต่ผ่าน “ต้องบำรุง” หรือไม่?

รันด้วยคำสั่งด้านบน แล้วเลือกทุกสถานะและต้องบำรุง แล้วลองค้นหา utility

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

ต้องบำรุงพบ M-002 กับ M-003; เพิ่ม utility เหลือ M-003

</details>


## ลองทำเอง

เพิ่มทางเลือก “ครบ 500 ชั่วโมง” โดยแยกจาก “ต้องบำรุง”

<details>
<summary>เฉลย</summary>

```java
case "ครบ 500 ชั่วโมง" -> machine.getOperatingHours() >= Machine.MAINTENANCE_HOURS;
```
เพิ่มข้อความเดียวกันในรายการ ComboBox และ Case นี้ใน switch ทดลองแล้วนำออกก่อนขั้นถัดไป

</details>



<details>
<summary>เทียบโค้ดครบขั้น</summary>

[ชุดจบขั้นนี้](../../../lesson-resources/ep3-12-16-steps/14b-maintenance/)

</details>

[สารบัญ](../ep14-multi-filter-sort.md) · [ถัดไป](03-clear.md)
