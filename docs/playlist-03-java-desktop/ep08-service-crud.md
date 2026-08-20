# EP 3.8 — เชื่อม SmartFactoryService และ CRUD

## เป้าหมาย

- ส่ง Service เข้า Frame ผ่าน Constructor
- ให้ UI เรียก Use Case แทนการเก็บ Business Logic เอง
- เพิ่ม อ่าน อัปเดต และลบเครื่องจักร

```java
private final SmartFactoryService service;

public SmartFactoryFrame(SmartFactoryService service) {
    this.service = service;
}
```

เพิ่มเครื่อง:

```java
service.addMachine(new Machine(id, name, location));
refreshDashboard();
```

อัปเดต Sensor:

```java
service.updateSensor(machineId, temperature, vibration);
refreshDashboard();
```

ลบเครื่อง:

```java
service.removeMachine(machineId);
refreshDashboard();
```

ทุก Action จบด้วยการอ่าน state ล่าสุดแล้ว refresh View ลดความเสี่ยงที่ Table จะแสดงข้อมูลไม่ตรงกับ Model

## Challenge

เพิ่มปุ่มค้นหาด้วย ID โดยใช้ `service.findById(...)` และแสดงข้อความเมื่อไม่พบ

ถัดไป: [EP 3.9 — Timer และ UI Thread](ep09-timer-thread.md)

