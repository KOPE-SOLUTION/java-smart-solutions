# EP 1.9 — Array และ Enhanced for

## เป้าหมาย

- เก็บข้อมูลหลายค่าชนิดเดียวกัน
- อ่านและแก้ข้อมูลผ่าน index
- วนข้อมูลด้วย for และ enhanced for

## สร้างไฟล์ `MachineArray.java`

```java
public class MachineArray {
    public static void main(String[] args) {
        String[] names = {"Mixer", "Conveyor", "Water Pump"};
        double[] temperatures = {65.5, 82.3, 58.0};

        for (int index = 0; index < names.length; index++) {
            System.out.printf("%d. %-12s %.1f C%n",
                    index + 1, names[index], temperatures[index]);
        }

        double total = 0;
        for (double temperature : temperatures) {
            total += temperature;
        }
        System.out.printf("Average: %.1f C%n", total / temperatures.length);
    }
}
```

Index เริ่มที่ 0 และตำแหน่งสุดท้ายคือ `length - 1` การใช้ `index <= length` จะเกินขอบเขต

## ข้อจำกัดที่พาเข้าสู่ OOP

เมื่อเพิ่ม location, vibration และ hours ต้องสร้าง Array แยกหลายชุด และต้องรักษา index ให้ตรงกันทั้งหมด OOP จะรวมข้อมูลของเครื่องหนึ่งเครื่องไว้ใน Object เดียว

## Challenge

หาค่าอุณหภูมิสูงสุดพร้อมชื่อเครื่องจักรที่มีค่านั้น

ถัดไป: [EP 1.10 — Method และ Console Capstone](ep10-method-console-capstone.md)

