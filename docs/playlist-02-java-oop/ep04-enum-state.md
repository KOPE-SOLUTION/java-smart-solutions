# EP 2.4 — Enum และ State ของเครื่องจักร

## เป้าหมาย

- ใช้ Enum แทน String ที่สะกดผิดได้
- เลือกสถานะจากค่าที่กำหนดไว้
- เพิ่มข้อความสำหรับแสดงผลใน Enum

## 1. ปัญหาของ String

```java
String status = "RUNING";
```

คำว่า `RUNING` สะกดผิดแต่ยัง Compile ผ่าน เพราะ Java มองเป็นข้อความทั่วไป

## 2. สร้าง Enum

สร้างไฟล์ `MachineStatus.java`:

```java
public enum MachineStatus {
    OFFLINE,
    RUNNING,
    WARNING,
    MAINTENANCE
}
```

ทดลองใช้:

```java
MachineStatus status = MachineStatus.WARNING;
System.out.println(status);
```

Java จะไม่ยอม Compile ถ้าเลือกชื่อสถานะที่ไม่มีอยู่ใน Enum

## 3. เพิ่มข้อความสำหรับแสดงผล

เพิ่มข้อความหลังสถานะแต่ละค่า:

```java
OFFLINE("ปิดเครื่อง"),
RUNNING("กำลังทำงาน"),
WARNING("Sensor ผิดปกติ"),
MAINTENANCE("กำลังบำรุงรักษา");
```

`WARNING` ใช้บอกผลจากค่า Sensor เท่านั้น ส่วนการครบกำหนดบำรุงรักษาจะตรวจแยกด้วย `requiresMaintenance()` ใน EP 2.7

จากนั้นเพิ่ม Field, Constructor และ Getter ภายใน Enum:

```java
private final String displayName;

MachineStatus(String displayName) {
    this.displayName = displayName;
}

public String getDisplayName() {
    return displayName;
}
```

เรียกใช้งาน:

```java
System.out.println(status.getDisplayName());
```

ซอร์สฉบับเต็ม: [`MachineStatus.java`](../../src/main/java/smartfactory/model/MachineStatus.java)

## Challenge

เพิ่ม `EMERGENCY_STOP` พร้อมข้อความภาษาไทย

ถัดไป: [EP 2.5 — Composition และ Value Object](ep05-composition-value-object.md)
