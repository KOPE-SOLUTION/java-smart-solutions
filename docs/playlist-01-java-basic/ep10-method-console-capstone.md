# EP 1.10 — Method และ Smart Factory Console Capstone

## เป้าหมาย

- แยกโปรแกรมเป็น method ที่มีหน้าที่เดียว
- ใช้ parameter และ return value
- รวม Basic ทั้ง Playlist เป็น Mini Project

## Method ตรวจสถานะ

```java
private static String checkStatus(double temperature, double vibration) {
    if (temperature >= 80.0 || vibration >= 7.0) {
        return "WARNING";
    }
    return "RUNNING";
}
```

## Method แสดงหนึ่งแถว

```java
private static void printMachine(
        int number,
        String name,
        double temperature,
        double vibration
) {
    String status = checkStatus(temperature, vibration);
    System.out.printf("%d. %-12s %5.1f C %4.1f mm/s %s%n",
            number, name, temperature, vibration, status);
}
```

## โค้ด Capstone ใน Repository

เปิดและคัดลอกไฟล์ [`BasicDemo.java`](../../src/main/java/smartfactory/basic/BasicDemo.java) ซึ่งรวม Variable, Array, Loop, Method, Condition, Switch และ Scanner ไว้ในโปรแกรมที่รันได้

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run-basic.ps1
```

ผลลัพธ์ส่วนหนึ่ง:

```text
1. Mixer        อุณหภูมิ  65.5 °C -> NORMAL
2. Conveyor     อุณหภูมิ  82.3 °C -> WARNING
3. Water Pump   อุณหภูมิ  58.0 °C -> NORMAL
```

## Final Challenge

1. เพิ่ม Array แรงสั่นสะเทือน
2. เปลี่ยน `checkTemperature` เป็น `checkStatus`
3. แจ้งเตือนเมื่ออุณหภูมิตั้งแต่ 80 หรือแรงสั่นตั้งแต่ 7
4. เพิ่มเมนูให้ผู้ใช้เลือกดูเครื่องจักร
5. ป้องกันหมายเลขที่อยู่นอกช่วง

Playlist ถัดไปจะแก้ข้อจำกัดของ Array และ Static Method ด้วย Class และ Object

ถัดไป: [Playlist 2 — Java OOP in Action](../playlist-02-java-oop/README.md)

