# EP 3.10 ตอนที่ 3 — ส่งผล Sensor กลับมาอัปเดตตาราง

เป้าหมาย: ใช้ Task จำลองค่า Sensor แล้วอัปเดต Service ตาราง และ Summary พร้อมป้องกันงานซ้อนและรับข้อผิดพลาด

ใช้โปรเจกต์จาก [ตอนที่ 2](ep10b-task-thread.md) ต่อ ปิดโปรแกรมก่อนแก้ไฟล์ใน `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop`

## 1. สร้างกล่องใส่ผลลัพธ์

สร้าง `SensorUpdate.java` ในโฟลเดอร์เดียวกับ `DashboardApp.java` แล้ววางทั้งไฟล์:

```java
package smartfactory.desktop;

public record SensorUpdate(String machineId, double temperature, double vibration) {}
```

หนึ่ง Object คือผลของหนึ่งเครื่อง เช่น M-001 อุณหภูมิ 85 และแรงสั่น 3.2 ยังไม่ได้เปลี่ยน Machine

## 2. แยกงาน Sensor เป็นคลาสมีชื่อ

สร้าง `SensorSimulationTask.java` ในโฟลเดอร์เดียวกัน แล้ววางทั้งไฟล์:

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
            double temperature = ThreadLocalRandom.current().nextDouble(50, 110);
            double vibration = ThreadLocalRandom.current().nextDouble(1, 9);
            results.add(new SensorUpdate(id, temperature, vibration));
        }
        return results;
    }
}
```

- ยังสืบทอดจาก Task เหมือนตอนที่ 2 แต่แยกคลาสเพราะงานมีรายละเอียดมากขึ้น และส่งกลับเป็นรายการ `SensorUpdate`
- `List.copyOf` เก็บชุดรหัสของรอบนั้น ส่วน `call()` สร้างผลอย่างเดียว ไม่แก้ Machine หรือหน้าจอ
- ช่วงอุณหภูมิใหม่นี้อาจถึงระดับหยุดฉุกเฉินได้ ไม่มีการหน่วง 3 วินาทีแล้ว

## 3. เตรียมป้องกันงานซ้อน

ใน `DashboardApp.java` เพิ่ม Import ต่อจาก Import เดิม:

```java
import java.util.List;
```

เพิ่ม Field ต่อจาก `sensorButton` นอกทุก Method:

```java
private boolean sensorBusy;
```

แทนที่ `runBackgroundDemo()` ทั้ง Method ด้วยโครงนี้ แล้วเติมจุด A–C ในขั้นถัดไปก่อนรัน:

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

    SensorSimulationTask task = new SensorSimulationTask(ids);
    // A: รับผลสำเร็จ
    // B: รับข้อผิดพลาด
    // C: เริ่ม Thread
}
```

งานเก่ายังไม่จบให้ข้าม ถ้าไม่มีเครื่องให้แจ้งผู้ใช้ ส่วน `snapshot` เก็บรายการเครื่องตอนเริ่มงาน โดยส่งเฉพาะรหัสให้ Task

เพิ่ม Method นี้ **ถัดจากปีกกาปิด `simulateInBackground()` ทันที ก่อน Method ถัดไป** ให้ทั้งสอง Method อยู่ระดับเดียวกัน:

```java
private void finishSensorTask() {
    sensorBusy = false;
    sensorButton.setDisable(false);
}
```

## 4. รับผลสำเร็จและข้อผิดพลาด

ภายใน `simulateInBackground()` แทนที่ `// A: รับผลสำเร็จ` ด้วย:

```java
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
```

รับผลแล้วให้ Service อัปเดตทีละเครื่อง จากนั้นค่อย Refresh หน้าจอ เงื่อนไข `continue` ข้ามเครื่องที่ถูกลบหรือถูกสร้างใหม่ด้วยรหัสเดิมระหว่างรอ

แทนที่ `// B: รับข้อผิดพลาด` ด้วย:

```java
task.setOnFailed(event -> {
    finishSensorTask();
    statusLabel.setText("อ่านค่า Sensor ไม่สำเร็จ กรุณาลองใหม่");
});
```

งานสำเร็จและล้มเหลวเป็นคนละกรณี แต่ต้องคืนสถานะปุ่มทั้งคู่

## 5. เริ่มงานและเชื่อมปุ่ม

แทนที่ `// C: เริ่ม Thread` ด้วย:

```java
Thread worker = new Thread(task, "sensor-worker");
worker.start();
```

ใน `buildMachineForm()` แทนที่สองบรรทัด `sensorButton.setText(...)` และ `sensorButton.setOnAction(...)` เดิม:

```java
sensorButton.setText("จำลอง Sensor 1 ครั้ง");
sensorButton.setOnAction(event -> simulateInBackground());
```

## 6. รันและตรวจผล

บันทึกทั้งสามไฟล์ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

- กดครั้งเดียว ค่า Sensor และชั่วโมงอัปเดตทุกเครื่อง ตารางกับ Summary ต้องตรงกัน
- งานยังไม่จบจะเริ่มซ้อนไม่ได้ งานจำลองนี้เร็วมากจึงอาจมองไม่ทันว่าปุ่มถูกปิดชั่วคราว
- ถ้าไม่กด ค่าไม่อัปเดตเอง สถานะไม่จำเป็นต้องเปลี่ยนทุกรอบ
- ลบทุกเครื่องแล้วกดปุ่ม ต้องเห็นข้อความยังไม่มีเครื่องจักร

<details>
<summary>ทดลองกรณีล้มเหลว</summary>

ปิดโปรแกรม ใน `SensorSimulationTask.call()` แทนที่เฉพาะ `return results;` ชั่วคราวด้วย `throw new IllegalStateException("ทดลองงานล้มเหลว");` แล้วรันและกดปุ่ม ต้องเห็นข้อความอ่านค่า Sensor ไม่สำเร็จและปุ่มกลับมาใช้งานได้ จากนั้นปิดโปรแกรมแล้วคืน `return results;` ก่อนเรียนต่อ

</details>

ตอนนี้ยังไม่ยกเลิกงานเมื่อปิดหน้าต่าง ให้รอรอบปัจจุบันจบก่อนปิด เราจะเพิ่มเรื่องนี้พร้อม Auto Sensor ในตอนที่ 4 ชั่วโมงเพิ่ม 1 ต่อการรับค่าหนึ่งรอบ ไม่ใช่ชั่วโมงจริง

ถัดไป: [ตอนที่ 4 — เริ่มและหยุด Auto Sensor ด้วย Timeline](ep10d-auto-sensor-timeline.md)
