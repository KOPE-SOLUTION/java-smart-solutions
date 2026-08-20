# EP 2.1 — Class และ Object

## เป้าหมาย

- แยกความหมายของ Class กับ Object
- สร้าง Object จาก Class
- เห็นว่าแต่ละ Object เก็บข้อมูลของตัวเอง

Class คือแบบพิมพ์ ส่วน Object คือสิ่งที่สร้างขึ้นจากแบบพิมพ์นั้น Class `Machine` เดียวจึงใช้สร้าง Mixer, Conveyor และเครื่องจักรอื่นได้

## 1. สร้าง Class

สร้างไฟล์ `Machine.java`:

```java
public class Machine {
    String id;
    String name;
    double temperature;
}
```

Field คือข้อมูลที่ Object แต่ละตัวเก็บไว้

## 2. สร้างไฟล์สำหรับทดลอง

สร้างไฟล์ `ClassObjectDemo.java`:

```java
public class ClassObjectDemo {
    public static void main(String[] args) {
        // สร้าง Object ในส่วนถัดไป
    }
}
```

## 3. สร้าง Object แรก

วางภายใน `main`:

```java
Machine mixer = new Machine();
mixer.id = "M-001";
mixer.name = "Mixer";
mixer.temperature = 65.5;

System.out.println(mixer.id + " | " + mixer.name);
```

`new Machine()` สร้าง Object ส่วนตัวแปร `mixer` ใช้อ้างถึง Object นั้น

## 4. สร้าง Object ที่สอง

```java
Machine conveyor = new Machine();
conveyor.id = "M-002";
conveyor.name = "Conveyor";
conveyor.temperature = 82.3;

System.out.println(conveyor.id + " | " + conveyor.name);
```

Compile และ Run:

```powershell
javac -encoding UTF-8 Machine.java ClassObjectDemo.java
java ClassObjectDemo
```

การแก้ `mixer.temperature` ไม่กระทบ `conveyor.temperature` เพราะเป็นคนละ Object

## Challenge

สร้าง Object `pump` เพิ่ม แล้วพิมพ์รหัส ชื่อ และอุณหภูมิ

ถัดไป: [EP 2.2 — Field, Method และ Constructor](ep02-field-method-constructor.md)
