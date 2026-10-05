# EP3.11 ตอนที่ 5 — นำ Sensor และ Auto กลับมา

เป้าหมาย: จำลอง Sensor เปิด–หยุด Auto และหยุดงานเมื่อปิดหน้าต่าง

<details>
<summary>กลับมาเรียนต่อและต้องการชุดเริ่มต้น</summary>

ถ้าทำต่อจากตอนก่อนหน้า ใช้งานเดิมได้เลย หากต้องการเริ่มตอนนี้ใหม่ ให้ปิดแอปและบันทึกไฟล์ก่อนรันจากโฟลเดอร์หลัก Repository:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\prepare-lesson.ps1 -Episode 3.11-5 -BackupExisting
```

คำสั่งสำรองโปรเจกต์เดิมทั้งชุดใน `practice/_backups` ก่อนเตรียมจุดเริ่ม ดู[ชุดพร้อมเรียน](../../lesson-resources/ep3-11-steps/README.md)

</details>

ทำใน `practice/smart-factory-dashboard` ปิดแอปก่อนแก้ไฟล์ แล้วใช้คำสั่งนี้จากโฟลเดอร์หลัก Repository เมื่อถึงจุดรัน:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

เพิ่มโค้ดให้ครบขั้นด้านล่าง แล้วรันตรวจผล

<a id="step-1"></a>

## ขั้นที่ 1: นำ Sensor และ Auto กลับมา

### 1. นำงาน Sensor กลับมา

ใน `src/main/java/smartfactory/ui/DashboardController.java` เพิ่ม Import:

```java
import java.util.List;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.util.Duration;
import smartfactory.desktop.SensorSimulationTask;
import smartfactory.desktop.SensorUpdate;
```

เพิ่ม Field หลัง `addButton`:

```java
@FXML private Button sensorButton;
    @FXML private Button autoSensorButton;
    private boolean sensorBusy;
    private Timeline sensorTimeline;
    private Task<?> activeSensorTask;
```

คัดลอกสาม Method จาก DashboardApp เดิมมาไว้หลัง `handleMaintenance()`:

- `simulateInBackground()` เพิ่ม `@FXML` เหนือ Method
- `toggleAutoSensor()` เพิ่ม `@FXML` เหนือ Method
- `finishSensorTask()` คงเดิม

ใช้ `SensorSimulationTask` และ `SensorUpdate` ใน package `smartfactory.desktop` เดิม ไม่สร้างสองคลาสนี้ซ้ำ

เพิ่มก่อน `configureTable();` ใน `initialize()`:

```java
sensorTimeline = new Timeline(
        new KeyFrame(Duration.seconds(2), event -> simulateInBackground())
);
sensorTimeline.setCycleCount(Timeline.INDEFINITE);
```

หลัง `finishSensorTask()` เพิ่ม:

```java
public void stopBackgroundWork() {
        sensorTimeline.stop();
        if (activeSensorTask != null) {
            activeSensorTask.cancel();
        }
    }
```

### 2. เพิ่มปุ่ม

ใน `src/main/resources/smartfactory/ui/dashboard-view.fxml` เพิ่มหลังปุ่มบำรุง ภายใน VBox `actionButtons`:

```xml
<Button fx:id="sensorButton" text="จำลอง Sensor 1 ครั้ง" onAction="#simulateInBackground"/>
<Button fx:id="autoSensorButton" text="เริ่ม Auto Sensor" onAction="#toggleAutoSensor"/>
```

### 3. ต่อการปิดหน้าต่าง

ใน `src/main/java/smartfactory/ui/DesktopApp.java` เพิ่มก่อน `stage.show();`:

```java
DashboardController controller = loader.getController();
stage.setOnHidden(event -> controller.stopBackgroundWork());
```

DesktopApp รู้ว่าเมื่อไรหน้าต่างปิด ส่วน Controller รู้ว่าจะหยุดงานอะไร

**ก่อนรัน:** ปุ่มหยุด Auto หยุดการนัดรอบใหม่ หรือยกเลิก Task ที่กำลังทำ?

รันด้วยคำสั่งด้านบน แล้วกดจำลองหนึ่งครั้ง แล้วเริ่ม Auto รอ 2–3 รอบ กดหยุด และปิดหน้าต่าง

<details>
<summary>รันแล้วค่อยเปิดตรวจผล</summary>

จำลองหนึ่งครั้งทำให้ชั่วโมงเพิ่ม 1; Auto เปลี่ยนค่าทุกประมาณ 2 วินาที; หยุด Auto ไม่นัดรอบใหม่ แต่งานที่เริ่มไปแล้วอาจส่งผลครั้งสุดท้าย; ปิดหน้าต่างหยุด Timeline และขอยกเลิก Task ที่ยังทำอยู่

</details>


### ลองทำเอง

เปลี่ยน Auto เป็นทุก 5 วินาทีแล้วลองรัน

<details>
<summary>เฉลย</summary>

เปลี่ยน `Duration.seconds(2)` เป็น `Duration.seconds(5)` ใน `initialize()` ทดลองแล้วคืนเป็น 2 ก่อนบทถัดไป

</details>

<details>
<summary>เทียบโค้ดเมื่อจบตอน</summary>

[ชุดจบตอนที่ 5](../../lesson-resources/ep3-11-steps/07-sensor/) เป็นผลหลังทำครบตอน ไม่ต้องคัดลอกทั้งชุดถ้าทำต่อเนื่อง

</details>

[ก่อนหน้า](ep11d-form-crud.md) · [สารบัญ EP3.11](ep11-fxml-controller.md) · [ถัดไป](ep12-thai-package-iot.md)
