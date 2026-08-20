# EP 3.8 — เชื่อม SmartFactoryService และ CRUD

## เป้าหมาย

- ส่ง Service เข้า Frame ผ่าน Constructor
- ให้ UI เรียก Use Case แทนการเก็บ Business Logic
- Refresh หน้าจอหลังข้อมูลเปลี่ยน

## 1. รับ Service ผ่าน Constructor

เพิ่ม Field ใน Frame:

```java
private final SmartFactoryService service;
```

เพิ่ม Parameter ใน Constructor:

```java
public SmartFactoryFrame(SmartFactoryService service) {
    this.service = service;
}
```

## 2. เพิ่มเครื่องจักร

ใน Event Handler ของปุ่ม Add:

```java
Machine machine = new Machine(id, name, location);
service.addMachine(machine);
refreshDashboard();
```

รันและตรวจว่าตารางมีแถวใหม่ก่อนเพิ่ม Action อื่น

## 3. อัปเดต Sensor

```java
service.updateSensor(machineId, temperature, vibration);
refreshDashboard();
```

## 4. ลบเครื่องจักร

```java
service.removeMachine(machineId);
refreshDashboard();
```

ทุก Action จบด้วยการอ่าน State ล่าสุดและ Refresh View ทำให้ตารางตรงกับข้อมูลใน Service

## Challenge

เพิ่มปุ่มค้นหาด้วย ID โดยใช้ `service.findById(...)` และแสดงข้อความเมื่อไม่พบ

ถัดไป: [EP 3.9 — Timer และ UI Thread](ep09-timer-thread.md)
