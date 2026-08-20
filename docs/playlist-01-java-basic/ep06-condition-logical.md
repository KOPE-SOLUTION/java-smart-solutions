# EP 1.6 — if/else และ Logical Operator

## เป้าหมาย

- สร้างเงื่อนไขหลายระดับ
- ใช้ AND, OR และ NOT
- แปลงกฎ Smart Factory เป็นโค้ด

## กติกา

- อุณหภูมิตั้งแต่ 100 °C: `EMERGENCY`
- อุณหภูมิตั้งแต่ 80 °C หรือแรงสั่นตั้งแต่ 7 mm/s: `WARNING`
- นอกนั้น: `RUNNING`

## สร้างไฟล์ `MachineAlert.java`

```java
public class MachineAlert {
    public static void main(String[] args) {
        double temperature = 82.3;
        double vibration = 6.2;
        String status;

        if (temperature >= 100.0) {
            status = "EMERGENCY";
        } else if (temperature >= 80.0 || vibration >= 7.0) {
            status = "WARNING";
        } else {
            status = "RUNNING";
        }

        System.out.println("Status: " + status);
    }
}
```

Logical Operator:

- `&&` ทุกเงื่อนไขต้องจริง
- `||` เงื่อนไขใดจริงก็ได้
- `!` กลับค่าจริงเป็นเท็จ

## Challenge

เพิ่มตัวแปร `online` ถ้าเครื่องไม่ออนไลน์ให้แสดง `OFFLINE` ก่อนตรวจ Sensor เสมอ

ถัดไป: [EP 1.7 — switch และเมนู](ep07-switch-menu.md)

