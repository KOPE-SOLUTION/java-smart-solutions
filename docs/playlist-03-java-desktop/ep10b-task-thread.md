# EP 3.10 ตอนที่ 2 — ทดลองงานเบื้องหลังด้วย Task และ Thread

เป้าหมาย: กดปุ่มแล้วรอผล 3 วินาที แต่หน้าจอยังตอบสนอง ตอนนี้ยังไม่อัปเดต Sensor

ใช้โปรเจกต์จาก [ตอนที่ 1](ep10a-sensor-button.md) ต่อ ปิดโปรแกรมก่อนแก้ไฟล์ใน `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop`

**Task คือชุดงาน ส่วน Thread คือตัวรันงานแยกจากหน้าจอ** ตอนนี้มีสองรอบทดลอง: รับผลสำเร็จก่อน แล้วค่อยดูแลปุ่ม ข้อผิดพลาด และการปิดหน้าต่าง

## รอบแรก — เห็นผลโดยหน้าจอไม่ค้าง

### 1. สร้างคลาสงานที่มีชื่อ

สร้างไฟล์ `DemoTask.java` ในโฟลเดอร์เดียวกับ `DashboardApp.java` แล้ววางทั้งไฟล์:

```java
package smartfactory.desktop;

import javafx.concurrent.Task;

public class DemoTask extends Task<String> {
    @Override
    protected String call() throws Exception {
        Thread.sleep(3000);
        return "งานเบื้องหลังเสร็จแล้ว";
    }
}
```

- `DemoTask` เป็นคลาสลูกของ `Task<String>` จึงส่งผลกลับเป็นข้อความ
- `@Override call()` ใส่เนื้อหางานที่คลาสแม่กำหนดไว้: รอ 3 วินาทีแล้วคืนข้อความ
- ไม่เรียก `call()` เอง และไม่แก้ Label หรือตารางภายใน Method นี้

### 2. สร้างงานและรับผลสำเร็จ

กลับมา `DashboardApp.java` ลบ `simulateOnce()` ทั้ง Method แล้ววาง Method นี้แทนตำแหน่งเดิม หลังปีกกาปิด `refreshDashboard()`:

```java
private void runBackgroundDemo() {
    statusLabel.setText("กำลังทำงานเบื้องหลัง...");

    DemoTask task = new DemoTask();
    task.setOnSucceeded(event -> {
        statusLabel.setText(task.getValue());
    });

    Thread worker = new Thread(task, "sensor-worker");
    worker.start();
}
```

- `new DemoTask()` สร้างงานไว้ ส่วน `worker.start()` จึงเริ่ม Thread ให้รันงานนั้น
- `setOnSucceeded` ฝากคำสั่งไว้ทำเมื่องานสำเร็จ ส่วน `getValue()` รับข้อความที่ `call()` คืนมา
- ตัวรับเหตุการณ์นี้ทำงานบน JavaFX Application Thread จึงแก้ Label ได้

ใน `buildMachineForm()` แทนที่ `sensorButton.setOnAction(...)` เดิมด้วยสองบรรทัดนี้:

```java
sensorButton.setText("ทดสอบงานเบื้องหลัง");
sensorButton.setOnAction(event -> runBackgroundDemo());
```

เก็บ `actionButtons.getChildren().add(sensorButton);` ไว้ครั้งเดียว ลบ `import java.util.Random;` ที่ไม่ได้ใช้แล้ว ส่วน `DemoTask` อยู่ Package เดียวกันจึงไม่ต้อง Import

### 3. รันทันทีแล้วสังเกตหน้าจอ

บันทึกทั้งสองไฟล์ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

- กดปุ่มครั้งเดียว ระหว่างรอลองพิมพ์ชื่อหรือเลือกแถว หน้าจอต้องยังตอบสนอง
- ประมาณ 3 วินาทีต่อมา Label แสดงว่างานเสร็จ ค่า Sensor และชั่วโมงไม่เปลี่ยน

รอบนี้ยังไม่ป้องกันการกดซ้ำ ให้รอผลก่อนกดอีกครั้ง และรอให้งานจบก่อนปิดโปรแกรมเพื่อแก้รอบถัดไป

## รอบที่สอง — ดูแลการเริ่มและจบงาน

### 4. ป้องกันงานซ้อน

ใน `DashboardApp.java` เพิ่ม Import ต่อจาก Import เดิม:

```java
import javafx.concurrent.Task;
```

เพิ่ม Field ต่อจาก `sensorButton` นอกทุก Method:

```java
private boolean sensorBusy;
private Task<?> activeSensorTask;
```

`sensorBusy` เช็กว่ามีงานค้างหรือไม่ ส่วน `activeSensorTask` เก็บงานไว้ขอยกเลิก โดย `<?>` ไม่เจาะจงชนิดผลลัพธ์

เพิ่มโค้ดนี้ **บรรทัดแรกภายใน `runBackgroundDemo()` ก่อน `statusLabel.setText(...)`**:

```java
if (sensorBusy) {
    return;
}
sensorBusy = true;
sensorButton.setDisable(true);
```

เพิ่ม Method นี้ **ถัดจากปีกกา `}` ที่ปิด `runBackgroundDemo()` ทันที ก่อน Method ถัดไป** ให้ `private void` อยู่ระดับเดียวกัน ไม่ซ้อน Method:

```java
private void finishSensorTask() {
    sensorBusy = false;
    sensorButton.setDisable(false);
    activeSensorTask = null;
}
```

กลับเข้าไปใน `runBackgroundDemo()` แทนที่เฉพาะ `task.setOnSucceeded(...)` เดิมด้วย:

```java
task.setOnSucceeded(event -> {
    finishSensorTask();
    statusLabel.setText(task.getValue());
});
```

รันด้วยคำสั่งเดิม: ระหว่างรอ ปุ่มนี้ถูกปิด แต่ช่องกรอกยังใช้ได้ พองานเสร็จปุ่มกลับมาใช้งานได้ แล้วปิดโปรแกรมก่อนแก้ขั้นต่อไป

### 5. เพิ่มทางออกเมื่องานล้มเหลว

ใน `runBackgroundDemo()` เพิ่มหลัง `task.setOnSucceeded(...);` และก่อนบรรทัด `Thread worker = ...`:

```java
task.setOnFailed(event -> {
    finishSensorTask();
    statusLabel.setText("งานไม่สำเร็จ กรุณาลองใหม่");
});
```

ถ้างานล้มเหลว ต้องคืนปุ่มด้วย ไม่เช่นนั้นปุ่มจะถูกปิดค้างไว้

**ทดลองข้อผิดพลาด:** ใน `DemoTask.java` แทนที่เฉพาะบรรทัด `return "งานเบื้องหลังเสร็จแล้ว";` ชั่วคราวด้วย:

```java
throw new IllegalStateException("ทดลองงานล้มเหลว");
```

รันแล้วกดปุ่ม รอประมาณ 3 วินาที ต้องเห็นข้อความงานไม่สำเร็จและปุ่มกลับมาใช้งานได้ จากนั้นปิดโปรแกรมและ **เปลี่ยนบรรทัด `throw` กลับเป็น `return` เดิม** ก่อนทำขั้นถัดไป

### 6. ขอยกเลิกงานเมื่อปิดหน้าต่าง

ใน `runBackgroundDemo()` เพิ่มหลัง `task.setOnFailed(...);` และก่อนบรรทัด `Thread worker = ...`:

```java
task.setOnCancelled(event -> finishSensorTask());
activeSensorTask = task;
```

`setOnCancelled` คืนสถานะเมื่องานถูกยกเลิก ส่วน `activeSensorTask` เก็บงานปัจจุบันไว้ ทั้ง Succeeded, Failed และ Cancelled เป็นคนละกรณี ไม่ได้ทำเรียงกัน

ใน `start()` เพิ่มหลัง `stage.show();` ก่อนปีกกาปิด Method:

```java
stage.setOnHidden(event -> {
    if (activeSensorTask != null) {
        activeSensorTask.cancel();
    }
});
```

`cancel()` ขอยกเลิกงาน ไม่ใช่การบังคับฆ่า Thread; การรอด้วย `sleep` ในตัวอย่างนี้ถูกขัดจังหวะได้

กลับไป `runBackgroundDemo()` เพิ่มบรรทัดนี้ **ระหว่าง `new Thread(...)` กับ `worker.start();`**:

```java
worker.setDaemon(true);
```

Daemon หมายถึง Thread นี้ไม่ทำให้ JVM ต้องอยู่ต่อเมื่อไม่มี Thread แบบ non-daemon เหลือแล้ว ไม่ใช่คำสั่งยกเลิกงานแทน `cancel()`

### 7. รันตรวจรอบสุดท้าย

ตรวจว่า `DemoTask.call()` ใช้ `return` ตามเดิม บันทึกทั้งสองไฟล์ แล้วรันด้วยคำสั่งเดิม:

- กดปุ่ม รอผลโดยหน้าจอยังตอบสนอง ปุ่มกลับมาเมื่อสำเร็จ และกดรอบใหม่ได้
- กดปุ่มอีกครั้งแล้วปิดหน้าต่างทันที โปรแกรมต้องปิดได้และ Terminal กลับมารับคำสั่ง
- ค่า Sensor และชั่วโมงยังไม่เปลี่ยน เพราะตอนนี้เป็นเพียงงานทดลอง

ทุกครั้งที่กดปุ่มจะสร้าง `DemoTask` ใหม่ เพราะ Task หนึ่งตัวใช้รันได้ครั้งเดียว

<details>
<summary>อ่านเพิ่ม: ถ้าเจอ new Task() แล้วมีปีกกาต่อท้าย</summary>

รูปแบบ `new Task<>() { ... }` คือการสร้างคลาสลูกแบบไม่ตั้งชื่อ (Anonymous Class) แล้วสร้าง Object ตรงจุดใช้งาน คล้ายกับ `new TableCell<>() { ... }` ที่เคยใช้ใน EP3.8

ยังเป็นการสืบทอดและ Override เหมือน `DemoTask` แต่บทนี้ใช้คลาสมีชื่อเพื่อแยกงานออกจากหน้าจอให้ชัดเจน ไม่ต้องเปลี่ยนโค้ดตามรูปแบบนี้

</details>

สรุป: งานไม่ได้เร็วขึ้น แต่ Thread ของหน้าจอไม่ต้องหยุดรอ ตอนหน้าจะเปลี่ยน `DemoTask` เป็นงาน Sensor ที่ส่งผลจริงกลับมาให้ Service

ถัดไป: [ตอนที่ 3 — ส่งผล Sensor กลับมาอัปเดตตาราง](ep10c-sensor-task-result.md)
