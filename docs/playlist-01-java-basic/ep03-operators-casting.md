# EP 1.3 — Operator และ Type Casting

## เป้าหมาย

- คำนวณค่า Sensor ด้วย `+`, `-` และ `/`
- ปรับค่าตัวแปรด้วย `+=`, `-=`, `++` และ `--`
- เปรียบเทียบชั่วโมงทำงานด้วย `>=`
- เห็นความต่างระหว่าง Integer Division กับ Explicit Casting

ทำตามทีละส่วนและรันดูผลหลังจบแต่ละหัวข้อ เพื่อเห็นหน้าที่ของ Operator แต่ละกลุ่มแยกจากกัน

## 1. สร้างโครงไฟล์

สร้างไฟล์ `MachineCalculation.java`:

```java
public class MachineCalculation {
    public static void main(String[] args) {
        // เพิ่มโค้ดแต่ละส่วนตรงนี้
    }
}
```

## 2. คำนวณค่าเฉลี่ยแรงสั่นสะเทือน

วางภายใน `main`:

```java
double vibration1 = 2.35;
double vibration2 = 2.50;
double vibration3 = 2.20;

double averageVibration = (vibration1 + vibration2 + vibration3) / 3.0;
System.out.println("Average vibration: " + averageVibration + " mm/s");
```

วงเล็บทำให้ Java บวกทั้งสามค่าก่อนหาร และใช้ `3.0` เพื่อให้ผลลัพธ์เป็นเลขทศนิยม

รันแล้วควรได้:

```text
Average vibration: 2.35 mm/s
```

## 3. ปรับค่าจากค่าเดิม

วางต่อใน `main`:

```java
int operatingHours = 420;

operatingHours += 8;
System.out.println("After += 8: " + operatingHours);

operatingHours -= 3;
System.out.println("After -= 3: " + operatingHours);
```

`operatingHours += 8` มีความหมายเหมือน `operatingHours = operatingHours + 8`

ทดลอง `++` และ `--` ต่อจากส่วนเดิม:

```java
int inspectionCount = 3;

inspectionCount++;
System.out.println("After ++: " + inspectionCount);

inspectionCount--;
System.out.println("After --: " + inspectionCount);
```

`++` เพิ่มหนึ่ง และ `--` ลดหนึ่ง เหมาะกับตัวนับจำนวนครั้ง

## 4. ตรวจรอบบำรุงรักษา

```java
final int MAINTENANCE_INTERVAL_HOURS = 500;
int remainingHours = MAINTENANCE_INTERVAL_HOURS - operatingHours;
boolean maintenanceDue = operatingHours >= MAINTENANCE_INTERVAL_HOURS;

System.out.println("Remaining hours: " + remainingHours);
System.out.println("Maintenance due: " + maintenanceDue);
```

เมื่อชั่วโมงทำงานเป็น `425` จะเหลือ `75` ชั่วโมง และ `maintenanceDue` เป็น `false`

## 5. Integer Division และ Explicit Casting

เริ่มจากหารจำนวนเต็ม:

```java
int remainingFullDays = remainingHours / 24;
System.out.println("Remaining full days: " + remainingFullDays);
```

`75 / 24` ได้ `3` เพราะตัวตั้งและตัวหารเป็น `int` Java จึงตัดทศนิยมทิ้ง เรียกว่า Integer Division

ถ้าต้องการทศนิยม ให้แปลงค่าหนึ่งด้านเป็น `double`:

```java
double remainingExactDays = (double) remainingHours / 24;
System.out.println("Remaining exact days: " + remainingExactDays);
```

`(double)` คือ Explicit Casting ผลลัพธ์จึงเป็น `3.125`

## 6. Compile และ Run

```powershell
javac -encoding UTF-8 MachineCalculation.java
java -Dfile.encoding=UTF-8 MachineCalculation
```

## Checkpoint

- เปลี่ยน `operatingHours` เป็น `480` แล้วทำนายผลก่อนรัน
- เปรียบเทียบ `remainingHours / 24` กับ `(double) remainingHours / 24`
- เปลี่ยน `3.0` ในสูตรค่าเฉลี่ยเป็น `3` แล้วสังเกตว่าผลยังเป็นทศนิยมเพราะค่าแรงสั่นเป็น `double`

## Challenge

ทำทีละข้อและรันใหม่ทุกครั้ง:

1. เพิ่มค่าแรงสั่นสะเทือนครั้งที่สี่ แล้วเปลี่ยนตัวหารเป็น `4.0`
2. กำหนดชั่วโมงทำงานให้เกิน `500` แล้วตรวจ `maintenanceDue`
3. ใช้ `operatingHours - MAINTENANCE_INTERVAL_HOURS` หาจำนวนชั่วโมงที่เกินกำหนด
4. ใช้ `/ 24` หาจำนวนวันเต็ม และใช้ `% 24` หาเศษชั่วโมง

ถัดไป: [EP 1.4 — String และ Output Formatting](ep04-string-output-format.md)
