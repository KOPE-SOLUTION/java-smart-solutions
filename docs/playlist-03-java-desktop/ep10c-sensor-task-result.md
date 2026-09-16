# EP 3.10 ตอนที่ 3 — ส่งผล Sensor กลับมาอัปเดตตาราง

เป้าหมาย: ใช้ปุ่มเดิมรันงาน Sensor เบื้องหลัง แล้วนำผลกลับมาอัปเดต Service ตาราง และ Summary

ใช้โปรเจกต์ที่จบทั้งสองรอบทดลองของ [ตอนที่ 2](ep10b-task-thread.md) ต่อ สร้างไฟล์ใหม่สองไฟล์ใน `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop`

## 1. สร้างกล่องใส่ผลลัพธ์

สร้าง `SensorUpdate.java` แล้ววาง:

```java
package smartfactory.desktop;

public record SensorUpdate(String machineId, double temperature, double vibration) {}
```

หนึ่ง Object คือผลของหนึ่งเครื่อง เช่น รหัส M-001 อุณหภูมิ 85 และแรงสั่น 3.2 ยังไม่ได้เปลี่ยนสถานะของ Machine

## 2. สร้างงาน Sensor

สร้าง `SensorSimulationTask.java` แล้ววาง เป็นคลาสลูกของ Task แบบมีชื่อเหมือน `DemoTask` แต่เปลี่ยนจากคืนข้อความเป็นคืนรายการผล Sensor:

```java
package smartfactory.desktop;

import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SensorSimulationTask extends Task<List<SensorUpdate>> {
    private final List<String> machineIds;

    public SensorSimulationTask(List<String> machineIds) {
        this.machineIds = List.copyOf(machineIds);
    }

    @Override
    protected List<SensorUpdate> call() {
        List<SensorUpdate> results = new ArrayList<>();
        for (String id : machineIds) {
            if (isCancelled()) {
                break;
            }
            double temperature = ThreadLocalRandom.current().nextDouble(50, 110);
            double vibration = ThreadLocalRandom.current().nextDouble(1, 9);
            results.add(new SensorUpdate(id, temperature, vibration));
        }
        return results;
    }
}
```

`Task<List<SensorUpdate>>` ส่งผลหลายเครื่องกลับมา `List.copyOf` เก็บชุดรหัสของรอบนั้น ส่วน `call()` สร้างผลอย่างเดียว ไม่แก้ Machine หรือหน้าจอ ช่วงอุณหภูมิใหม่นี้อาจถึงระดับหยุดฉุกเฉินได้

## 3. เปลี่ยนงานทดลองเป็นงาน Sensor

กลับมาแก้ `DashboardApp.java` เพิ่ม Import ต่อจาก Import เดิม:

```java
import java.util.List;
```

แทนที่ `runBackgroundDemo()` ทั้ง Method ด้วย:

```java
private void simulateInBackground() {
    if (sensorBusy) {
        return;
    }
    List<Machine> snapshot = List.copyOf(service.getMachines());
    if (snapshot.isEmpty()) {
        statusLabel.setText("ยังไม่มีเครื่องจักรให้จำลอง");
        return;
    }
    List<String> ids = snapshot.stream().map(Machine::getId).toList();
    sensorBusy = true;
    sensorButton.setDisable(true);
    statusLabel.setText("กำลังอ่านค่า Sensor...");

    SensorSimulationTask task = new SensorSimulationTask(ids);
    task.setOnSucceeded(event -> {
        finishSensorTask();
        for (SensorUpdate update : task.getValue()) {
            Machine current = service.findById(update.machineId()).orElse(null);
            if (current == null || !snapshot.contains(current)) {
                continue;
            }
            service.updateSensor(update.machineId(), update.temperature(), update.vibration());
        }
        refreshDashboard();
        statusLabel.setText("อัปเดต Sensor ล่าสุดแล้ว");
    });
    task.setOnFailed(event -> {
        finishSensorTask();
        statusLabel.setText("อ่านค่า Sensor ไม่สำเร็จ กรุณาลองใหม่");
    });
    task.setOnCancelled(event -> finishSensorTask());

    activeSensorTask = task;
    Thread worker = new Thread(task, "sensor-worker");
    worker.setDaemon(true);
    worker.start();
}
```

เก็บ Field `sensorBusy`, `activeSensorTask`, Import `javafx.concurrent.Task`, Method `finishSensorTask()` และ `stage.setOnHidden(...)` จากตอนที่ 2 ไว้ ไม่เพิ่มซ้ำ

- `task.getValue()` คือรายการผลจาก `call()` แล้วส่งให้ Service อัปเดตทีละเครื่อง
- `snapshot` เก็บรายการ Machine ตอนเริ่มรอบ ใช้ข้ามผลของเครื่องที่ถูกลบหรือถูกสร้างใหม่ด้วยรหัสเดิมระหว่างรอ งานเบื้องหลังได้รับเฉพาะรหัส ไม่ได้อ่าน Object เหล่านี้
- `refreshDashboard()` อยู่หลัง Loop เพื่อให้ Summary คำนวณหลังอัปเดตครบ

ใน `buildMachineForm()` แทนที่สองบรรทัด `sensorButton.setText(...)` และ `sensorButton.setOnAction(...)` เดิม:

```java
sensorButton.setText("จำลอง Sensor 1 ครั้ง");
sensorButton.setOnAction(event -> simulateInBackground());
```

งานใหม่ไม่มี `Thread.sleep(3000)` แล้ว จึงเสร็จเร็วขึ้น แต่ยังใช้ Thread แยกและสร้าง Task ใหม่ทุกครั้ง ไฟล์ `DemoTask.java` เก็บไว้ทบทวนได้ แต่ปุ่มนี้จะไม่เรียกใช้งานแล้ว

## 4. รันและตรวจผล

บันทึกทั้งสามไฟล์ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

- กดครั้งเดียว ค่า Sensor และชั่วโมงอัปเดตทุกเครื่อง ตารางกับ Summary ต้องตรงกัน
- ถ้าไม่กด ค่าไม่อัปเดตเอง สถานะไม่จำเป็นต้องเปลี่ยนทุกรอบ
- ลบทุกเครื่องแล้วกดปุ่ม ต้องเห็นข้อความยังไม่มีเครื่องจักร ไม่เกิด Error

ค่าทั้งหมดยังเป็นการจำลอง ชั่วโมงเพิ่ม 1 ต่อการรับค่าหนึ่งรอบ ไม่ใช่ชั่วโมงจริง

ถัดไป: [ตอนที่ 4 — เริ่มและหยุด Auto Sensor ด้วย Timeline](ep10d-auto-sensor-timeline.md)
