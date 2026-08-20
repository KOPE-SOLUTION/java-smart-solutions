# EP 1.9 — Array และ Enhanced for

## เป้าหมาย

- เก็บข้อมูลหลายค่าชนิดเดียวกันใน Array
- อ่านข้อมูลด้วย Index
- วนอ่านข้อมูลด้วย `for` และ enhanced `for`

## 1. สร้างโครงไฟล์

สร้างไฟล์ `MachineArray.java`:

```java
public class MachineArray {
    public static void main(String[] args) {
        // เพิ่มโค้ดแต่ละส่วนตรงนี้
    }
}
```

## 2. สร้าง Array

```java
String[] names = {"Mixer", "Conveyor", "Water Pump"};
double[] temperatures = {65.5, 82.3, 58.0};
```

ทดลองอ่านตำแหน่งแรก:

```java
System.out.println(names[0]);
System.out.println(temperatures[0]);
```

Index เริ่มจาก `0` ตำแหน่งสุดท้ายจึงเป็น `length - 1`

## 3. วนแสดงข้อมูลด้วย Index

```java
for (int index = 0; index < names.length; index++) {
    System.out.println(names[index] + " | " + temperatures[index] + " C");
}
```

ใช้ `index < names.length` ไม่ใช้ `<=` เพราะจะอ่านเกินขอบเขต Array

## 4. หาค่าเฉลี่ยด้วย enhanced `for`

```java
double total = 0;

for (double temperature : temperatures) {
    total += temperature;
}

double average = total / temperatures.length;
System.out.println("Average: " + average + " C");
```

## ข้อจำกัดที่พาเข้าสู่ OOP

เมื่อเพิ่ม location, vibration และ hours จะต้องสร้าง Array แยกหลายชุดและรักษา Index ให้ตรงกัน OOP จะรวมข้อมูลของเครื่องหนึ่งเครื่องไว้ใน Object เดียว

## Challenge

หาค่าอุณหภูมิสูงสุดพร้อมชื่อเครื่องจักรที่มีค่านั้น

ถัดไป: [EP 1.10 — Method และ Console Capstone](ep10-method-console-capstone.md)
