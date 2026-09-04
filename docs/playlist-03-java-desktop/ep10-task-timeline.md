# EP 3.10 — จำลอง Sensor ด้วย Task, Thread และ Timeline

## สิ่งที่จะทำ

- ใช้ `Task` ทำงานเบื้องหลัง
- ใช้ `Timeline` เรียกงานเป็นช่วงเวลา
- อัปเดต UI เมื่อ Task สำเร็จโดยไม่ทำให้หน้าต่างค้าง

```mermaid
sequenceDiagram
    participant FX as JavaFX Application Thread
    participant Timeline
    participant Task as Background Task
    Timeline->>FX: ถึงรอบทุก 2 วินาที
    FX->>Task: start
    Task->>Task: สร้างค่าจาก Sensor
    Task-->>FX: onSucceeded
    FX->>FX: Service update + refresh UI
```

## 1. เพิ่มคอลัมน์สำหรับดูผลจาก Sensor

ก่อนสร้าง Background Task ให้เพิ่มคอลัมน์อุณหภูมิและแรงสั่นสะเทือนใน `buildMachineTable()` เพื่อให้มองเห็นค่าที่ Task อัปเดต:

```java
TableColumn<Machine, String> temperatureColumn = new TableColumn<>("อุณหภูมิ °C");
temperatureColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(
        format(data.getValue().getLatestReading().getTemperature())
));

TableColumn<Machine, String> vibrationColumn = new TableColumn<>("แรงสั่น mm/s");
vibrationColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(
        format(data.getValue().getLatestReading().getVibration())
));
```

แก้ `setAll(...)` ให้เรียงคอลัมน์ดังนี้:

```java
machineTable.getColumns().setAll(
        idColumn,
        nameColumn,
        locationColumn,
        statusColumn,
        temperatureColumn,
        vibrationColumn,
        hoursColumn,
        maintenanceColumn
);
```

เพิ่ม Import และ Method สำหรับจัดรูปแบบทศนิยม:

```java
import java.util.Locale;

private static String format(double value) {
    return String.format(Locale.ROOT, "%.1f", value);
}
```

## 2. สร้างข้อมูลผลลัพธ์

สร้าง `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/SensorUpdate.java`:

```java
package smartfactory.desktop;

public record SensorUpdate(String machineId, double temperature, double vibration) {}
```

## 3. สร้าง Background Task

สร้าง `SensorSimulationTask.java` ใน Package เดียวกัน:

```java
package smartfactory.desktop;

import javafx.concurrent.Task;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SensorSimulationTask extends Task<List<SensorUpdate>> {
    private final List<String> machineIds;

    public SensorSimulationTask(List<String> machineIds) {
        this.machineIds = List.copyOf(machineIds);
    }

    @Override
    protected List<SensorUpdate> call() {
        return machineIds.stream()
                .map(id -> new SensorUpdate(
                        id,
                        ThreadLocalRandom.current().nextDouble(50, 110),
                        ThreadLocalRandom.current().nextDouble(1, 9)
                ))
                .toList();
    }
}
```

`call()` ทำงานบน Background Thread และไม่แก้ JavaFX Component โดยตรง

## 4. รัน Task และกลับมาอัปเดต UI

เพิ่ม Method ใน `DashboardApp`:

```java
private void simulateInBackground() {
    List<String> ids = service.getMachines().stream().map(Machine::getId).toList();
    if (ids.isEmpty()) {
        return;
    }

    SensorSimulationTask task = new SensorSimulationTask(ids);
    task.setOnSucceeded(event -> {
        for (SensorUpdate update : task.getValue()) {
            service.updateSensor(update.machineId(), update.temperature(), update.vibration());
        }
        refreshDashboard();
        statusLabel.setText("อัปเดต Sensor ล่าสุดแล้ว");
    });

    Thread worker = new Thread(task, "sensor-simulation");
    worker.setDaemon(true);
    worker.start();
}
```

เพิ่ม Import `java.util.List` และ `smartfactory.model.Machine`

## 5. เรียกอัตโนมัติทุก 2 วินาที

เพิ่ม Import และ Field:

```java
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

private Timeline sensorTimeline;
```

ใน `start()` หลังสร้างหน้าจอ:

```java
sensorTimeline = new Timeline(
        new KeyFrame(Duration.seconds(2), event -> simulateInBackground())
);
sensorTimeline.setCycleCount(Timeline.INDEFINITE);
sensorTimeline.play();
stage.setOnHidden(event -> sensorTimeline.stop());
```

ผลที่ต้องเห็น: อุณหภูมิ สถานะ และ Summary เปลี่ยนทุก 2 วินาที แต่หน้าต่างยังลากและกดได้ตามปกติ

`refreshDashboard()` ต้องอยู่หลัง Loop เพราะ Summary ต้องคำนวณใหม่หลังอัปเดต Sensor ครบทุกเครื่อง หากเรียกก่อนอัปเดต ตัวเลขที่เห็นจะช้ากว่าข้อมูลจริงหนึ่งรอบ

## Challenge

เพิ่มปุ่มเริ่ม/หยุด Auto Sensor โดยตรวจ `sensorTimeline.getStatus()`

ถัดไป: [EP 3.11 — FXML และ Controller](ep11-fxml-controller.md)
