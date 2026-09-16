# EP 3.10 ตอนที่ 2 — ทดลองงานเบื้องหลังด้วย Task และ Thread

เป้าหมาย: กดปุ่มแล้วรอผล 3 วินาที แต่หน้าจอยังตอบสนอง ตอนนี้ยังไม่อัปเดต Sensor

ใช้โปรเจกต์จาก [ตอนที่ 1](ep10a-sensor-button.md) ต่อ แก้ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/DashboardApp.java`

จำก่อนเพียงสองคำ: **Task คือชุดงาน ส่วน Thread คือตัวที่รันงานนั้นแยกจากหน้าจอ**

เราจะประกอบ Method ทีละช่วง: เตรียมปุ่ม → สร้างงาน → รับผล → เริ่ม Thread แล้วจึงรันทดสอบเมื่อครบขั้นตอน

## 1. เตรียมตัวแปรและคืนสถานะปุ่ม

เพิ่ม Import ต่อจาก Import เดิม:

```java
import javafx.concurrent.Task;
```

เพิ่ม Field ต่อจาก `sensorButton` นอกทุก Method:

```java
private boolean sensorBusy;
private Task<?> activeSensorTask;
```

- `sensorBusy` เช็กว่ามีงานค้างอยู่หรือไม่ ค่าเริ่มต้นคือ `false`
- `activeSensorTask` เก็บงานไว้ขอยกเลิก ส่วน `<?>` ไม่เจาะจงชนิดผลลัพธ์

เพิ่ม Method นี้ภายใน Class `DashboardApp` แต่นอก Method อื่น:

```java
private void finishSensorTask() {
    sensorBusy = false;
    sensorButton.setDisable(false);
    activeSensorTask = null;
}
```

`finishSensorTask()` คืนสถานะว่างและเปิดปุ่ม ไม่ได้สั่งหยุด Thread

## 2. สร้างโครง Method สำหรับปุ่ม

ลบ `simulateOnce()` ทั้ง Method แล้ววางโครงนี้แทน:

```java
private void runBackgroundDemo() {
    if (sensorBusy) {
        return;
    }
    sensorBusy = true;
    sensorButton.setDisable(true);
    statusLabel.setText("กำลังทำงานเบื้องหลัง...");

    // A: สร้างงาน

    // B: รับผลเมื่องานจบ

    // C: เริ่ม Thread
}
```

มีงานอยู่ให้ออกจาก Method ด้วย `return`; ถ้ายังไม่มีจึงปิดปุ่มและเริ่มเตรียมงาน

## 3. ใส่งานที่จะทำใน Task

ภายใน `runBackgroundDemo()` แทนที่คอมเมนต์ `// A: สร้างงาน` ด้วย:

```java
Task<String> task = new Task<>() {
    @Override
    protected String call() throws Exception {
        Thread.sleep(3000);
        return "งานเบื้องหลังเสร็จแล้ว";
    }
};
```

- `Task<String>` ส่งผลกลับเป็นข้อความ จึงใช้ `String` เป็นชนิดผลลัพธ์ของ `call()`
- `call()` เป็น Abstract Method ของ `Task` ที่คลาสลูกต้องเขียนเนื้อหาให้ ส่วน `@Override` ระบุว่าเรากำลังทำตาม Method ของคลาสแม่
- `Thread.sleep(3000)` จำลองงานช้า 3 วินาที แล้ว `return` ส่งข้อความกลับ

**สร้าง Task ยังไม่ใช่เริ่มงาน** ไม่เรียก `call()` เอง และไม่แก้หน้าจอภายใน `call()`

<details>
<summary>อ่านเพิ่ม: ทำไมมีปีกกาหลัง new Task และคำอื่น ๆ ในบรรทัด call</summary>

- `new Task<>() { ... }` สร้าง Object จากคลาสลูกแบบไม่ตั้งชื่อ จึงเขียน `call()` ของคลาสลูกในปีกกานี้ได้
- `<>` ให้ Java อนุมานชนิดผลลัพธ์จาก `Task<String>` ด้านซ้าย
- `protected` ใช้ระดับการเข้าถึงตาม Method เดิม
- `throws Exception` ยอมให้ข้อผิดพลาดส่งออกจาก Method เช่น การรอถูกขัดจังหวะ

</details>

## 4. รับผลเมื่องานสำเร็จ

แทนที่คอมเมนต์ `// B: รับผลเมื่องานจบ` ด้วย:

```java
task.setOnSucceeded(event -> {
    finishSensorTask();
    statusLabel.setText(task.getValue());
});
```

- `setOnSucceeded` ฝากคำสั่งไว้ทำเมื่องานสำเร็จ ไม่ได้ทำทันที รูปแบบ `event ->` เหมือนที่เคยใช้กับปุ่ม
- `task.getValue()` รับผลจาก `call()` ตัวรับเหตุการณ์นี้อยู่บน JavaFX Application Thread จึงเปลี่ยน Label ได้

## 5. รองรับงานล้มเหลวและการยกเลิก

เพิ่มสองส่วนนี้ **หลัง `task.setOnSucceeded(...);` และก่อนคอมเมนต์ `// C`**:

```java
task.setOnFailed(event -> {
    finishSensorTask();
    statusLabel.setText("งานไม่สำเร็จ กรุณาลองใหม่");
});
```

`setOnFailed` คืนปุ่มและแจ้งผู้ใช้เมื่องานล้มเหลว

```java
task.setOnCancelled(event -> finishSensorTask());
```

`setOnCancelled` คืนสถานะเมื่องานถูกยกเลิก

ทั้งสามตัวเป็น **ทางเลือกตามผลของงาน** ไม่ได้ทำเรียงกันทั้งสำเร็จ ล้มเหลว และยกเลิก

## 6. เริ่มทำงานบน Thread แยก

แทนที่คอมเมนต์ `// C: เริ่ม Thread` ด้วย:

```java
activeSensorTask = task;
Thread worker = new Thread(task, "sensor-worker");
worker.setDaemon(true);
worker.start();
```

- `activeSensorTask` เก็บงานไว้ ส่วน `new Thread(...)` เตรียมตัวรันงานชื่อ `sensor-worker`
- `setDaemon(true)` ไม่ให้ Thread นี้รั้งโปรแกรมไว้ตอนออก ไม่ใช่คำสั่งยกเลิกงาน
- **`start()` เริ่ม Thread ใหม่** ให้รันงานใน `call()` แยกจาก Thread ของหน้าจอ

ทุกครั้งที่กดปุ่ม Method นี้จะสร้าง Task ใหม่ เพราะ Task หนึ่งตัวใช้รันได้ครั้งเดียว

## 7. เชื่อมปุ่มและดูแลตอนปิดหน้าต่าง

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

`cancel()` ขอให้ยกเลิกงานเมื่อปิดหน้าต่าง ไม่ใช่การบังคับฆ่า Thread; การรอด้วย `sleep` ในตัวอย่างนี้ถูกขัดจังหวะได้

## 8. รันและตรวจผล

บันทึกไฟล์หลังทำครบ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

- กดปุ่ม เห็นข้อความกำลังทำงาน และปุ่มนี้ถูกปิดชั่วคราว
- ระหว่างรอ ลองเลือกแถวหรือพิมพ์ชื่อเครื่องจักร ยังใช้งานได้
- ประมาณ 3 วินาทีต่อมา เห็นข้อความงานเสร็จและกดปุ่มได้อีก ค่า Sensor และชั่วโมงยังไม่เปลี่ยน
- ลองปิดหน้าต่างระหว่างรอ โปรแกรมต้องปิดได้

สรุป: `call()` ทำงานเบื้องหลัง ส่วน `setOnSucceeded` รับผลมาแสดงบนหน้าจอ

ถัดไป: [ตอนที่ 3 — ส่งผล Sensor กลับมาอัปเดตตาราง](ep10c-sensor-task-result.md)
