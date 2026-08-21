# EP 3.9 — Swing Timer และ UI Thread

## เป้าหมาย

- จำลองค่า Sensor ตามช่วงเวลา
- เริ่มและหยุด Timer จากปุ่มเดียว
- Refresh UI หลัง State เปลี่ยน
- แยกงานสั้นบน EDT ออกจากงาน I/O ที่ใช้เวลานาน

EP นี้แก้สองไฟล์:

1. `SmartFactoryService.java` — เพิ่มการจำลองค่า Sensor
2. `FirstWindow.java` — เพิ่ม Swing Timer และปุ่มควบคุม

## 1. เพิ่ม Random ใน SmartFactoryService.java

เปิด `SmartFactoryService.java` แล้วเพิ่ม Import บนสุดของไฟล์:

```java
import java.util.Random;
```

วาง Method นี้ภายใน Class:

```java
public void simulateSensorReadings(Random random) {
    for (Machine machine : machines) {
        double temperature = 50.0 + random.nextDouble() * 40.0;
        double vibration = 1.0 + random.nextDouble() * 8.0;

        machine.updateReading(
                new SensorReading(temperature, vibration)
        );
    }
}
```

Method นี้เปลี่ยน Model เท่านั้น ไม่แตะ Swing Component จึงยังแยกหน้าที่ของ Service กับ UI ชัดเจน

## 2. เพิ่ม Import ใน FirstWindow.java

วางรวมกับ Import เดิมเหนือ Class:

```java
import java.util.Random;
import javax.swing.Timer;
```

ใช้ `javax.swing.Timer` ไม่ใช่ `java.util.Timer` เพราะ Swing Timer ส่ง Event บน EDT

## 3. สร้างปุ่ม Auto

วางภายใน `createWindow()` บริเวณเดียวกับปุ่มอื่น:

```java
JButton autoButton = new JButton("เริ่มจำลองอัตโนมัติ");
actions.add(autoButton);
```

## 4. สร้าง Random และ Timer

วางหลังสร้าง `refreshAction` จาก EP3.8:

```java
Random random = new Random();

Timer simulationTimer = new Timer(2_000, event -> {
    service.simulateSensorReadings(random);
    refreshAction.run();
});
```

ค่า `2_000` คือ 2,000 milliseconds หรือ 2 วินาที ทุกครั้งที่ Timer ทำงาน Service จะสร้างค่าใหม่ แล้ว UI อ่าน State ล่าสุดมา Refresh

ยังไม่ต้องเรียก `simulationTimer.start()` ตรงนี้ เนื่องจากการเริ่มและหยุดจะควบคุมผ่านปุ่ม

## 5. สลับ Start และ Stop

วาง Listener หลังสร้าง `simulationTimer`:

```java
autoButton.addActionListener(event -> {
    if (simulationTimer.isRunning()) {
        simulationTimer.stop();
        autoButton.setText("เริ่มจำลองอัตโนมัติ");
    } else {
        simulationTimer.start();
        autoButton.setText("หยุดจำลองอัตโนมัติ");
    }
});
```

ใช้ Timer ตัวเดิมตลอดอายุหน้าต่าง อย่าสร้าง `new Timer(...)` ใหม่ทุกครั้งที่กดปุ่ม

## 6. Compile และ Run

```powershell
javac -encoding UTF-8 MachineStatus.java SensorReading.java FactoryDevice.java Maintainable.java Machine.java SmartFactoryService.java FirstWindow.java
java -Dfile.encoding=UTF-8 FirstWindow
```

ทดลองตามลำดับ:

1. กดเริ่มจำลองอัตโนมัติ
2. รออย่างน้อยสองวินาที
3. ตรวจว่าสถานะและ Summary เปลี่ยนตามค่า Sensor
4. กดหยุดและตรวจว่าข้อมูลหยุดเปลี่ยน
5. กดเริ่มอีกครั้งและตรวจว่า Timer เดิมทำงานต่อ

## EDT กับงานที่ใช้เวลานาน

Swing Timer เหมาะกับงานสั้น เช่น สุ่มค่า เปลี่ยน Model และ Refresh Component แต่ไม่ควรใส่ Database Query, HTTP Request หรือ MQTT Receive ที่ต้องรอไว้ใน Event นี้โดยตรง เพราะจะทำให้หน้าต่างค้าง

เมื่อเชื่อม IoT จริง ให้รับข้อมูลบน Background Thread แล้วส่งเฉพาะงานเปลี่ยน UI กลับมาด้วย `SwingUtilities.invokeLater(...)`

## ตรวจความพร้อมก่อนเข้า EP 3.10

- Import `javax.swing.Timer` ถูกต้อง
- Service มี `simulateSensorReadings(Random random)`
- Timer ถูกสร้างเพียงหนึ่งครั้ง
- ปุ่มเดียวสลับ Start และ Stop ได้
- ทุก Tick จบด้วย `refreshAction.run()`
- ปิดหน้าต่างแล้วโปรแกรมจบ

## Challenge

เพิ่ม Label แสดงเวลาที่อัปเดตล่าสุด:

1. สร้าง `JLabel lastUpdatedLabel`
2. เพิ่มลงใน `actions`
3. ภายใน Timer ใช้ `LocalTime.now()` กำหนดข้อความใหม่
4. เพิ่ม `import java.time.LocalTime;`

ถัดไป: [EP 3.10 — ภาษาไทย Packaging และ IoT](ep10-thai-package-iot.md)
