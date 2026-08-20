# EP 2.1 — Class และ Object

## เป้าหมาย

- แยกความหมายของ Class กับ Object
- เปลี่ยนข้อมูลเครื่องจักรจาก Array เป็น Object
- สร้าง Object หลายตัวจาก Class เดียว

## แนวคิด

Class คือแบบพิมพ์ ส่วน Object คือสิ่งที่สร้างขึ้นจากแบบพิมพ์นั้น `Machine` หนึ่ง Class จึงสร้าง Mixer, Conveyor และ Pump ได้หลาย Object

## สร้างไฟล์ `ClassObjectDemo.java`

```java
public class ClassObjectDemo {
    static class Machine {
        String id;
        String name;
        double temperature;
    }

    public static void main(String[] args) {
        Machine mixer = new Machine();
        mixer.id = "M-001";
        mixer.name = "Mixer";
        mixer.temperature = 65.5;

        Machine conveyor = new Machine();
        conveyor.id = "M-002";
        conveyor.name = "Conveyor";
        conveyor.temperature = 82.3;

        System.out.println(mixer.id + " " + mixer.name);
        System.out.println(conveyor.id + " " + conveyor.name);
    }
}
```

## Checkpoint

- `mixer` และ `conveyor` มี Class เดียวกัน แต่เก็บ state คนละชุด
- การแก้ `mixer.temperature` ไม่กระทบ `conveyor.temperature`
- Field ยังเปิดเป็นค่า default เพราะยังไม่ได้ทำ Encapsulation

## Challenge

สร้าง Object `pump` เพิ่มและพิมพ์ข้อมูลทั้งสามเครื่องด้วย method ชั่วคราวหนึ่ง method

ถัดไป: [EP 2.2 — Field, Method และ Constructor](ep02-field-method-constructor.md)

