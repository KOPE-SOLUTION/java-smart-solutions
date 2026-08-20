# EP 2.4 — Enum และ State ของเครื่องจักร

## เป้าหมาย

- ใช้ Enum แทน String ที่พิมพ์ผิดได้ง่าย
- เก็บข้อมูลเพิ่มเติมใน Enum
- เปลี่ยน state ผ่านกฎของ Object

## ปัญหาของ String

```java
String status = "RUNING"; // สะกดผิดแต่ยัง Compile ผ่าน
```

## Enum

```java
public enum MachineStatus {
    OFFLINE("ปิดเครื่อง"),
    RUNNING("กำลังทำงาน"),
    WARNING("ต้องตรวจสอบ"),
    MAINTENANCE("กำลังบำรุงรักษา");

    private final String displayName;

    MachineStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
```

ใช้งาน:

```java
MachineStatus status = MachineStatus.WARNING;
System.out.println(status.getDisplayName());
```

ซอร์สจริง: [`MachineStatus.java`](../../src/main/java/smartfactory/model/MachineStatus.java)

## Challenge

เพิ่ม `EMERGENCY_STOP` พร้อมข้อความภาษาไทย แล้วปรับกฎให้สถานะนี้เกิดเมื่ออุณหภูมิตั้งแต่ 100 °C

ถัดไป: [EP 2.5 — Composition และ Value Object](ep05-composition-value-object.md)

