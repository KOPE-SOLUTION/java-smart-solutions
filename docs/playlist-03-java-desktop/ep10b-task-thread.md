# EP 3.10 ตอนที่ 2 — ทดลองงานเบื้องหลังด้วย Task และ Thread

เป้าหมาย: กดปุ่มแล้วรอผล 3 วินาที แต่หน้าจอยังตอบสนอง ตอนนี้ยังไม่อัปเดต Sensor

ใช้โปรเจกต์จาก [ตอนที่ 1](ep10a-sensor-button.md) ต่อ แก้ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/DashboardApp.java`

`Task` คือชุดงาน ส่วน `Thread` คือตัวที่รันงานนั้นแยกจาก Thread ที่ดูแลหน้าจอ

## 1. เตรียมงานทดลอง

เพิ่ม Import ต่อจาก Import เดิม:

```java
import javafx.concurrent.Task;
```

เพิ่ม Field ต่อจาก `sensorButton`:

```java
private boolean sensorBusy;
private Task<?> activeSensorTask;
```

`sensorBusy` บอกว่ามีงานค้างอยู่ ส่วน `activeSensorTask` เก็บงานล่าสุดไว้ยกเลิกเมื่อปิดหน้าต่าง `Task<?>` หมายถึง Task ที่ยังไม่ระบุชนิดผลลัพธ์ใน Field นี้

ลบ Method `simulateOnce()` ทั้ง Method แล้ววาง Method นี้แทน:

```java
private void runBackgroundDemo() {
    if (sensorBusy) {
        return;
    }
    sensorBusy = true;
    sensorButton.setDisable(true);
    statusLabel.setText("กำลังทำงานเบื้องหลัง...");

    Task<String> task = new Task<>() {
        @Override
        protected String call() throws Exception {
            Thread.sleep(3000);
            return "งานเบื้องหลังเสร็จแล้ว";
        }
    };

    task.setOnSucceeded(event -> {
        finishSensorTask();
        statusLabel.setText(task.getValue());
    });
    task.setOnFailed(event -> {
        finishSensorTask();
        statusLabel.setText("งานไม่สำเร็จ กรุณาลองใหม่");
    });
    task.setOnCancelled(event -> finishSensorTask());

    activeSensorTask = task;
    Thread worker = new Thread(task, "sensor-worker");
    worker.setDaemon(true);
    worker.start();
}
```

เพิ่ม Method ต่อจาก `runBackgroundDemo()`:

```java
private void finishSensorTask() {
    sensorBusy = false;
    sensorButton.setDisable(false);
    activeSensorTask = null;
}
```

- `call()` รอ 3 วินาทีบน Background Thread เพื่อจำลองงานช้า ไม่ใช่การหน่วงที่ต้องใส่ในงานจริง
- `setOnSucceeded` ทำงานบน JavaFX Application Thread จึงเปลี่ยน Label ได้ ส่วน `call()` ไม่แก้หน้าจอ
- `worker.start()` เริ่ม Thread ใหม่ และ `setDaemon(true)` ไม่ให้งานนี้รั้งโปรแกรมไว้เมื่อโปรแกรมกำลังออก

## 2. เปลี่ยนปุ่มเดิมให้เรียกงานทดลอง

ใน `buildMachineForm()` แทนที่เฉพาะ `sensorButton.setOnAction(...)` เดิมด้วย:

```java
sensorButton.setText("ทดสอบงานเบื้องหลัง");
sensorButton.setOnAction(event -> runBackgroundDemo());
```

เก็บ `actionButtons.getChildren().add(sensorButton);` ไว้เพียงครั้งเดียว และลบ `import java.util.Random;` เพราะตอนนี้ไม่ได้ใช้แล้ว

ใน `start()` เพิ่มหลัง `stage.show();` ก่อนปีกกาปิด Method:

```java
stage.setOnHidden(event -> {
    if (activeSensorTask != null) {
        activeSensorTask.cancel();
    }
});
```

## 3. รันและตรวจผล

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

รันจากโฟลเดอร์หลักของ Repository เช่นเดิม แล้วทดลอง:

- กดปุ่ม เห็นข้อความกำลังทำงาน และปุ่มนี้ถูกปิดชั่วคราวเพื่อไม่ให้กดงานซ้ำ
- ระหว่างรอ ลองเลือกแถวหรือพิมพ์ชื่อเครื่องจักร ยังใช้งานได้
- ประมาณ 3 วินาทีต่อมา เห็นข้อความงานเสร็จและกดปุ่มได้อีก ค่า Sensor และชั่วโมงยังไม่เปลี่ยน
- ลองปิดหน้าต่างระหว่างรอ โปรแกรมต้องปิดได้

ถัดไป: [ตอนที่ 3 — ส่งผล Sensor กลับมาอัปเดตตาราง](ep10c-sensor-task-result.md)
