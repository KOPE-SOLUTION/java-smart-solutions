# EP 2.5 — Composition และ Value Object

## เป้าหมาย

- ใช้ความสัมพันธ์แบบ has-a
- รวมค่า Sensor ที่เป็นข้อมูลชุดเดียวกัน
- ป้องกันการเปลี่ยนค่าหลังสร้าง Object

Machine มีข้อมูล Sensor หนึ่งชุด จึงแยกข้อมูลนั้นเป็น `SensorReading`

## 1. สร้างโครง Class

สร้างไฟล์ `SensorReading.java`:

```java
public class SensorReading {
    private final double temperature;
    private final double vibration;

    // เพิ่ม Constructor และ Getter ในส่วนถัดไป
}
```

Field เป็น `final` จึงต้องกำหนดค่าผ่าน Constructor และเปลี่ยนภายหลังไม่ได้

## 2. เพิ่ม Constructor

```java
public SensorReading(double temperature, double vibration) {
    if (vibration < 0) {
        throw new IllegalArgumentException("vibration must not be negative");
    }

    this.temperature = temperature;
    this.vibration = vibration;
}
```

## 3. เพิ่ม Getter

```java
public double getTemperature() {
    return temperature;
}

public double getVibration() {
    return vibration;
}
```

## 4. ให้ Machine เก็บ SensorReading

เพิ่ม Field นี้ใน `Machine`:

```java
private SensorReading latestReading;
```

ตอนนี้ Machine **มี** SensorReading จึงเป็นความสัมพันธ์แบบ has-a หรือ Composition

ซอร์สฉบับเต็ม: [`SensorReading.java`](../../src/main/java/smartfactory/model/SensorReading.java)

## Challenge

เพิ่ม `recordedAt` ชนิด `LocalDateTime` แล้วตรวจว่าค่าห้ามเป็น `null`

ถัดไป: [EP 2.6 — Inheritance และ Abstract Class](ep06-inheritance-abstract.md)
