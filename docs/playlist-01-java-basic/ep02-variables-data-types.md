# EP 1.2 — Variable, Data Type และ Constant

## เป้าหมาย

- เลือกชนิดข้อมูลให้เหมาะกับงาน
- ประกาศและเปลี่ยนค่าตัวแปร
- ใช้ `final` กับค่าที่ไม่ควรถูกเปลี่ยน

## สร้างไฟล์ `MachineVariables.java`

```java
public class MachineVariables {
    public static void main(String[] args) {
        String machineId = "M-001";
        String machineName = "Mixer";
        double temperature = 65.5;
        int operatingHours = 120;
        boolean online = true;
        char productionLine = 'A';
        final double MAX_TEMPERATURE = 80.0;

        System.out.println(machineId + " - " + machineName);
        System.out.println("Temperature: " + temperature);
        System.out.println("Operating hours: " + operatingHours);
        System.out.println("Online: " + online);
        System.out.println("Line: " + productionLine);
        System.out.println("Limit: " + MAX_TEMPERATURE);
    }
}
```

| Type | ใช้เก็บ | ตัวอย่าง |
|---|---|---|
| `String` | ข้อความ | รหัสและชื่อเครื่อง |
| `double` | เลขทศนิยม | อุณหภูมิ |
| `int` | จำนวนเต็ม | ชั่วโมงทำงาน |
| `boolean` | จริง/เท็จ | ออนไลน์หรือไม่ |
| `char` | อักขระหนึ่งตัว | Production Line |

## Run

```powershell
javac -encoding UTF-8 MachineVariables.java
java MachineVariables
```

## Checkpoint

ลองเปลี่ยน `temperature` เป็นข้อความ และสังเกตว่า Java ป้องกันข้อมูลผิดชนิดตั้งแต่ Compile Time อย่างไร

## Challenge

เพิ่มตัวแปรแรงสั่นสะเทือน, จำนวนรอบต่อนาที และวันที่ตรวจล่าสุด โดยเลือก Type ให้เหมาะสม

ถัดไป: [EP 1.3 — Operator และ Type Casting](ep03-operators-casting.md)

