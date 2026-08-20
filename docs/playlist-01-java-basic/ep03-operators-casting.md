# EP 1.3 — Operator และ Type Casting

## เป้าหมาย

- ใช้ Arithmetic และ Comparison Operator
- เข้าใจ integer division
- แปลงชนิดตัวเลขแบบ explicit casting

## สร้างไฟล์ `SensorCalculation.java`

```java
public class SensorCalculation {
    public static void main(String[] args) {
        double temperature1 = 65.5;
        double temperature2 = 72.0;
        double temperature3 = 82.3;

        double average = (temperature1 + temperature2 + temperature3) / 3;
        double differenceFromLimit = 80.0 - average;
        int roundedDown = (int) average;

        System.out.println("Average: " + average);
        System.out.println("Distance from limit: " + differenceFromLimit);
        System.out.println("Rounded down: " + roundedDown);
        System.out.println("Above limit: " + (temperature3 >= 80.0));
    }
}
```

Operator ที่ใช้บ่อย:

- คำนวณ: `+`, `-`, `*`, `/`, `%`
- เปรียบเทียบ: `>`, `>=`, `<`, `<=`, `==`, `!=`
- กำหนดค่าแบบย่อ: `+=`, `-=`, `++`, `--`

## จุดที่ต้องระวัง

```java
System.out.println(5 / 2);          // 2
System.out.println(5.0 / 2);        // 2.5
System.out.println((double) 5 / 2); // 2.5
```

ถ้าตัวตั้งและตัวหารเป็น `int` ผลลัพธ์จะตัดทศนิยมก่อนนำไปเก็บ

## Challenge

คำนวณค่าเฉลี่ยแรงสั่นสามครั้ง และคำนวณว่าเหลืออีกกี่ชั่วโมงก่อนครบกำหนดบำรุงที่ 500 ชั่วโมง

ถัดไป: [EP 1.4 — String และ Output Formatting](ep04-string-output-format.md)

