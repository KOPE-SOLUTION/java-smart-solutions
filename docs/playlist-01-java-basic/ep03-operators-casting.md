# EP 1.3 — Operator และ Type Casting

## เป้าหมาย

- ใช้ Arithmetic Operator คำนวณข้อมูลเครื่องจักร
- ใช้ Comparison Operator ตรวจเงื่อนไขเป็น `true` หรือ `false`
- กำหนดค่าแบบย่อด้วย `+=`, `-=`, `++` และ `--`
- เข้าใจลำดับการคำนวณและการใช้วงเล็บ
- แยกความแตกต่างระหว่าง Integer Division กับการหารแบบทศนิยม
- แปลงชนิดตัวเลขด้วย Explicit Casting อย่างระมัดระวัง

## Operator ที่ใช้ในบทนี้

| กลุ่ม | Operator | ตัวอย่าง | ความหมาย |
|---|---|---|---|
| Arithmetic | `+` | `a + b` | บวก |
| Arithmetic | `-` | `a - b` | ลบ |
| Arithmetic | `*` | `a * b` | คูณ |
| Arithmetic | `/` | `a / b` | หาร |
| Arithmetic | `%` | `a % b` | หาเศษจากการหาร |
| Comparison | `>`, `>=` | `hours >= 500` | มากกว่า หรือมากกว่าเท่ากับ |
| Comparison | `<`, `<=` | `value < limit` | น้อยกว่า หรือน้อยกว่าเท่ากับ |
| Comparison | `==`, `!=` | `rpm == target` | เท่ากัน หรือไม่เท่ากัน |
| Compound assignment | `+=`, `-=` | `hours += 8` | คำนวณแล้วเก็บค่ากลับตัวแปรเดิม |
| Increment / Decrement | `++`, `--` | `count++` | เพิ่มหรือลดค่าครั้งละหนึ่ง |

## สร้างไฟล์ `MachineCalculation.java`

ตัวอย่างนี้นำข้อมูลจาก EP 1.2 มาคำนวณค่าเฉลี่ยแรงสั่นสะเทือนและรอบบำรุงรักษา

```java
public class MachineCalculation {
    public static void main(String[] args) {
        double vibration1 = 2.35;
        double vibration2 = 2.50;
        double vibration3 = 2.20;

        double totalVibration =
                vibration1 + vibration2 + vibration3;

        double averageVibration =
                totalVibration / 3.0;

        int operatingHours = 420;
        final int MAINTENANCE_INTERVAL_HOURS = 500;

        operatingHours += 8;
        System.out.println(
                "Operating hours after += 8: " + operatingHours
        );

        operatingHours -= 3;
        System.out.println(
                "Operating hours after -= 3: " + operatingHours
        );

        int inspectionCount = 3;

        inspectionCount++;
        System.out.println(
                "Inspection count after ++: " + inspectionCount
        );

        inspectionCount--;
        System.out.println(
                "Inspection count after --: " + inspectionCount
        );

        int remainingHours =
                MAINTENANCE_INTERVAL_HOURS - operatingHours;

        int remainingFullDays = remainingHours / 24;

        double remainingDays =
                (double) remainingHours / 24;

        int remainingHoursAfterFullDays = remainingHours % 24;
        int totalOperatingMinutes = operatingHours * 60;

        boolean maintenanceDue =
                operatingHours >= MAINTENANCE_INTERVAL_HOURS;

        int averageVibrationAsInteger =
                (int) averageVibration;

        System.out.println("Average vibration: "
                + averageVibration + " mm/s");
        System.out.println("Remaining hours: "
                + remainingHours);
        System.out.println("Remaining full days: "
                + remainingFullDays);
        System.out.println("Remaining exact days: "
                + remainingDays);
        System.out.println("Hours after full days: "
                + remainingHoursAfterFullDays);
        System.out.println("Total operating minutes: "
                + totalOperatingMinutes);
        System.out.println("Maintenance due: "
                + maintenanceDue);
        System.out.println("Vibration as integer: "
                + averageVibrationAsInteger);
    }
}
```

## Compile และ Run

```powershell
javac -encoding UTF-8 MachineCalculation.java
java -Dfile.encoding=UTF-8 MachineCalculation
```

ผลลัพธ์:

```text
Operating hours after += 8: 428
Operating hours after -= 3: 425
Inspection count after ++: 4
Inspection count after --: 3
Average vibration: 2.35 mm/s
Remaining hours: 75
Remaining full days: 3
Remaining exact days: 3.125
Hours after full days: 3
Total operating minutes: 25500
Maintenance due: false
Vibration as integer: 2
```

## กำหนดค่าแบบย่อ

### `+=` และ `-=`

```java
operatingHours += 8;
operatingHours -= 3;
```

มีความหมายเหมือนกับ:

```java
operatingHours = operatingHours + 8;
operatingHours = operatingHours - 3;
```

Operator แบบย่อช่วยลดการเขียนชื่อตัวแปรซ้ำ และแสดงให้เห็นชัดว่าต้องการปรับค่าจากค่าเดิม

### `++` และ `--`

```java
inspectionCount++;
inspectionCount--;
```

- `inspectionCount++` เพิ่มค่าครั้งละหนึ่ง
- `inspectionCount--` ลดค่าครั้งละหนึ่ง

เมื่อเขียนเป็นคำสั่งแยกบรรทัด `count++` และ `++count` ให้ผลสุดท้ายเหมือนกัน ความแตกต่างจะเกิดเมื่อใช้เป็นส่วนหนึ่งของนิพจน์อื่น จึงควรแยกเป็นคนละคำสั่งในช่วงเริ่มต้นเพื่อให้โค้ดอ่านง่าย

## ลำดับการคำนวณ

การคูณ หาร และหารเอาเศษทำงานก่อนการบวกและลบ วงเล็บช่วยระบุว่าต้องรวมค่าทั้งสามก่อนหาร

```java
double correctAverage =
        (vibration1 + vibration2 + vibration3) / 3.0;
```

ถ้าไม่ใส่วงเล็บ:

```java
double wrongAverage =
        vibration1 + vibration2 + vibration3 / 3.0;
```

Java จะหาร `vibration3` ก่อน แล้วจึงนำผลลัพธ์ไปบวกกับอีกสองค่า ทำให้ไม่ได้ค่าเฉลี่ยที่ต้องการ

## Integer Division

Integer Division คือการหารที่ตัวตั้งและตัวหารเป็นจำนวนเต็ม หากทั้งสองด้านเป็น `int` Java จะตัดส่วนทศนิยมทิ้งก่อนนำผลลัพธ์ไปเก็บ

```java
System.out.println(5 / 2);   // 2
System.out.println(75 / 24); // 3
```

แม้ตัวแปรปลายทางจะเป็น `double` แต่ถ้าการหารเกิดขึ้นระหว่าง `int` ผลลัพธ์ก็สูญเสียทศนิยมไปแล้ว

```java
double days = 75 / 24;
System.out.println(days); // 3.0 ไม่ใช่ 3.125
```

ทำให้ตัวเลขอย่างน้อยหนึ่งด้านเป็น `double` เพื่อรักษาทศนิยม:

```java
double daysWithDecimal = 75 / 24.0;
System.out.println(daysWithDecimal); // 3.125
```

## Explicit Casting

Explicit Casting คือการระบุให้ Java แปลงชนิดข้อมูลอย่างชัดเจน โดยเขียน Type ที่ต้องการไว้ในวงเล็บ

```java
double remainingDays =
        (double) remainingHours / 24;
```

`(double) remainingHours` ทำให้การหารครั้งนี้เป็นการหารแบบทศนิยม:

```text
75.0 / 24 = 3.125
```

การแปลงจาก `double` เป็น `int` ต้องเขียน Explicit Casting เช่นกัน:

```java
double averageVibration = 2.35;
int vibrationAsInteger = (int) averageVibration;
```

ค่าที่ได้คือ `2` เพราะ Casting เป็น `int` จะตัดส่วนทศนิยมทิ้ง ไม่ใช่การปัดเศษ และอาจทำให้ข้อมูลสูญหาย

## Comparison Operator

ผลลัพธ์จากการเปรียบเทียบเป็น `boolean` ซึ่งมีค่า `true` หรือ `false`

```java
boolean maintenanceDue =
        operatingHours >= MAINTENANCE_INTERVAL_HOURS;
```

เมื่อชั่วโมงทำงานเป็น `425` และกำหนดบำรุงรักษาเป็น `500` ผลลัพธ์จึงเป็น `false`

## Checkpoint

- เปลี่ยน `operatingHours` เป็น `480` แล้วคำนวณผลลัพธ์ใหม่
- เปรียบเทียบ `remainingHours / 24` กับ `(double) remainingHours / 24`
- ลบวงเล็บออกจากสูตรค่าเฉลี่ยแล้วสังเกตผลลัพธ์
- เปลี่ยน `averageVibrationAsInteger` กลับเป็น `double` และอธิบายว่าข้อมูลต่างกันอย่างไร

## Challenge

- เพิ่มค่าแรงสั่นสะเทือนครั้งที่สี่ แล้วปรับสูตรค่าเฉลี่ยให้ถูกต้อง
- กำหนดชั่วโมงทำงานให้เกิน `500` และตรวจค่าของ `maintenanceDue`
- คำนวณว่าเกินหรือเหลือจากกำหนดบำรุงรักษากี่ชั่วโมง
- ทดลองใช้ `%` แยกจำนวนวันเต็มและชั่วโมงที่เหลือ

ถัดไป: [EP 1.4 — String และ Output Formatting](ep04-string-output-format.md)
