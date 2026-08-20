# EP 2.10 — Service Layer, Exception และ Test

## เป้าหมาย

- แยก Use Case ออกจาก Model และ UI
- ปฏิเสธรหัสเครื่องจักรซ้ำ
- รัน Test เพื่อตรวจ Business Rule

## 1. หน้าที่ของ Service

[`SmartFactoryService.java`](../../src/main/java/smartfactory/service/SmartFactoryService.java) เป็นจุดรวม Use Case เช่น เพิ่ม ลบ ค้นหา และอัปเดต Sensor

เริ่มจาก Method เพิ่มเครื่อง:

```java
public void addMachine(Machine machine) {
    machines.add(machine);
}
```

Model เป็นเจ้าของกฎของ Object ส่วน Service ประสานการทำงานของหลาย Object

## 2. ป้องกันรหัสซ้ำ

เพิ่มเงื่อนไขก่อน `machines.add(...)`:

```java
if (findById(machine.getId()).isPresent()) {
    throw new IllegalArgumentException("รหัสเครื่องจักรซ้ำ: " + machine.getId());
}
```

ตอนนี้ Service จะหยุดและแจ้งข้อผิดพลาดก่อนเพิ่มข้อมูลซ้ำ

## 3. รัน Test

[`SmartFactoryTest.java`](../../src/test/java/smartfactory/SmartFactoryTest.java) ตรวจกรณีหลัก เช่น ค่าปลอดภัย อุณหภูมิสูง การบำรุงรักษา รหัสซ้ำ และภาษาไทยใน Swing

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\test.ps1
```

ผลลัพธ์ที่ถูกต้อง:

```text
Encoding check passed: ... UTF-8 files
Build completed: ...\out
PASS: 5 tests
```

## Final Challenge

เพิ่ม `EMERGENCY_STOP` เมื่ออุณหภูมิตั้งแต่ 100 °C แล้วเพิ่ม Test ทีละค่า: `99.9`, `100.0` และ `100.1`

ถัดไป: [Playlist 3 — Java Desktop Workshop](../playlist-03-java-desktop/README.md)
