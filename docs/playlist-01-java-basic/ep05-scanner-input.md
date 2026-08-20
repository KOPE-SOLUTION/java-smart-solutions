# EP 1.5 — รับข้อมูลด้วย Scanner

## เป้าหมาย

- รับข้อความ จำนวนเต็ม และเลขทศนิยมจาก Console
- ใช้ `nextLine`, `nextInt` และ `nextDouble`
- เข้าใจ newline ที่ค้างหลังการรับตัวเลข

## 1. สร้างโครงไฟล์

สร้างไฟล์ `SensorInput.java`:

```java
import java.util.Scanner;

public class SensorInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // เพิ่มโค้ดแต่ละส่วนตรงนี้

        scanner.close();
    }
}
```

ให้วาง Snippet ต่อไปนี้ก่อน `scanner.close();`

## 2. รับรหัสเครื่องจักร

```java
System.out.print("Machine ID: ");
String machineId = scanner.nextLine().trim();

System.out.println("Machine: " + machineId);
```

Compile และ Run เพื่อทดสอบการรับข้อความก่อน:

```powershell
javac -encoding UTF-8 SensorInput.java
java SensorInput
```

## 3. รับตัวเลข

```java
System.out.print("Temperature: ");
double temperature = scanner.nextDouble();

System.out.print("Operating hours: ");
int hours = scanner.nextInt();
```

## 4. แสดงผลรวม

```java
System.out.printf("%s | %.1f C | %d hours%n", machineId, temperature, hours);
```

ลองกรอก `M-001`, `67.5` และ `120` ตามลำดับ

## จุดที่ต้องระวัง

หลัง `nextInt()` หรือ `nextDouble()` ยังมี newline ค้างอยู่ ถ้าจะรับข้อความต่อ ให้รับ newline ทิ้งก่อน:

```java
scanner.nextLine();

System.out.print("Machine name: ");
String machineName = scanner.nextLine();
```

## Challenge

รับชื่อเครื่องจักรและตำแหน่งเพิ่ม แล้วแสดงข้อมูลทุกค่าในหนึ่งบรรทัด

ถัดไป: [EP 1.6 — if/else และ Logical Operator](ep06-condition-logical.md)
