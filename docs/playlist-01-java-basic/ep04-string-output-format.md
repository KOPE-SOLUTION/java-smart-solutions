# EP 1.4 — String และ Output Formatting

## เป้าหมาย

- ต่อและเปรียบเทียบ String อย่างถูกต้อง
- ใช้ `printf` จัดคอลัมน์และทศนิยม
- สร้าง output ที่อ่านเหมือนรายงาน

## สร้างไฟล์ `FactoryReport.java`

```java
public class FactoryReport {
    public static void main(String[] args) {
        String machineId = "M-001";
        String machineName = "Mixer";
        double temperature = 65.567;
        int hours = 120;

        String title = "SMART FACTORY REPORT";
        System.out.println("=== " + title + " ===");
        System.out.printf("%-8s %-15s %10s %8s%n", "ID", "NAME", "TEMP", "HOURS");
        System.out.printf("%-8s %-15s %8.1f C %8d%n",
                machineId, machineName, temperature, hours);
    }
}
```

ตัวกำหนดรูปแบบ:

- `%s` ข้อความ
- `%d` จำนวนเต็ม
- `%.1f` ทศนิยมหนึ่งตำแหน่ง
- `%-15s` ข้อความชิดซ้ายในพื้นที่ 15 ตัวอักษร
- `%n` ขึ้นบรรทัดใหม่ตามระบบปฏิบัติการ

## เปรียบเทียบ String

```java
String status = "RUNNING";
System.out.println(status.equals("RUNNING"));
```

ใช้ `.equals(...)` เปรียบเทียบเนื้อหา String ไม่ใช้ `==`

## Challenge

เพิ่มคอลัมน์ Location และ Status พร้อมจัดตารางไม่ให้ข้อความชนกัน

ถัดไป: [EP 1.5 — รับข้อมูลด้วย Scanner](ep05-scanner-input.md)

