# EP 2.7 — Interface และสัญญาการทำงาน

## เป้าหมาย

- กำหนดความสามารถด้วย Interface
- ใช้ `implements` และ `@Override`
- ให้หลาย Class ใช้สัญญาเดียวกัน

## 1. สร้าง Interface

สร้างไฟล์ `Maintainable.java`:

```java
public interface Maintainable {
    boolean requiresMaintenance();
    void performMaintenance();
}
```

Interface ระบุว่า Object ต้องทำอะไร แต่ยังไม่กำหนดรายละเอียดการทำงาน

## 2. ให้ Machine ใช้ Interface

แก้ส่วนประกาศ Class:

```java
public class Machine extends FactoryDevice implements Maintainable {
```

เพิ่มสถานะเริ่มต้นใน `Machine` ถ้ายังไม่มี Field นี้:

```java
private MachineStatus status = MachineStatus.OFFLINE;
```

## 3. กำหนดรายละเอียดของแต่ละ Method

วางภายใน `Machine`:

```java
@Override
public boolean requiresMaintenance() {
    return operatingHours >= 500 || status == MachineStatus.WARNING;
}
```

เพิ่ม Method สำหรับบำรุงรักษา:

```java
@Override
public void performMaintenance() {
    operatingHours = 0;
    status = MachineStatus.OFFLINE;
}
```

Machine และอุปกรณ์ชนิดอื่นอาจสืบทอด Parent ต่างกัน แต่ยัง `implements Maintainable` เหมือนกันได้

ซอร์สฉบับเต็ม: [`Maintainable.java`](../../src/main/java/smartfactory/model/Maintainable.java)

## Challenge

สร้าง Interface `SensorReadable` ที่มี Method `updateReading(SensorReading reading)`

ถัดไป: [EP 2.8 — Polymorphism](ep08-polymorphism.md)
