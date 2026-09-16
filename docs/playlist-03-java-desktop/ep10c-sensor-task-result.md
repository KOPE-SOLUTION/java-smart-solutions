# EP 3.10 ตอนที่ 3A — ส่งผล Sensor กลับมาอัปเดตตาราง

เป้าหมาย: กดปุ่มให้ Task จำลองค่า Sensor แล้วนำผลมาอัปเดตตารางและ Summary

ใช้โปรเจกต์จาก [ตอนที่ 2](ep10b-task-thread.md) ต่อ ปิดโปรแกรมก่อนแก้ไฟล์ใน `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop`

ตอนที่ 2 ส่งกลับข้อความหนึ่งข้อความ ตอนนี้ส่งกลับ **รายการค่าของเครื่องจักร** โดย Task สร้างข้อมูล ส่วนตัวรับผลบน JavaFX Thread นำไปอัปเดตผ่าน Service

## 1. สร้างชนิดข้อมูลสำหรับผลลัพธ์

สร้าง `SensorUpdate.java` ในโฟลเดอร์เดียวกับ `DashboardApp.java` แล้ววางทั้งไฟล์:

```java
package smartfactory.desktop;

public record SensorUpdate(String machineId, double temperature, double vibration) {}
```

เหมือน `MachineRow` ที่เคยเรียน แต่เก็บรหัสเครื่อง อุณหภูมิ และแรงสั่น เช่น M-001, 85, 3.2 หนึ่ง Object คือผลของหนึ่งเครื่อง ยังไม่ได้เปลี่ยนข้อมูลใน Machine

## 2. สร้าง Task สำหรับจำลองค่า

สร้าง `SensorSimulationTask.java` ในโฟลเดอร์เดียวกัน แล้ววางทั้งไฟล์:

```java
package smartfactory.desktop;

import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SensorSimulationTask extends Task<List<SensorUpdate>> {
    private final List<String> machineIds;

    public SensorSimulationTask(List<String> machineIds) {
        this.machineIds = List.copyOf(machineIds);
    }

    @Override
    protected List<SensorUpdate> call() {
        Random random = new Random();
        List<SensorUpdate> results = new ArrayList<>();
        for (String id : machineIds) {
            double temperature = 50.0 + random.nextDouble() * 60.0;
            double vibration = 1.0 + random.nextDouble() * 8.0;
            results.add(new SensorUpdate(id, temperature, vibration));
        }
        return results;
    }
}
```

อ่านเป็นสามช่วง:

- `Task<List<SensorUpdate>>`: งานหนึ่งรอบส่งกลับรายการผล Sensor หลายเครื่อง ใช้คลาสมีชื่อแทน Anonymous Class เพราะจะใช้ต่อกับปุ่มและ Auto Sensor
- Constructor รับรหัสเครื่อง โดย `List.copyOf` ทำสำเนารายการที่แก้ไขสมาชิกไม่ได้ เก็บรหัสสำหรับรอบนี้
- `call()` วนสุ่มค่า ใส่รายการ แล้ว `return results` ไม่แตะ Service หรือหน้าจอ

ใช้ `Random` เหมือนตอนที่ 1 แต่อุณหภูมิเป็น 50 ถึงน้อยกว่า 110 จึงมีโอกาสถึงระดับหยุดฉุกเฉิน แรงสั่นเป็น 1 ถึงน้อยกว่า 9 ไม่มีการรอ 10 วินาทีแล้ว

## 3. รับผลแล้วอัปเดตหน้าจอ

ใน `DashboardApp.java` เพิ่ม Import ต่อจาก Import เดิม หากมีแล้วไม่เพิ่มซ้ำ:

```java
import java.util.ArrayList;
import java.util.List;
```

แทนที่ `runBackgroundDemo()` ทั้ง Method ซึ่งอยู่หลัง `refreshDashboard()` ด้วย:

```java
private void simulateInBackground() {
    List<String> ids = new ArrayList<>();
    for (Machine machine : service.getMachines()) {
        ids.add(machine.getId());
    }

    SensorSimulationTask task = new SensorSimulationTask(ids);
    task.setOnSucceeded(event -> {
        for (SensorUpdate update : task.getValue()) {
            service.updateSensor(update.machineId(), update.temperature(), update.vibration());
        }
        refreshDashboard();
        statusLabel.setText("อัปเดต Sensor ล่าสุดแล้ว");
    });

    Thread worker = new Thread(task, "sensor-worker");
    worker.start();
}
```

- Loop แรกเก็บเฉพาะรหัสให้ Task ไม่ส่ง Machine ไปแก้บน Background Thread
- `task.getValue()` ได้รายการผล จึงวนส่งให้ Service ทีละเครื่อง
- `setOnSucceeded` ทำงานบน JavaFX Thread จึงอัปเดตข้อมูลและ Refresh หน้าจอได้

## 4. เชื่อมปุ่มและรัน

ใน `buildMachineForm()` แทนที่สองบรรทัด `sensorButton.setText(...)` และ `sensorButton.setOnAction(...)` เดิม:

```java
sensorButton.setText("จำลอง Sensor 1 ครั้ง");
sensorButton.setOnAction(event -> simulateInBackground());
```

เก็บ `actionButtons.getChildren().add(sensorButton);` ไว้ครั้งเดียว บันทึกทั้งสามไฟล์ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

1. เปิดโปรแกรมใหม่ กด **จำลอง Sensor 1 ครั้ง** แล้วรอข้อความ **อัปเดต Sensor ล่าสุดแล้ว**
2. ชั่วโมง M-001, M-002, M-003 เพิ่มจาก 121, 481, 521 เป็น 122, 482, 522 ค่า Sensor ถูกสุ่มใหม่ และ Summary ต้องตรงกับสถานะในตาราง
3. ไม่กดซ้ำ ค่าต้องไม่เปลี่ยนเอง สถานะไม่จำเป็นต้องเปลี่ยนทุกรอบ

ตัวอย่างนี้ทดลองทางสำเร็จก่อน: กดทีละครั้ง รอผลก่อนเพิ่ม ลบ บำรุงรักษา หรือปิดหน้าต่าง ยังไม่รองรับการแก้รายการระหว่างรองานและงานล้มเหลว เราจะเพิ่มใน 3B ชั่วโมงเพิ่ม 1 ต่อการรับค่าหนึ่งรอบ ไม่ใช่ชั่วโมงจริง

ถัดไป: [ตอนที่ 3B — ป้องกันงานซ้อนและรับข้อผิดพลาด](ep10c2-sensor-task-safety.md)
