# EP 2.2 — Field, Method และ Constructor

## เป้าหมาย

- แยกข้อมูลใน Field ออกจากพฤติกรรมใน Method
- กำหนดข้อมูลเริ่มต้นผ่าน Constructor
- ใช้ `this` อ้างถึง Object ปัจจุบัน

ทำงานต่อในไฟล์ `Machine.java` จาก EP 2.1

## 1. เพิ่ม Constructor

วางภายใน Class `Machine`:

```java
public Machine(String id, String name) {
    this.id = id;
    this.name = name;
    this.temperature = 0;
}
```

Constructor ไม่มี Return Type และทำงานเมื่อใช้ `new`

กลับไปที่ `ClassObjectDemo.java` แล้วเปลี่ยนการสร้าง Object เป็น:

```java
Machine mixer = new Machine("M-001", "Mixer");
```

## 2. เพิ่ม Method เปลี่ยนอุณหภูมิ

วางภายใน Class `Machine`:

```java
public void updateTemperature(double temperature) {
    this.temperature = temperature;
}
```

เรียก Method ภายใน `main`:

```java
mixer.updateTemperature(65.5);
```

## 3. เพิ่ม Method สรุปข้อมูล

```java
public String getSummary() {
    return id + " | " + name + " | " + temperature + " C";
}
```

เรียกใช้งาน:

```java
System.out.println(mixer.getSummary());
```

- Field เก็บ State ของ Object
- Constructor กำหนดค่าเริ่มต้น
- Method แสดงพฤติกรรมของ Object

## Challenge

เพิ่ม Constructor อีกแบบที่รับอุณหภูมิเริ่มต้น แล้วสร้าง Object โดยไม่ต้องเรียก `updateTemperature`

ถัดไป: [EP 2.3 — Encapsulation และ Validation](ep03-encapsulation-validation.md)
