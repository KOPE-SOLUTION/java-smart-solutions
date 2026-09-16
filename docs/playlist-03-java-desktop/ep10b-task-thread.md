# EP 3.10 ตอนที่ 2 — ทดลองงานเบื้องหลังด้วย Task และ Thread

เป้าหมาย: กดปุ่มแล้วรอผล 3 วินาที แต่หน้าจอยังตอบสนอง ตอนนี้ยังไม่อัปเดต Sensor

ใช้โปรเจกต์จาก [ตอนที่ 1](ep10a-sensor-button.md) ต่อ ปิดโปรแกรมแล้วแก้เฉพาะ `practice/smart-factory-dashboard/src/main/java/smartfactory/desktop/DashboardApp.java` ไม่ต้องสร้างไฟล์ใหม่

**Task คือชุดงาน ส่วน Thread คือตัวรันงานแยกจากหน้าจอ** ตอนนี้ทดลองเฉพาะทางสำเร็จ ส่วนการป้องกันงานซ้อนและข้อผิดพลาดอยู่ตอนที่ 3 การหยุดงานเมื่อปิดหน้าต่างอยู่ตอนที่ 4

## 1. สร้างงาน รับผล แล้วเริ่ม Thread

เพิ่ม Import ต่อจาก Import เดิม และลบ `import java.util.Random;` ที่ไม่ได้ใช้แล้ว:

```java
import javafx.concurrent.Task;
```

แทนที่ `simulateOnce()` ทั้ง Method ซึ่งอยู่หลังปีกกาปิดของ `refreshDashboard()` ด้วย:

```java
private void runBackgroundDemo() {
    statusLabel.setText("กำลังทำงานเบื้องหลัง...");

    Task<String> task = new Task<>() {
        @Override
        protected String call() throws Exception {
            Thread.sleep(3000);
            return "งานเบื้องหลังเสร็จแล้ว";
        }
    };

    task.setOnSucceeded(event -> {
        statusLabel.setText(task.getValue());
    });

    Thread worker = new Thread(task, "sensor-worker");
    worker.start();
}
```

อ่านโค้ดเป็นสามส่วน:

- **สร้างงาน:** `Task<String>` ส่งผลเป็นข้อความ ส่วน `call()` คืองานที่รอ 3 วินาทีแล้วคืนข้อความ ไม่แก้หน้าจอใน Method นี้
- **รับผล:** `setOnSucceeded` รอให้งานสำเร็จ แล้วรับผลด้วย `getValue()` ตัวรับเหตุการณ์นี้อยู่บน JavaFX Application Thread จึงเปลี่ยน Label ได้
- **เริ่มงาน:** `worker.start()` เริ่ม Thread ใหม่ให้รัน Task ไม่ต้องเรียก `call()` เอง

`new Task<>() { ... }` สร้างคลาสลูกแบบไม่ตั้งชื่อ (Anonymous Class) แล้ว Override `call()` ตามหลัก OOP เหมือนที่เคยใช้กับ `TableCell` ใน EP3.8 ส่วน `task` ตัวเล็กเป็นชื่อตัวแปรที่อ้างถึง Object ไม่ใช่ชื่อคลาสลูก

## 2. ให้ปุ่มเรียกงานทดลอง

ใน `buildMachineForm()` แทนที่ `sensorButton.setOnAction(...)` เดิมด้วย:

```java
sensorButton.setText("ทดสอบงานเบื้องหลัง");
sensorButton.setOnAction(event -> runBackgroundDemo());
```

เก็บ `actionButtons.getChildren().add(sensorButton);` ไว้ครั้งเดียว

## 3. รันและตรวจผล

บันทึกไฟล์แล้วรันจากโฟลเดอร์หลักของ Repository:

```powershell
.\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run
```

- กดปุ่มครั้งเดียว ระหว่างรอลองพิมพ์ชื่อหรือเลือกแถว หน้าจอต้องยังตอบสนอง
- ประมาณ 3 วินาทีต่อมา Label แสดงว่างานเสร็จ ค่า Sensor และชั่วโมงไม่เปลี่ยน
- รอผลก่อนกดอีกครั้ง แต่ละครั้งจะสร้าง Task ใหม่ เพราะ Task หนึ่งตัวใช้รันได้ครั้งเดียว

นี่เป็นตัวอย่างทดลองพื้นฐาน ยังไม่มีการป้องกันกดซ้ำ การรับข้อผิดพลาด หรือการยกเลิกงาน ให้รองานจบก่อนปิดหน้าต่าง หากปิดก่อน งานสั้นนี้อาจยังทำต่อจนครบเวลา

สรุป: งานไม่ได้เร็วขึ้น แต่ Thread ของหน้าจอไม่ต้องหยุดรอ

ถัดไป: [ตอนที่ 3 — ส่งผล Sensor กลับมาอัปเดตตาราง](ep10c-sensor-task-result.md)
