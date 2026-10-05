# EP3.11 — ขั้นที่ 4: นำ Summary กลับมา

เป้าหมาย: ยอดทั้งหมด 3 และต้องบำรุง 2

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากบทก่อนหน้า ใช้งานเดิมได้เลย ไม่ต้องเตรียมใหม่

หากต้องการกลับจุดเริ่ม ให้ปิดแอป บันทึกไฟล์ แล้วรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-4 -BackupExisting
```

คำสั่งเก็บโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมชุดใหม่ ดูที่มาใน[ชุดพร้อมเรียน](../../../lesson-resources/ep3-11-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ รันจากโฟลเดอร์หลัก Repository หลังเพิ่มโค้ดครบขั้น:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

## 1. วาง Summary

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

## 2. ต่อข้อมูลเดิม

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

## 3. ใช้ CSS เดิม

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


## ลองทำเอง

เพิ่มหน่วย “เครื่อง” หลังยอดทั้งหมด

<details>
<summary>เฉลย</summary>

```java
totalLabel.textProperty().bind(machineCount.asString("ทั้งหมด: %d เครื่อง"));
```

</details>



<details>
<summary>ดูโค้ดครบขั้นเพื่อเทียบ</summary>

[ชุดจบขั้นที่ 4](../../../lesson-resources/ep3-11-steps/04-summary/) เป็นผลจากขั้นนี้ ไม่ต้องคัดลอกทั้งชุดถ้าทำต่อเนื่อง

</details>

[ก่อนหน้า](03-table.md) · [สารบัญ EP3.11](../ep11-fxml-controller.md) · [ถัดไป](05-add.md)
