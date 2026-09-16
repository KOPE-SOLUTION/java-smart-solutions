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

- `sensorBusy` เริ่มต้นเป็น `false` ใช้เช็กว่ามีงานค้างอยู่หรือไม่
- `activeSensorTask` เก็บ Task ที่กำลังทำไว้ขอยกเลิกภายหลัง เครื่องหมาย `<?>` หมายถึง Field นี้รับ Task ได้โดยไม่เจาะจงชนิดผลลัพธ์

เพิ่ม Method นี้ภายใน Class `DashboardApp` แต่นอก Method อื่น:

```java
private void finishSensorTask() {
    sensorBusy = false;
    sensorButton.setDisable(false);
    activeSensorTask = null;
}
```

หน้าที่เดียวของ Method นี้คือคืนสถานะว่าง เปิดปุ่ม และเลิกเก็บงานที่จบแล้ว ไม่ใช่คำสั่งหยุด Thread

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

ถ้ามีงานอยู่ `return` จะออกจาก Method ทันที ถ้ายังไม่มี จึงตั้งสถานะไม่ว่างและปิดปุ่มชั่วคราว

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

อ่านทีละส่วน:

- `Task<String>` คืองานที่ส่งผลกลับเป็นข้อความ ส่วน `<>` ด้านขวาให้ Java อนุมานชนิดจากด้านซ้าย
- `new Task<>() { ... }` สร้าง Object จากคลาสลูกของ `Task` แบบไม่ตั้งชื่อ จึงเขียน Method ของคลาสลูกไว้ในวงเล็บปีกกานี้ได้
- `@Override` บอกว่าเรากำลังเขียนทับ Method ที่คลาสแม่กำหนดไว้ ไม่ใช่คำสั่งเริ่มงาน
- `call()` เป็น Abstract Method ของ `Task` ที่เราต้องเขียนเนื้อหางานให้ ในกรณีนี้จึงคืนค่าเป็น `String` ตาม `Task<String>`
- `protected` ใช้ระดับการเข้าถึงตาม Method เดิม ส่วน `throws Exception` ยอมให้ข้อผิดพลาดส่งออกจาก Method ได้ เช่น การรอถูกขัดจังหวะ
- `Thread.sleep(3000)` จำลองงานช้า 3 วินาที แล้ว `return` ส่งข้อความเป็นผลลัพธ์ของ Task

**ตอนนี้เพียงสร้างงานไว้ ยังไม่ได้เริ่มทำงาน** ไม่ต้องเรียก `call()` เอง และอย่าแก้ Label หรือตารางภายใน `call()`

## 4. รับผลเมื่องานสำเร็จ

แทนที่คอมเมนต์ `// B: รับผลเมื่องานจบ` ด้วย:

```java
task.setOnSucceeded(event -> {
    finishSensorTask();
    statusLabel.setText(task.getValue());
});
```

ส่วนนี้คือการฝากคำสั่งไว้ทำ **เมื่องานสำเร็จ** ยังไม่ได้ทำทันทีที่เขียนถึงบรรทัดนี้

- `event -> { ... }` รูปแบบเดียวกับที่ใช้กับปุ่ม `setOnAction` แต่ครั้งนี้รอเหตุการณ์งานสำเร็จ
- `finishSensorTask()` คืนสถานะปุ่ม ส่วน `task.getValue()` รับข้อความที่ `call()` ส่งกลับมา

ตัวรับเหตุการณ์นี้ทำงานบน JavaFX Application Thread จึงแก้ Label ได้ ต่างจาก `call()` ที่เราจะให้ทำบน Background Thread

## 5. รองรับงานล้มเหลวและการยกเลิก

เพิ่มสองส่วนนี้ **หลัง `task.setOnSucceeded(...);` และก่อนคอมเมนต์ `// C`**:

```java
task.setOnFailed(event -> {
    finishSensorTask();
    statusLabel.setText("งานไม่สำเร็จ กรุณาลองใหม่");
});
```

ถ้างานล้มเหลว ให้คืนปุ่มและแจ้งผู้ใช้ แทนการปล่อยให้ปุ่มถูกปิดค้างไว้

```java
task.setOnCancelled(event -> finishSensorTask());
```

ถ้างานถูกยกเลิก ให้คืนสถานะเช่นกัน บรรทัดสั้นนี้เขียนได้เพราะมีเพียงคำสั่งเดียว

ทั้งสามตัวเป็น **ทางเลือกตามผลของงาน** ไม่ได้ทำเรียงกันทั้งสำเร็จ ล้มเหลว และยกเลิก

## 6. เริ่มทำงานบน Thread แยก

แทนที่คอมเมนต์ `// C: เริ่ม Thread` ด้วย:

```java
activeSensorTask = task;
Thread worker = new Thread(task, "sensor-worker");
worker.setDaemon(true);
worker.start();
```

- เก็บ `task` ไว้ก่อน เพื่อขอยกเลิกเมื่อปิดหน้าต่าง
- สร้าง Thread ให้รัน Task นี้ ชื่อ `sensor-worker` เป็นชื่อสำหรับช่วยตรวจสอบงาน
- `setDaemon(true)` ไม่ให้ Thread นี้รั้งโปรแกรมไว้ตอนออก ไม่ใช่คำสั่งยกเลิกงาน
- **`start()` คือจุดเริ่ม Thread ใหม่** ซึ่งจะรันงานใน `call()` ทำให้การรอ 3 วินาทีไม่ขวาง Thread ของหน้าจอ

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

เมื่อหน้าต่างปิด ถ้ายังมีงานอยู่ให้ขอยกเลิกงานนั้น `cancel()` ไม่ใช่การบังคับฆ่า Thread; สำหรับงานทดลองนี้ การรอด้วย `sleep` สามารถถูกขัดจังหวะได้

## 8. รันและตรวจผล

บันทึกไฟล์หลังทำครบ แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

- กดปุ่ม เห็นข้อความกำลังทำงาน และปุ่มนี้ถูกปิดชั่วคราว
- ระหว่างรอ ลองเลือกแถวหรือพิมพ์ชื่อเครื่องจักร ยังใช้งานได้
- ประมาณ 3 วินาทีต่อมา เห็นข้อความงานเสร็จและกดปุ่มได้อีก ค่า Sensor และชั่วโมงยังไม่เปลี่ยน
- ลองปิดหน้าต่างระหว่างรอ โปรแกรมต้องปิดได้

สรุป: `call()` ทำงานและส่งผล ส่วน `setOnSucceeded` รับผลมาแสดงบนหน้าจอ งานไม่ได้เร็วขึ้น แต่หน้าจอไม่ต้องหยุดรอ

ถัดไป: [ตอนที่ 3 — ส่งผล Sensor กลับมาอัปเดตตาราง](ep10c-sensor-task-result.md)
