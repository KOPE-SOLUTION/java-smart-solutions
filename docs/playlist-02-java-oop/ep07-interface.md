# EP 2.7 — Interface และสัญญาการทำงาน

## เป้าหมาย

- แยกความสามารถออกจากลำดับ Inheritance
- ใช้ `implements` และ `@Override`
- ออกแบบ Contract ที่หลาย Class ใช้ร่วมกัน

```java
public interface Maintainable {
    boolean requiresMaintenance();
    void performMaintenance();
}
```

```java
public class Machine extends FactoryDevice implements Maintainable {
    @Override
    public boolean requiresMaintenance() {
        return operatingHours >= 500 || status == MachineStatus.WARNING;
    }

    @Override
    public void performMaintenance() {
        operatingHours = 0;
        status = MachineStatus.OFFLINE;
    }
}
```

`Machine` และ `EnergyMeter` อาจสืบทอดคนละ Parent Class แต่ยัง implement `Maintainable` เหมือนกันได้

ซอร์สจริง: [`Maintainable.java`](../../src/main/java/smartfactory/model/Maintainable.java)

## Challenge

สร้าง Interface `SensorReadable` ที่มี method `updateReading(SensorReading reading)`

ถัดไป: [EP 2.8 — Polymorphism](ep08-polymorphism.md)

