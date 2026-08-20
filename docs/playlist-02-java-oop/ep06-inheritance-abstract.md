# EP 2.6 — Inheritance และ Abstract Class

## เป้าหมาย

- ใช้ความสัมพันธ์แบบ is-a
- ดึง Field ร่วมไว้ใน Parent Class
- บังคับชนิดลูกด้วย Abstract Method

```java
public abstract class FactoryDevice {
    private final String id;
    private String name;
    private String location;

    protected FactoryDevice(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public abstract String getDeviceType();
}
```

```java
public class Machine extends FactoryDevice {
    public Machine(String id, String name, String location) {
        super(id, name, location);
    }

    @Override
    public String getDeviceType() {
        return "Machine";
    }
}
```

ใช้ Inheritance เมื่อ Machine **เป็น** FactoryDevice และต้องการสัญญาร่วมจริง ๆ ไม่ควรใช้เพียงเพื่อลดการเขียนโค้ดซ้ำ

ซอร์สจริง: [`FactoryDevice.java`](../../src/main/java/smartfactory/model/FactoryDevice.java)

## Challenge

สร้าง `EnergyMeter extends FactoryDevice` และ override `getDeviceType()`

ถัดไป: [EP 2.7 — Interface](ep07-interface.md)

