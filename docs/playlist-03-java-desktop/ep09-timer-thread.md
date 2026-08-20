# EP 3.9 — Swing Timer และ UI Thread

## เป้าหมาย

- อัปเดต Dashboard ตามเวลา
- เริ่มและหยุด Timer
- แยกงานสั้นบน EDT ออกจากงาน I/O ที่ใช้เวลานาน

```java
Timer simulationTimer = new Timer(2_000, event -> {
    service.simulateSensorReadings(random);
    refreshDashboard();
});
```

```java
if (simulationTimer.isRunning()) {
    simulationTimer.stop();
    autoButton.setText("เริ่มจำลองอัตโนมัติ");
} else {
    simulationTimer.start();
    autoButton.setText("หยุดจำลองอัตโนมัติ");
}
```

Swing Timer ส่ง Event บน EDT เหมาะกับงานสั้นที่แก้ UI งานอ่าน Database, HTTP หรือ MQTT ที่รอนานควรทำบน background thread แล้วกลับมาอัปเดต Swing ด้วย `SwingUtilities.invokeLater`

## Challenge

เพิ่ม Label แสดงเวลาที่อัปเดตล่าสุด และ ComboBox ให้เลือกช่วง 1, 2 หรือ 5 วินาที

ถัดไป: [EP 3.10 — ภาษาไทย Packaging และ IoT](ep10-thai-package-iot.md)

