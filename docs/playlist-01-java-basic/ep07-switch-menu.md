# EP 1.7 — switch และเมนูคำสั่ง

## เป้าหมาย

- รับหมายเลขเมนูจากผู้ใช้
- เลือกผลลัพธ์ด้วย `switch`
- แยกกรณีที่ผู้ใช้เลือกไม่ถูกต้อง

## 1. สร้างโครงไฟล์

สร้างไฟล์ `FactoryMenu.java`:

```java
import java.util.Scanner;

public class FactoryMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // เพิ่มโค้ดแต่ละส่วนตรงนี้

        scanner.close();
    }
}
```

## 2. แสดงและรับเมนู

วางก่อน `scanner.close();`

```java
System.out.println("1. Show machines");
System.out.println("2. Read sensor");
System.out.println("3. Maintenance");
System.out.print("Select: ");

int menu = scanner.nextInt();
```

## 3. เลือกการทำงาน

วางต่อจากส่วนเดิม:

```java
String action = switch (menu) {
    case 1 -> "Showing all machines";
    case 2 -> "Reading sensor";
    case 3 -> "Starting maintenance";
    default -> "Unknown menu";
};

System.out.println(action);
```

`switch` เหมาะกับการเปรียบเทียบตัวแปรเดียวกับค่าคงที่หลายค่า ส่วน `if/else` เหมาะกับช่วงตัวเลขหรือเงื่อนไขผสม

## Challenge

เพิ่มเมนู `0` สำหรับออกจากโปรแกรม และเมนู `4` สำหรับแสดงจำนวนชั่วโมงทำงาน

ถัดไป: [EP 1.8 — Loop](ep08-loop.md)
