# EP 1.7 — switch และเมนูคำสั่ง

## เป้าหมาย

- เลือกการทำงานจากค่าหนึ่งค่า
- ใช้ switch expression แบบ Java รุ่นใหม่
- สร้างเมนู Console

## สร้างไฟล์ `FactoryMenu.java`

```java
import java.util.Scanner;

public class FactoryMenu {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("1. Show machines");
            System.out.println("2. Read sensor");
            System.out.println("3. Maintenance");
            System.out.print("Select: ");
            int menu = scanner.nextInt();

            String action = switch (menu) {
                case 1 -> "Showing all machines";
                case 2 -> "Reading sensor";
                case 3 -> "Starting maintenance";
                default -> "Unknown menu";
            };

            System.out.println(action);
        }
    }
}
```

switch เหมาะเมื่อเปรียบเทียบตัวแปรเดียวกับค่าคงที่หลายค่า ส่วน `if/else` เหมาะกับช่วงตัวเลขหรือเงื่อนไขผสม

## Challenge

เพิ่มเมนู 0 สำหรับออกจากโปรแกรม และเมนู 4 สำหรับแสดงจำนวนชั่วโมงทำงาน

ถัดไป: [EP 1.8 — Loop](ep08-loop.md)

