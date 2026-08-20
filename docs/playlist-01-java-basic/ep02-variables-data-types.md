# EP 1.2 — Variable, Data Type และ Constant

## เป้าหมาย

- เลือกชนิดข้อมูลให้เหมาะกับงาน
- ประกาศและเปลี่ยนค่าตัวแปร
- ใช้ `final` กับค่าที่ไม่ควรถูกเปลี่ยน

## 1. สร้างโครงไฟล์

สร้างไฟล์ `MachineVariables.java`:

```java
public class MachineVariables {
    public static void main(String[] args) {
        // เพิ่มโค้ดแต่ละส่วนตรงนี้
    }
}
```

## 2. เก็บชื่อและรหัสเครื่องจักร

วางภายใน `main`:

```java
String machineId = "M-001";
String machineName = "Mixer";

System.out.println(machineId + " - " + machineName);
```

`String` ใช้เก็บข้อความ เช่น รหัสและชื่อเครื่องจักร

## 3. เพิ่มข้อมูลชนิดอื่น

วางต่อจากส่วนเดิม:

```java
double temperature = 65.5;
int operatingHours = 120;
boolean online = true;
char productionLine = 'A';

System.out.println("Temperature: " + temperature);
System.out.println("Operating hours: " + operatingHours);
System.out.println("Online: " + online);
System.out.println("Line: " + productionLine);
```

| Type | ใช้เก็บ | ตัวอย่าง |
|---|---|---|
| `String` | ข้อความ | รหัสและชื่อเครื่อง |
| `double` | เลขทศนิยม | อุณหภูมิ |
| `int` | จำนวนเต็ม | ชั่วโมงทำงาน |
| `boolean` | จริง/เท็จ | ออนไลน์หรือไม่ |
| `char` | อักขระหนึ่งตัว | Production Line |

## 4. เพิ่มค่าคงที่

```java
final double MAX_TEMPERATURE = 80.0;
System.out.println("Temperature limit: " + MAX_TEMPERATURE);
```

ตัวแปรที่มี `final` กำหนดค่าได้ครั้งเดียว ชื่อค่าคงที่นิยมเขียนด้วยตัวพิมพ์ใหญ่

## 5. Compile และ Run

```powershell
javac -encoding UTF-8 MachineVariables.java
java MachineVariables
```

## ลองเพิ่มข้อมูลจาก Sensor

วางตัวแปรเหล่านี้ใน `main`:

```java
double vibration = 2.35;
int rpm = 1450;

System.out.println("Vibration: " + vibration + " mm/s");
System.out.println("RPM: " + rpm);
```

วันที่ควรใช้ `LocalDate` แทนข้อความ โดยเพิ่มบรรทัดนี้เหนือ `public class`:

```java
import java.time.LocalDate;
```

แล้วเพิ่มใน `main`:

```java
LocalDate lastInspectionDate = LocalDate.of(2026, 8, 20);
System.out.println("Last inspection: " + lastInspectionDate);
```

## Checkpoint

ลองเปลี่ยน `temperature` เป็นข้อความ แล้วสังเกตว่า Java ป้องกันข้อมูลผิดชนิดตั้งแต่ Compile Time อย่างไร

## Challenge

เปลี่ยนค่าตัวแปรทั้งหมดให้เป็นข้อมูลเครื่องจักรที่คุณต้องการใช้ใน Smart Factory ของตัวเอง

ถัดไป: [EP 1.3 — Operator และ Type Casting](ep03-operators-casting.md)
