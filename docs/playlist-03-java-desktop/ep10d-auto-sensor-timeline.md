# EP 3.10 ตอนที่ 4 — เริ่มและหยุด Auto Sensor ด้วย Timeline

เป้าหมาย: เรียกงาน Sensor เดิมประมาณทุก 2 วินาที และกดเริ่ม–หยุดได้ โดยไม่สร้างงานซ้อน

ใช้โปรเจกต์จาก [ตอนที่ 3](ep10c-sensor-task-result.md) ต่อ แก้ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/DashboardApp.java`

```mermaid
sequenceDiagram
    participant FX as JavaFX Application Thread
    participant BG as Background Thread
    Note over FX: Timeline ถึงรอบ
    FX->>FX: ตรวจ sensorBusy ถ้างานเก่ายังไม่จบให้ข้ามรอบ
    FX->>BG: ถ้าว่าง สร้าง Task ใหม่แล้ว start
    BG-->>FX: ผล Sensor → onSucceeded
    FX->>FX: อัปเดต Service และ refreshDashboard
```

## 1. เตรียมตัวตั้งเวลา

เพิ่ม Import ต่อจาก Import เดิม:

```java
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
```

เพิ่ม Field ต่อจาก Field เดิม:

```java
private Timeline sensorTimeline;
private final Button autoSensorButton = new Button("เริ่ม Auto Sensor");
```

ใน `start()` หลัง `stage.show();` และก่อน `stage.setOnHidden(...)` เพิ่ม:

```java
sensorTimeline = new Timeline(
        new KeyFrame(Duration.seconds(2), event -> simulateInBackground())
);
sensorTimeline.setCycleCount(Timeline.INDEFINITE);
```

ตอนนี้ยังไม่เรียก `play()` โปรแกรมจึงรอให้ผู้ใช้กดเริ่มก่อน ส่วน `sensorBusy` ใน Method เดิมป้องกันทั้งงานจากปุ่มและงานจาก Timer ซ้อนกันอยู่แล้ว

## 2. เพิ่มปุ่มเริ่ม–หยุด

เพิ่ม Method หลังปีกกาปิดของ `simulateInBackground()`:

```java
private void toggleAutoSensor() {
    if (sensorTimeline.getStatus() == Animation.Status.RUNNING) {
        sensorTimeline.stop();
        autoSensorButton.setText("เริ่ม Auto Sensor");
        statusLabel.setText("หยุด Auto Sensor แล้ว");
    } else {
        sensorTimeline.play();
        autoSensorButton.setText("หยุด Auto Sensor");
        statusLabel.setText("เริ่ม Auto Sensor แล้ว");
    }
}
```

ใน `buildMachineForm()` เพิ่มหลัง `actionButtons.getChildren().add(sensorButton);`:

```java
autoSensorButton.setOnAction(event -> toggleAutoSensor());
actionButtons.getChildren().add(autoSensorButton);
```

`Timeline` แค่เรียก Method ตามเวลาบน Thread ของหน้าจอ งาน Sensor ยังทำบน Background Thread ส่วน `stop()` หยุดรอบใหม่ งานที่เริ่มไปแล้วอาจส่งผลกลับมาอีกหนึ่งรอบ

## 3. หยุดงานเมื่อปิดหน้าต่าง

ใน `start()` แทนที่ `stage.setOnHidden(...)` เดิมทั้งชุดด้วย:

```java
stage.setOnHidden(event -> {
    sensorTimeline.stop();
    if (activeSensorTask != null) {
        activeSensorTask.cancel();
    }
});
```

อย่าเพิ่ม `setOnHidden` ซ้ำอีกชุด เพราะจะทับ Handler เดิม

## 4. รันและตรวจผล

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

รันจากโฟลเดอร์หลักของ Repository แล้วทดลอง:

- เปิดโปรแกรม ค่า Sensor ยังไม่เปลี่ยนเอง
- กดเริ่ม Auto Sensor รอประมาณ 2 วินาที ค่าจะเริ่มอัปเดตเป็นรอบ หน้าจอยังเลือกแถวและพิมพ์ได้
- กดหยุด แล้วรอให้งานที่เริ่มไปแล้วจบ ค่าและชั่วโมงต้องหยุดเปลี่ยน
- ขณะหยุด Auto กดจำลองหนึ่งครั้ง ยังอัปเดตได้หนึ่งรอบ แล้วกดเริ่ม Auto ใหม่ได้
- ปิดหน้าต่าง โปรแกรมหยุด Timer และขอยกเลิก Task ที่ค้างอยู่

ถ้าจะทดสอบบำรุงรักษา ให้หยุด Auto และรอรอบที่ค้างจบก่อน มิฉะนั้นค่า Sensor รอบใหม่อาจเปลี่ยนสถานะ OFFLINE ทันที ชั่วโมงที่เพิ่มยังเป็นค่าจำลอง ไม่ใช่เวลาจริง

ถัดไป: [EP 3.11 — FXML และ Controller](ep11-fxml-controller.md)
