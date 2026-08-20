# EP 2.3 — Encapsulation และ Validation

## เป้าหมาย

- ซ่อน Field ด้วย `private`
- เปิดเฉพาะพฤติกรรมที่ปลอดภัย
- ป้องกัน Object ที่ข้อมูลว่างหรือค่าติดลบ

## ปัญหาของ Public Field

```java
machine.operatingHours = -500;
machine.id = "";
```

โค้ดภายนอกสามารถทำให้ Object อยู่ใน state ที่ไม่มีความหมายได้

## Encapsulated Class

```java
public class Machine {
    private final String id;
    private String name;
    private int operatingHours;

    public Machine(String id, String name) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        this.id = id.trim();
        this.name = name;
    }

    public void addOperatingHour() {
        operatingHours++;
    }

    public int getOperatingHours() {
        return operatingHours;
    }
}
```

Encapsulation ไม่ได้แปลว่าต้องสร้าง setter ทุก Field ถ้าค่าไม่ควรถูกแก้โดยตรงก็ไม่ควรมี setter

ซอร์สจริง: [`FactoryDevice.java`](../../src/main/java/smartfactory/model/FactoryDevice.java)

## Challenge

เพิ่ม Validation ให้ชื่อและตำแหน่งห้ามว่าง พร้อมทดสอบสร้าง Object ด้วยช่องว่าง

ถัดไป: [EP 2.4 — Enum และ State](ep04-enum-state.md)

