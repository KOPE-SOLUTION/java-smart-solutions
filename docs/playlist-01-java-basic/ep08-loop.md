# EP 1.8 — for, while และ do-while

## เป้าหมาย

- ใช้ `for` เมื่อต้องการจำนวนรอบชัดเจน
- ใช้ `while` เมื่อต้องตรวจเงื่อนไขก่อนทำ
- ใช้ `do-while` เมื่อต้องทำอย่างน้อยหนึ่งครั้ง

## สร้างไฟล์ทดลอง

สร้างไฟล์ `SensorLoop.java` แล้ววาง Loop ทีละแบบใน `main` อย่าวางพร้อมกันทั้งหมดในครั้งแรก

```java
public class SensorLoop {
    public static void main(String[] args) {
        // วาง Loop ที่ต้องการทดลองตรงนี้
    }
}
```

## 1. `for`: ทราบจำนวนรอบ

```java
for (int round = 1; round <= 5; round++) {
    System.out.println("Sensor round: " + round);
}
```

รันแล้วสังเกตว่าค่า `round` เปลี่ยนจาก 1 ถึง 5

## 2. `while`: ทำตราบใดที่เงื่อนไขยังจริง

ลบ Loop เดิมออกก่อน แล้วทดลอง:

```java
int hours = 480;

while (hours < 500) {
    hours++;
}

System.out.println("Maintenance at " + hours + " hours");
```

อย่าลืมเปลี่ยนค่าที่อยู่ในเงื่อนไข มิฉะนั้นอาจเกิด Infinite Loop

## 3. `do-while`: ทำอย่างน้อยหนึ่งครั้ง

```java
int menu;

do {
    menu = 0;
    System.out.println("Menu opened");
} while (menu != 0);
```

แม้ `menu` จะเป็น `0` โปรแกรมยังพิมพ์ข้อความหนึ่งครั้ง เพราะตรวจเงื่อนไขหลังจบคำสั่งใน `do`

## Challenge

สร้าง Loop แสดงชั่วโมง 100, 200, 300, 400 และ 500 พร้อมพิมพ์ `MAINTENANCE` เฉพาะรอบสุดท้าย

ถัดไป: [EP 1.9 — Array](ep09-array.md)
