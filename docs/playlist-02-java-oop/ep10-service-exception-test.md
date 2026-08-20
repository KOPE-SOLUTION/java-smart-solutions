# EP 2.10 — Service Layer, Exception และ Test

## เป้าหมาย

- แยก Use Case ออกจาก Model และ UI
- จัดการกรณีไม่พบหรือรหัสซ้ำ
- ตรวจ Business Rule แบบอัตโนมัติ

## Service Layer

[`SmartFactoryService.java`](../../src/main/java/smartfactory/service/SmartFactoryService.java) รับผิดชอบเพิ่ม ลบ ค้นหา อัปเดต Sensor และสรุปผลหลาย Machine

```java
public void addMachine(Machine machine) {
    if (findById(machine.getId()).isPresent()) {
        throw new IllegalArgumentException("รหัสเครื่องจักรซ้ำ: " + machine.getId());
    }
    machines.add(machine);
}
```

Model เป็นเจ้าของกฎของ Object ส่วน Service เป็นเจ้าของ Use Case ที่ประสานหลาย Object

## Test

[`SmartFactoryTest.java`](../../src/test/java/smartfactory/SmartFactoryTest.java) ตรวจห้ากรณี:

1. ค่าปลอดภัยต้องเป็น `RUNNING`
2. อุณหภูมิสูงต้องเป็น `WARNING`
3. Maintenance ต้อง reset ชั่วโมง
4. รหัสซ้ำต้องถูกปฏิเสธ
5. Swing Font และข้อความ Popup ต้องรองรับภาษาไทย

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\test.ps1
```

ผลลัพธ์:

```text
Encoding check passed: ... UTF-8 files
Build completed: ...\out
PASS: 5 tests
```

## Final Challenge

เพิ่ม `EMERGENCY_STOP` เมื่ออุณหภูมิตั้งแต่ 100 °C แล้วเพิ่ม Test อย่างน้อยสามค่า: 99.9, 100.0 และ 100.1

ถัดไป: [Playlist 3 — Java Desktop Workshop](../playlist-03-java-desktop/README.md)

