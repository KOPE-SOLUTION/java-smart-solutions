# EP 1.5 — รับข้อมูลด้วย Scanner

## เป้าหมาย

- รับ String, integer และ decimal จาก Console
- เข้าใจปัญหา newline หลัง `nextInt`/`nextDouble`
- ตรวจข้อมูลเบื้องต้นก่อนนำไปใช้

## สร้างไฟล์ `SensorInput.java`

```java
import java.util.Scanner;

public class SensorInput {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Machine ID: ");
            String machineId = scanner.nextLine().trim();

            System.out.print("Temperature: ");
            double temperature = scanner.nextDouble();

            System.out.print("Operating hours: ");
            int hours = scanner.nextInt();

            System.out.printf("%s | %.1f C | %d hours%n",
                    machineId, temperature, hours);
        }
    }
}
```

## Run

```powershell
javac -encoding UTF-8 SensorInput.java
java SensorInput
```

กรอก `M-001`, `67.5` และ `120` ตามลำดับ

## จุดที่ต้องระวัง

หลัง `nextInt()` หรือ `nextDouble()` ยังมี newline ค้างอยู่ ถ้าจะเรียก `nextLine()` ต่อ ให้รับ newline ทิ้งก่อน:

```java
scanner.nextLine();
String nextText = scanner.nextLine();
```

## Challenge

รับชื่อเครื่องจักรและตำแหน่งเพิ่ม จากนั้นแสดงข้อมูลทุกค่าในหนึ่งบรรทัด

ถัดไป: [EP 1.6 — if/else และ Logical Operator](ep06-condition-logical.md)

