# EP 2.6 — Inheritance และ Abstract Class

## เป้าหมาย

- ใช้ความสัมพันธ์แบบ is-a
- เก็บ Field ร่วมไว้ใน Parent Class
- บังคับ Class ลูกด้วย Abstract Method

## 1. สร้าง Parent Class

สร้างไฟล์ `FactoryDevice.java`:

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

    public String getName() {
        return name;
    }
}
```

`abstract` หมายถึง Class นี้ใช้เป็นแบบกลางและไม่สร้าง Object โดยตรง

## 2. เพิ่ม Abstract Method

วางภายใน `FactoryDevice`:

```java
public abstract String getDeviceType();
```

Class ลูกทุกตัวต้องกำหนดคำตอบของ Method นี้

## 3. ให้ Machine สืบทอด FactoryDevice

ลบ Field `id`, `name` และ `location` ที่ซ้ำออกจาก `Machine` เพราะ Parent Class เป็นผู้เก็บข้อมูลเหล่านี้แล้ว จากนั้นเปลี่ยนส่วนประกาศ Class:

```java
public class Machine extends FactoryDevice {
```

เพิ่ม Constructor:

```java
public Machine(String id, String name, String location) {
    super(id, name, location);
}
```

และกำหนด Method ที่ Parent บังคับไว้:

```java
@Override
public String getDeviceType() {
    return "Machine";
}
```

ใช้ Inheritance เมื่อ Machine **เป็น** FactoryDevice จริง ๆ ไม่ควรใช้เพียงเพื่อลดโค้ดซ้ำ

ซอร์สฉบับเต็ม: [`FactoryDevice.java`](../../src/main/java/smartfactory/model/FactoryDevice.java)

## Challenge

สร้าง `EnergyMeter extends FactoryDevice` และ Override `getDeviceType()`

ถัดไป: [EP 2.7 — Interface](ep07-interface.md)
