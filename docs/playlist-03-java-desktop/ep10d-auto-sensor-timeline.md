# EP 3.10 ตอนที่ 4 — เริ่มและหยุด Auto Sensor ด้วย Timeline

เป้าหมาย: เรียกงาน Sensor เดิมประมาณทุก 2 วินาที และกดเริ่ม–หยุดได้ โดยไม่สร้างงานซ้อน

ใช้โปรเจกต์จาก [ตอนที่ 3B](ep10c2-sensor-task-safety.md) ต่อ โดยคืนโค้ดทดลองล้มเหลวและนำการรอ 3 วินาทีออกแล้ว แก้ `DashboardApp.java` และ `SensorSimulationTask.java` ใน `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop` โดยเริ่มจากหน้า Dashboard ก่อน

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

เพิ่ม Field ต่อจาก `sensorBusy` นอกทุก Method:

```java
private Timeline sensorTimeline;
private final Button autoSensorButton = new Button("เริ่ม Auto Sensor");
```

ใน `start()` เพิ่มหลัง `stage.show();` ก่อนปีกกาปิด Method:

```java
sensorTimeline = new Timeline(
        new KeyFrame(Duration.seconds(2), event -> simulateInBackground())
);
sensorTimeline.setCycleCount(Timeline.INDEFINITE);
```

ตอนนี้ยังไม่เรียก `play()` โปรแกรมจึงรอให้ผู้ใช้กดเริ่มก่อน ส่วน `sensorBusy` ใน Method เดิมป้องกันทั้งงานจากปุ่มและงานจาก Timer ซ้อนกันอยู่แล้ว

## 2. เพิ่มปุ่มเริ่ม–หยุด

เพิ่ม Method หลังปีกกาปิดของ `simulateInBackground()` และก่อน `finishSensorTask()` ให้ทั้งสาม Method อยู่ระดับเดียวกัน:

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

### เก็บ Task ปัจจุบันไว้ขอยกเลิก

ใน `DashboardApp.java` เพิ่ม Field ต่อจาก `sensorBusy` ไม่เพิ่ม Import `Task` ซ้ำ เพราะมีจากตอนที่ 2 แล้ว:

```java
private Task<?> activeSensorTask;
```

ใน `simulateInBackground()` เพิ่มหลัง `task.setOnFailed(...);` และก่อน `Thread worker = ...`:

```java
task.setOnCancelled(event -> finishSensorTask());
activeSensorTask = task;
```

`setOnCancelled` คืนสถานะเมื่อถูกยกเลิก เป็นอีกกรณีแยกจากสำเร็จและล้มเหลว ส่วน `<?>` หมายถึงไม่เจาะจงชนิดผลลัพธ์ของ Task ใน Field นี้

ใน `finishSensorTask()` เพิ่มท้าย Method หลัง `sensorButton.setDisable(false);`:

```java
activeSensorTask = null;
```

### หยุด Timer และขอยกเลิกงาน

ใน `start()` เพิ่มหลัง `sensorTimeline.setCycleCount(Timeline.INDEFINITE);` ก่อนปีกกาปิด Method:

```java
stage.setOnHidden(event -> {
    sensorTimeline.stop();
    if (activeSensorTask != null) {
        activeSensorTask.cancel();
    }
});
```

`stop()` หยุดรอบใหม่ ส่วน `cancel()` ขอยกเลิก Task ที่กำลังทำ ไม่ได้บังคับฆ่า Thread

ให้ Task ตรวจคำขอยกเลิกด้วย: ใน `SensorSimulationTask.java` เพิ่มเป็นบรรทัดแรกภายใน Loop `for (String id : machineIds)` ก่อนสุ่มอุณหภูมิ:

```java
if (isCancelled()) {
    break;
}
```

เมื่อถูกยกเลิกจะไม่เริ่มคำนวณเครื่องถัดไป และ Task ที่ถูกยกเลิกจะไม่ส่งผลผ่าน `setOnSucceeded`

### ไม่ให้ Worker เป็นเหตุให้โปรแกรมอยู่ต่อ

กลับมา `DashboardApp.java` ใน `simulateInBackground()` เพิ่มระหว่าง `new Thread(...)` กับ `worker.start();`:

```java
worker.setDaemon(true);
```

ปิดหน้าต่างไม่ได้แปลว่า Thread ทุกตัวจบแล้ว การตั้งเป็น Daemon ทำให้ Worker นี้ไม่รั้ง JVM เมื่อไม่มี Thread แบบ non-daemon เหลืออยู่ แต่ไม่ใช่การยกเลิกงานแทน `cancel()`

## 4. รันและตรวจผล

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

บันทึกทั้งสองไฟล์ รันจากโฟลเดอร์หลักของ Repository แล้วทดลอง:

- เปิดโปรแกรม ค่า Sensor ยังไม่เปลี่ยนเอง
- กดเริ่ม Auto Sensor รอประมาณ 2 วินาที ค่าจะเริ่มอัปเดตเป็นรอบ หน้าจอยังเลือกแถวและพิมพ์ได้
- กดหยุด แล้วรอให้งานที่เริ่มไปแล้วจบ ค่าและชั่วโมงต้องหยุดเปลี่ยน
- ขณะหยุด Auto กดจำลองหนึ่งครั้ง ยังอัปเดตได้หนึ่งรอบ แล้วกดเริ่ม Auto ใหม่ได้
- ปิดหน้าต่าง โปรแกรมหยุด Timer และขอยกเลิก Task ที่ค้างอยู่

ถ้าจะทดสอบบำรุงรักษา ให้หยุด Auto และรอรอบที่ค้างจบก่อน มิฉะนั้นค่า Sensor รอบใหม่อาจเปลี่ยนสถานะ OFFLINE ทันที ชั่วโมงที่เพิ่มยังเป็นค่าจำลอง ไม่ใช่เวลาจริง

ถัดไป: [EP 3.11 — FXML และ Controller](ep11-fxml-controller.md)
