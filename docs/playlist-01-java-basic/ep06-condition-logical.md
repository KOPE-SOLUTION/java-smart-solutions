# EP 1.6 — if/else และ Logical Operator

## เป้าหมาย

- ตัดสินใจด้วย `if`, `else if` และ `else`
- ใช้ OR (`||`) เชื่อมสองเงื่อนไข
- แปลงกฎ Smart Factory เป็นโค้ด

## กติกา

- อุณหภูมิตั้งแต่ 100 °C: `EMERGENCY`
- อุณหภูมิตั้งแต่ 80 °C หรือแรงสั่นตั้งแต่ 7 mm/s: `WARNING`
- นอกนั้น: `RUNNING`

## 1. สร้างโครงไฟล์และข้อมูล

สร้างไฟล์ `MachineAlert.java`:

```java
public class MachineAlert {
    public static void main(String[] args) {
        double temperature = 82.3;
        double vibration = 6.2;
        String status;

        // เพิ่มเงื่อนไขตรงนี้
    }
}
```

## 2. ตรวจเงื่อนไขแรก

วางแทน Comment ภายใน `main`:

```java
if (temperature >= 100.0) {
    status = "EMERGENCY";
} else {
    status = "RUNNING";
}

System.out.println("Status: " + status);
```

รันครั้งแรกให้เห็น `RUNNING` ก่อน แล้วลองเปลี่ยนอุณหภูมิเป็น `105.0`

## 3. เพิ่มเงื่อนไข WARNING

แทนที่ชุด `if/else` เดิมด้วย:

```java
if (temperature >= 100.0) {
    status = "EMERGENCY";
} else if (temperature >= 80.0 || vibration >= 7.0) {
    status = "WARNING";
} else {
    status = "RUNNING";
}
```

`||` หมายถึงอย่างน้อยหนึ่งเงื่อนไขต้องเป็นจริง ส่วน `&&` หมายถึงทุกเงื่อนไขต้องเป็นจริง และ `!` ใช้กลับค่า `true` กับ `false`

## Challenge

เพิ่มตัวแปร `online` ถ้าเครื่องไม่ออนไลน์ให้แสดง `OFFLINE` ก่อนตรวจ Sensor เสมอ

ถัดไป: [EP 1.7 — switch และเมนู](ep07-switch-menu.md)
