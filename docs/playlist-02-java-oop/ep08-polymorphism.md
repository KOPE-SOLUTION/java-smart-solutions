# EP 2.8 — Polymorphism

## เป้าหมาย

- อ้างถึง Object ผ่าน Parent Type หรือ Interface
- เรียก Method ของ Object จริงที่ Runtime
- ใช้ Method เดียวกับอุปกรณ์หลายชนิด

## 1. มอง Machine ผ่าน Type อื่น

```java
Machine machine = new Machine("M-001", "Mixer", "Line A");

FactoryDevice device = machine;
Maintainable maintainable = machine;
```

ตัวแปรทั้งสามอ้างถึง Object เดียวกัน แต่เปิดให้เรียก Method ตาม Type ของตัวแปร

ทดลองเรียก:

```java
System.out.println(device.getDeviceType());
System.out.println(maintainable.requiresMaintenance());
```

## 2. สร้าง Method ที่รับ Parent Type

```java
private static void printDevice(FactoryDevice device) {
    System.out.println(device.getDeviceType() + " | " + device.getName());
}
```

Method นี้รับได้ทั้ง `Machine` และ Class อื่นที่สืบทอด `FactoryDevice` Java จะเลือก Implementation ของ Object จริงขณะ Runtime

ดูตัวอย่างทำงาน: [`OopDemo.java`](../../src/main/java/smartfactory/oop/OopDemo.java)

## Challenge

สร้าง Object `EnergyMeter` แล้วส่งเข้า `printDevice(...)` Method เดียวกับ Machine

ถัดไป: [EP 2.9 — Collection, Optional และ Stream](ep09-collection-optional-stream.md)
