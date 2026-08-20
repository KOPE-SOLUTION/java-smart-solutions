# EP 2.5 — Composition และ Value Object

## เป้าหมาย

- ใช้ความสัมพันธ์แบบ has-a
- รวมค่าที่เปลี่ยนพร้อมกันไว้ใน Value Object
- ทำข้อมูล Sensor ให้ immutable

Machine มีค่าจาก Sensor หนึ่งชุด จึงใช้ Composition:

```java
public class SensorReading {
    private final double temperature;
    private final double vibration;

    public SensorReading(double temperature, double vibration) {
        if (vibration < 0) {
            throw new IllegalArgumentException("vibration must not be negative");
        }
        this.temperature = temperature;
        this.vibration = vibration;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getVibration() {
        return vibration;
    }
}
```

Machine เก็บ Object นี้แทน Field ที่กระจัดกระจาย:

```java
private SensorReading latestReading;
```

ซอร์สจริง: [`SensorReading.java`](../../src/main/java/smartfactory/model/SensorReading.java)

## Challenge

เพิ่ม `recordedAt` ชนิด `LocalDateTime` และตรวจว่าเวลาห้ามเป็น `null`

ถัดไป: [EP 2.6 — Inheritance และ Abstract Class](ep06-inheritance-abstract.md)

