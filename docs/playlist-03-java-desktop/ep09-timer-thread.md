# EP 3.9 — Swing Timer และ UI Thread

## เป้าหมาย

- อัปเดต Dashboard ตามเวลา
- เริ่มและหยุด Timer จากปุ่มเดียว
- แยกงาน UI ออกจากงาน I/O ที่ใช้เวลานาน

## 1. สร้าง Timer

```java
Timer simulationTimer = new Timer(2_000, event -> simulateOnce());
```

ค่า `2_000` คือ 2,000 milliseconds หรือ 2 วินาที

## 2. แยกงานที่ทำแต่ละรอบ

```java
private void simulateOnce() {
    service.simulateSensorReadings(random);
    refreshDashboard();
}
```

เริ่ม Timer เพื่อทดลอง:

```java
simulationTimer.start();
```

## 3. สลับ Start และ Stop

วางใน Event Handler ของปุ่ม Auto:

```java
if (simulationTimer.isRunning()) {
    simulationTimer.stop();
    autoButton.setText("เริ่มจำลองอัตโนมัติ");
} else {
    simulationTimer.start();
    autoButton.setText("หยุดจำลองอัตโนมัติ");
}
```

Swing Timer ส่ง Event บน EDT จึงเหมาะกับงานสั้นที่แก้ UI งานอ่าน Database, HTTP หรือ MQTT ที่ต้องรอควรทำบน Background Thread

## Challenge

เพิ่ม Label แสดงเวลาที่อัปเดตล่าสุด แล้วกำหนดค่าใหม่ภายใน `simulateOnce()`

ถัดไป: [EP 3.10 — ภาษาไทย Packaging และ IoT](ep10-thai-package-iot.md)
