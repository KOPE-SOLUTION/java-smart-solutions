# EP 1.4 — String และ Output Formatting

## เป้าหมาย

- ต่อข้อความด้วย `+`
- ใช้ `printf` กำหนดจำนวนตำแหน่งทศนิยม
- เปรียบเทียบเนื้อหา String ด้วย `.equals(...)`

## 1. สร้างโครงไฟล์

สร้างไฟล์ `FactoryReport.java`:

```java
public class FactoryReport {
    public static void main(String[] args) {
        // เพิ่มโค้ดแต่ละส่วนตรงนี้
    }
}
```

## 2. เตรียมข้อมูล

```java
String machineId = "M-001";
String machineName = "Mixer";
double temperature = 65.567;
int hours = 120;
```

## 3. ต่อข้อความด้วย `+`

```java
String title = "SMART FACTORY REPORT";
System.out.println("=== " + title + " ===");
System.out.println(machineId + " | " + machineName);
```

## 4. จัดทศนิยมด้วย `printf`

```java
System.out.printf("Temperature: %.1f C%n", temperature);
System.out.printf("Operating hours: %d%n", hours);
```

- `%s` ใช้กับข้อความ
- `%d` ใช้กับจำนวนเต็ม
- `%.1f` แสดงทศนิยมหนึ่งตำแหน่ง
- `%n` ขึ้นบรรทัดใหม่

เมื่อเข้าใจแล้วจึงทดลองจัดเป็นตาราง:

```java
System.out.printf("%-8s %-15s %10s %8s%n", "ID", "NAME", "TEMP", "HOURS");
System.out.printf("%-8s %-15s %8.1f C %8d%n", machineId, machineName, temperature, hours);
```

## 5. เปรียบเทียบ String

```java
String status = "RUNNING";
boolean running = status.equals("RUNNING");
System.out.println("Running: " + running);
```

ใช้ `.equals(...)` เปรียบเทียบเนื้อหา String ไม่ใช้ `==`

## Challenge

เพิ่มตัวแปร Location และ Status จากนั้นเพิ่มเป็นสองคอลัมน์ใหม่ในตาราง

ถัดไป: [EP 1.5 — รับข้อมูลด้วย Scanner](ep05-scanner-input.md)
