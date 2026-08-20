# EP 2.3 — Encapsulation และ Validation

## เป้าหมาย

- ซ่อน Field ด้วย `private`
- เปิดการแก้ข้อมูลผ่าน Method ที่ควบคุมได้
- ปฏิเสธรหัสเครื่องจักรที่ว่าง

## 1. ปัญหาของ Field ที่แก้ได้โดยตรง

```java
machine.operatingHours = -500;
machine.id = "";
```

โค้ดภายนอกสามารถทำให้ Object มีข้อมูลที่ไม่มีความหมายได้

## 2. ซ่อน Field

เปลี่ยน Field ภายใน `Machine` เป็น:

```java
private final String id;
private String name;
private int operatingHours;
```

เมื่อเป็น `private` โค้ดภายนอกจะเปลี่ยนค่าโดยตรงไม่ได้ และ `final` ทำให้รหัสเปลี่ยนไม่ได้หลัง Constructor ทำงาน

## 3. ตรวจข้อมูลใน Constructor

```java
public Machine(String id, String name) {
    if (id == null || id.isBlank()) {
        throw new IllegalArgumentException("id must not be blank");
    }

    this.id = id.trim();
    this.name = name;
}
```

เริ่มจากตรวจรหัสเพียงค่าเดียวก่อน เพื่อเห็นผลของ Validation ชัดเจน

## 4. เปิดพฤติกรรมที่ปลอดภัย

```java
public void addOperatingHour() {
    operatingHours++;
}

public int getOperatingHours() {
    return operatingHours;
}
```

Encapsulation ไม่ได้หมายความว่าต้องสร้าง Setter ให้ทุก Field ถ้าค่าไม่ควรถูกแก้โดยตรงก็ไม่ควรมี Setter

ซอร์สฉบับเต็ม: [`FactoryDevice.java`](../../src/main/java/smartfactory/model/FactoryDevice.java)

## Challenge

เพิ่ม Validation ให้ชื่อห้ามว่าง แล้วทดลองสร้าง Object ด้วย `"   "`

ถัดไป: [EP 2.4 — Enum และ State](ep04-enum-state.md)
