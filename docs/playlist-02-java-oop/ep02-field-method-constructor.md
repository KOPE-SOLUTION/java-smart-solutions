# EP 2.2 — Field, Method และ Constructor

## เป้าหมาย

- แยก state กับ behavior
- กำหนดข้อมูลที่จำเป็นผ่าน Constructor
- ใช้ `this` อ้างถึง Object ปัจจุบัน

```java
public class Machine {
    String id;
    String name;
    double temperature;

    public Machine(String id, String name) {
        this.id = id;
        this.name = name;
        this.temperature = 0;
    }

    public void updateTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getSummary() {
        return id + " | " + name + " | " + temperature + " C";
    }
}
```

ใช้งาน:

```java
Machine mixer = new Machine("M-001", "Mixer");
mixer.updateTemperature(65.5);
System.out.println(mixer.getSummary());
```

Constructor ไม่มี return type และถูกเรียกตอน `new` ส่วน Method แสดงพฤติกรรมหลัง Object ถูกสร้างแล้ว

## Challenge

เพิ่ม Constructor อีกแบบที่รับอุณหภูมิเริ่มต้น และใช้ `this(id, name)` ลดโค้ดซ้ำ

ถัดไป: [EP 2.3 — Encapsulation และ Validation](ep03-encapsulation-validation.md)

