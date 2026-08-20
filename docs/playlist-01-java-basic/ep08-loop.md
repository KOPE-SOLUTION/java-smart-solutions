# EP 1.8 — for, while และ do-while

## เป้าหมาย

- เลือก Loop ให้เหมาะกับงาน
- ใช้ counter และเงื่อนไขหยุด
- ป้องกัน Infinite Loop

## for: ทราบจำนวนรอบ

```java
public class SensorLoop {
    public static void main(String[] args) {
        for (int round = 1; round <= 5; round++) {
            double temperature = 60.0 + round * 4.5;
            System.out.printf("Round %d: %.1f C%n", round, temperature);
        }
    }
}
```

## while: ทำซ้ำตราบใดที่เงื่อนไขยังจริง

```java
int hours = 480;
while (hours < 500) {
    hours++;
}
System.out.println("Maintenance at " + hours + " hours");
```

## do-while: ทำอย่างน้อยหนึ่งครั้ง

```java
int menu;
do {
    menu = 0; // แทนค่าที่รับจากผู้ใช้
} while (menu != 0);
```

## Challenge

สร้าง Loop แสดงชั่วโมง 100, 200, 300, 400 และ 500 พร้อมพิมพ์ `MAINTENANCE` เฉพาะรอบสุดท้าย

ถัดไป: [EP 1.9 — Array](ep09-array.md)

