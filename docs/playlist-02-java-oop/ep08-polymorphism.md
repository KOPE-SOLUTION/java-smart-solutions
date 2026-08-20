# EP 2.8 — Polymorphism

## เป้าหมาย

- มอง Object ผ่าน Parent Type หรือ Interface
- เรียก method ของ Object จริงที่ Runtime
- ลด `if` ตรวจชนิดที่ไม่จำเป็น

```java
Machine machine = new Machine("M-001", "Mixer", "Line A");

FactoryDevice device = machine;
Maintainable maintainable = machine;

System.out.println(device.getDeviceType());
System.out.println(maintainable.requiresMaintenance());
```

Method ที่รับชนิดกลาง:

```java
private static void printDevice(FactoryDevice device) {
    System.out.println(device.getDeviceType() + " | " + device.getName());
}
```

เมื่อส่ง `Machine` หรือ `EnergyMeter` เข้า method เดียวกัน Java จะเลือก implementation ของ Object จริง

ดูตัวอย่างทำงาน: [`OopDemo.java`](../../src/main/java/smartfactory/oop/OopDemo.java)

## Challenge

สร้าง `List<FactoryDevice>` ที่มี Machine และ EnergyMeter แล้ววนเรียก `getDeviceType()`

ถัดไป: [EP 2.9 — Collection, Optional และ Stream](ep09-collection-optional-stream.md)

