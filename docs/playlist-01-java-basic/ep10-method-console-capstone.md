# EP 1.10 — Method และ Smart Factory Console Capstone

## เป้าหมาย

- แยกโปรแกรมเป็น Method ที่มีหน้าที่เดียว
- ส่งข้อมูลเข้า Method ด้วย Parameter
- รับผลลัพธ์กลับด้วย `return`

บทนี้เริ่มจาก Method สั้นสองตัว ก่อนเปิดโปรแกรม Capstone ฉบับเต็มใน Repository

## 1. Method ที่มี Return Value

วาง Method นี้ภายใน Class แต่วางไว้นอก `main`:

```java
private static String checkStatus(double temperature, double vibration) {
    if (temperature >= 80.0 || vibration >= 7.0) {
        return "WARNING";
    }

    return "RUNNING";
}
```

ทดลองเรียกภายใน `main`:

```java
String status = checkStatus(82.3, 6.2);
System.out.println("Status: " + status);
```

## 2. Method ที่ไม่ Return ค่า

เพิ่ม Method นี้ไว้นอก `main`:

```java
private static void printMachine(String name, double temperature, double vibration) {
    String status = checkStatus(temperature, vibration);
    System.out.println(name + " | " + temperature + " C | " + vibration + " mm/s | " + status);
}
```

แล้วเรียกจาก `main`:

```java
printMachine("Mixer", 65.5, 2.3);
printMachine("Conveyor", 82.3, 6.2);
```

## 3. เปิด Capstone ฉบับเต็ม

เปิดไฟล์ [`BasicDemo.java`](../../src/main/java/smartfactory/basic/BasicDemo.java) หลังจากเข้าใจสอง Method ด้านบนแล้ว ไฟล์นี้รวม Variable, Array, Loop, Method, Condition, Switch และ Scanner ไว้เป็นโปรแกรมเดียว

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

ทำทีละข้อและรันหลังแก้แต่ละข้อ:

1. เพิ่ม Array แรงสั่นสะเทือน
2. เปลี่ยน `checkTemperature` เป็น `checkStatus`
3. แจ้งเตือนเมื่ออุณหภูมิตั้งแต่ 80 หรือแรงสั่นตั้งแต่ 7
4. เพิ่มเมนูให้ผู้ใช้เลือกดูเครื่องจักร
5. ป้องกันหมายเลขที่อยู่นอกช่วง

Playlist ถัดไปจะแก้ข้อจำกัดของ Array และ Static Method ด้วย Class และ Object

ถัดไป: [Playlist 2 — Java OOP in Action](../playlist-02-java-oop/README.md)
